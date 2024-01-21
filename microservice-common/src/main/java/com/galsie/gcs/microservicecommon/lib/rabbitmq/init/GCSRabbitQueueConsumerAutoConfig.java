package com.galsie.gcs.microservicecommon.lib.rabbitmq.init;

import com.galsie.gcs.microservicecommon.lib.galsecurity.init.GalSecurityCacheEnabledSessions;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.consumer.*;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.queues.GCSRabbitMQConfiguredQueue;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.queues.GCSRabbitMQConfiguredQueuesProvider;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitMQCommonQueueType;
import com.galsie.lib.utils.ArrayUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Finds all methods annotated with {@link GCSRabbitCommonQueueConsumer} and/or {@link GCSRabbitQueueConsumer}
 * - For {@link GCSRabbitCommonQueueConsumer}:Registers the {@link RegistrableGCSRabbitConsumer} for the default queue name for each defined common queue type
 * - For {@link GCSRabbitQueueConsumer}: Loads a {@link RegistrableGCSRabbitConsumer} for each of the specified queue names
 * NOTE: CAN have both annotations applied together
 */
@Getter
@Slf4j
public class GCSRabbitQueueConsumerAutoConfig implements BeanPostProcessor {

    @Autowired
    RabbitAdmin rabbitAdmin;

    @Autowired(required = false)
    GalSecurityCacheEnabledSessions galSecurityCacheEnabledSessions;

    @Autowired
    GCSRabbitMQConfiguredQueuesProvider gcsRabbitMQConfiguredQueuesProvider;

    @Autowired
    RegistrableGCSRabbitConsumerList registrableGCSRabbitConsumerList;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Method[] methods = bean.getClass().getMethods();
        for (Method method : methods) {
            var queueNames = ArrayUtils.joinArrays(getGCSRabbitQueueConsumerDefaultQueues(method), getGCSRabbitCommonQueueConsumerDefaultQueues(method));
            if (queueNames.isEmpty()){ // queue names would be 0 if there are no annotations or the annotations define no queues
                continue;
            }
            registrableGCSRabbitConsumerList.add(new RegistrableGCSRabbitConsumer(bean, method.getName(), queueNames.toArray(String[]::new)));
        }
        return bean;
    }

    /**
     * For every common queue types:
     * - Get all configured queue names for that type
     * @param method
     * @return
     */
    private String[] getGCSRabbitCommonQueueConsumerDefaultQueues(Method method){
        GCSRabbitCommonQueueConsumer annotation = AnnotationUtils.findAnnotation(method, GCSRabbitCommonQueueConsumer.class);
        if (annotation == null) {
            return new String[]{};
        }
        var shouldDisableConsumerIfNotInGCSSecurityCache = AnnotationUtils.findAnnotation(method, DisableConsumerIfNotInGCSSecurityCache.class) != null;
        return Arrays.stream(annotation.types()).map((queueType) -> this.getConfiguredQueueNamesForQueueType(queueType, shouldDisableConsumerIfNotInGCSSecurityCache)).flatMap(Arrays::stream).toArray(String[]::new);
    }

    private String[] getGCSRabbitQueueConsumerDefaultQueues(Method method){
        GCSRabbitQueueConsumer annotation = AnnotationUtils.findAnnotation(method, GCSRabbitQueueConsumer.class);
        if (annotation == null) {
            return new String[]{};
        }
        var queueNames = annotation.queues();
        var disableConsumerIfNotInGCSSecurityCache = AnnotationUtils.findAnnotation(method, DisableConsumerIfNotInGCSSecurityCache.class);
        if (disableConsumerIfNotInGCSSecurityCache != null){
            log.warn(disableConsumerIfNotInGCSSecurityCache.getClass() + " Not supported for " + annotation.getClass());
        }
        return queueNames;
    }

    public String[] getConfiguredQueueNamesForQueueType(GCSRabbitMQCommonQueueType commonQueueType, boolean shouldDisableConsumerIfNotInGCSSecurityCache){
        var queuesForType = gcsRabbitMQConfiguredQueuesProvider.getGcsRabbitMQConfiguredQueuesList().stream()
                .filter((configuredQueue) -> configuredQueue.getCommonQueueConfig().getCommonQueueType() == commonQueueType);

        // if tge consumer should be disabled when the queue type is a related to a session which is not in security cache
        if (shouldDisableConsumerIfNotInGCSSecurityCache){
            // we get the session type related to the commonQueueType
            var sessionTypeOpt = Arrays.stream(GalSecurityAuthSessionType.values()).filter((galSecurityAuthSessionType -> galSecurityAuthSessionType.getSessionListCacheCommonQueueType() == commonQueueType)).findFirst();
            // if the commonQueueType is associated with a session type and cache is not enabled for the session, we ignore all the queues for this commonQueueType
            if (sessionTypeOpt.isPresent() && (galSecurityCacheEnabledSessions == null || !galSecurityCacheEnabledSessions.getCacheEnabledSessionTypes().contains(sessionTypeOpt.get()))){
                return new String[]{};
            }
        }
        return queuesForType.map(GCSRabbitMQConfiguredQueue::getQueueName).toArray(String[]::new);
    }
}