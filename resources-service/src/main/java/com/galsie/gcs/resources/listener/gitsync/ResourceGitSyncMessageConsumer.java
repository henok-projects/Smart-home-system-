package com.galsie.gcs.resources.listener.gitsync;

import com.galsie.gcs.microservicecommon.lib.gcsjackson.GCSObjectMapper;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitMQCommonQueueType;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.consumer.GCSRabbitCommonQueueConsumer;
import com.galsie.gcs.resources.data.dto.GitSyncUpdateMessageDTO;
import com.galsie.gcs.resources.service.gitsync.GalAssetsGitRepositorySynchronizerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class ResourceGitSyncMessageConsumer {

    @Autowired
    GCSObjectMapper objectMapper;

    @Autowired
    GalAssetsGitRepositorySynchronizerService assetsGitRepositorySynchronizerService;

    @GCSRabbitCommonQueueConsumer(types = GCSRabbitMQCommonQueueType.RESOURCE_GIT_SYNC)
    public void consumeGitSyncBroadcast(Message message){
        try{
            assetsGitRepositorySynchronizerService.updateStatusFromGitSyncUpdateMessage(objectMapper.readValueFromMessage(message, GitSyncUpdateMessageDTO.class));
        } catch (IOException e) {
            log.error("Message sent to this queue is not a valid GitSyncUpdateMessageDTO");
        }
    }

}
