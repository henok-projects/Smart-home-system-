package com.galsie.gcs.users.listeners.rabbitmq;

import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.lastaccess.AuthSessionLastAccessUpdateDTO;
import com.galsie.gcs.microservicecommon.lib.gcsjackson.GCSObjectMapper;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitMQCommonQueueType;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.consumer.GCSRabbitCommonQueueConsumer;
import com.galsie.gcs.users.service.authentication.LocalUserAuthenticatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
public class UserAuthenticationRabbitMQConsumer {
    @Autowired
    private GCSObjectMapper objectMapper;

    @Autowired
    LocalUserAuthenticatorService localUserAuthenticatorService;

    @GCSRabbitCommonQueueConsumer(types = GCSRabbitMQCommonQueueType.USER_AUTH_LAST_ACCESS)
    public void onReceiveAuthLastAccessUpdate(Message message){
        var authSessionLastAccessUpdateOpt = getAuthSessionLastAccessUpdate(message);
        if (authSessionLastAccessUpdateOpt.isEmpty()){
            return; // already logged error
        }
        var response = localUserAuthenticatorService.receiveAuthSessionLastAccessUpdate(authSessionLastAccessUpdateOpt.get());
        if (response.hasError()){
            log.info("Failed to receive auth session last access update, reason:" + response.getGcsError().getErrorType()); // TODO: Change to log error
            return;
        }
        var lastAccessUpdateResponseDTO = response.getResponseData();
        if (lastAccessUpdateResponseDTO.hasError()){
            log.info("Failed to receive auth session last access update, reason:" + lastAccessUpdateResponseDTO.getAuthSessionLastUpdateResponseError()); // TODO: Change to log error
            // TODO: PErhaps if the error was that the session is expired, broadcast the active session lists?
        }
    }

    private Optional<AuthSessionLastAccessUpdateDTO> getAuthSessionLastAccessUpdate(Message message){
        try {
            var authSessionLastAccessUpdateDTO = objectMapper.readValueFromMessage(message, AuthSessionLastAccessUpdateDTO.class);
            return Optional.of(authSessionLastAccessUpdateDTO);
        }catch (IOException exception){
            log.info("Couldn't receive the last access udpate, reason: " + exception.getLocalizedMessage()); // TODO: Change to log error
        }
        return Optional.empty();
    }
}
