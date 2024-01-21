package com.galsie.gcs.microservicecommon.lib.gcssockets.init;

import com.galsie.gcs.microservicecommon.lib.gcssockets.handler.GCSSocketHandler;
import com.galsie.gcs.microservicecommon.lib.gcssockets.listener.OnPacket;
import com.galsie.gcs.microservicecommon.lib.gcssockets.listener.PacketListener;
import com.galsie.gcs.microservicecommon.lib.gcssockets.listener.registry.GCSGlobalPacketListenerRegistry;
import com.galsie.gcs.microservicecommon.lib.gcssockets.packet.Packet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;

@Slf4j
public class GCSGlobalPacketListenerRegistryAutoConf implements BeanPostProcessor {

    @Autowired
    GCSGlobalPacketListenerRegistry gcsGlobalPacketListenerRegistry;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        Method[] methods = beanClass.getDeclaredMethods();
        Class<? extends GCSSocketHandler>[] classHandlerTypes = null;
        PacketListener packetListener = beanClass.getAnnotation(PacketListener.class);
        if(packetListener != null){
            classHandlerTypes = packetListener.handlerTypes();
        }
        for (Method method : methods) {
            registerOnPacketMethod(method, bean, classHandlerTypes);
        }
        return bean;
    }

    /**
     * this method searches for methods annotated with {@link OnPacket} and registers them in the global packet listener registry
     * most methods annotated with {@link OnPacket} are in classes annotated with {@link PacketListener} so the handler types
     * of the {@link PacketListener} annotation are used to register the methods if the methods have no handler types specified
     * @param method
     * @param bean
     * @param classHandlerTypes
     */
    private void registerOnPacketMethod(Method method, Object bean, Class<? extends GCSSocketHandler>[] classHandlerTypes){
        if (!method.isAnnotationPresent(OnPacket.class)) {
            return;
        }
        OnPacket onPacket = method.getAnnotation(OnPacket.class);
        Class<? extends GCSSocketHandler>[] handlerTypes = onPacket.handlerTypes();
        if(handlerTypes == null|| handlerTypes.length == 0){//if the OnPacket method has no handler types, the handler type of the class is applied
            if(classHandlerTypes == null || classHandlerTypes.length == 0){
                log.info("@OnPacket method " + method.getName() + " in class " + bean.getClass().getName() + " has no handler types specified and the class has no handler types specified");
                log.info("The method will not be registered");
                return;
            }
            handlerTypes = classHandlerTypes;
        }
        Class<? extends Packet>[] packetDataTypes = onPacket.packetTypes();
        for(Class<? extends GCSSocketHandler> handlerClass: handlerTypes){
            for(Class<? extends  Packet> packetClass: packetDataTypes){
                gcsGlobalPacketListenerRegistry.addEntry(handlerClass, packetClass, bean, method);
            }
        }
    }

}
