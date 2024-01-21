package com.galsie.gcs.microservicecommon.lib.rabbitmq.queues;

import com.galsie.gcs.microservicecommon.config.gcsmicroservice.GCSMicroserviceGeneralLocalProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GCSRabbitMQConfiguredQueue {
   private GCSRabbitMQQueueConfiguration commonQueueConfig;
   private String queueName;


   public static GCSRabbitMQConfiguredQueue fromConfiguration(GCSMicroserviceGeneralLocalProperties gcsMicroserviceGeneralLocalProperties, GCSRabbitMQQueueConfiguration commonExchangeBindingConfiguration){
       String queueName = GCSRabbitMQConfiguredQueue.getQueueName(gcsMicroserviceGeneralLocalProperties, commonExchangeBindingConfiguration);
       return new GCSRabbitMQConfiguredQueue(commonExchangeBindingConfiguration, queueName);
   }

   public static String getQueueName(GCSMicroserviceGeneralLocalProperties gcsMicroserviceGeneralLocalProperties, GCSRabbitMQQueueConfiguration commonExchangeBindingConfiguration){
       var queueName = commonExchangeBindingConfiguration.getQueueName(); // get the queue name as configured
       if (queueName == null){
           queueName = commonExchangeBindingConfiguration.getCommonQueueType().getDefaultQueueName(); // if it was nil, get default queue name
       }
       if (commonExchangeBindingConfiguration.isUniqueQueuePerMicroservice()) {
           queueName = gcsMicroserviceGeneralLocalProperties.getMicroserviceName() + "-" + gcsMicroserviceGeneralLocalProperties.getInstanceUniqueId() + "-" + queueName;
       }
       return queueName;
   }
}
