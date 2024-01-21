package com.galsie.gcs.users.listeners.gcsevents;

import com.galsie.gcs.microservicecommon.lib.gcsevents.GCSGlobalEventListener;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitMQCommonQueueType;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitTemplate;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.GCSEventListener;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.OnGCSEvent;
import com.galsie.gcs.users.events.UserAuthSessionEndedGCSEvent;
import com.galsie.gcs.users.events.UserAuthSessionStartedGCSEvent;
import com.galsie.gcs.users.service.authentication.LocalUserAuthenticatorService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

@GCSGlobalEventListener
public class UserAuthSessionEventListener implements GCSEventListener {

    static long SEND_SESSIONS_ACCESSED_NO_LONGER_BEFORE_HOURS = 48; // TODO: load from configuration

    @Autowired
    GCSRabbitTemplate rabbitTemplate;

    @Autowired
    LocalUserAuthenticatorService authenticationService;

    @OnGCSEvent
    public void onUserAuthSessionStartedEvent(UserAuthSessionStartedGCSEvent event) {
        long userId = event.getGalsieAuthLoginSessionToken().getUserId();
        sendAuthSessionListFor(userId);
    }

    @OnGCSEvent
    public void onUserAuthSessionEndedEvent(UserAuthSessionEndedGCSEvent event) {
        long userId = event.getGalsieAuthLoginSessionToken().getUserId();
        sendAuthSessionListFor(userId);
    }

    private void sendAuthSessionListFor(Long userId){
        var accessedAfterTime = LocalDateTime.now().minusHours(SEND_SESSIONS_ACCESSED_NO_LONGER_BEFORE_HOURS);
        var dto = authenticationService.gcsInternalGetUserAccountActiveSessionsDTOFor(userId, Optional.of(accessedAfterTime));
        rabbitTemplate.convertAndSend(GCSRabbitMQCommonQueueType.USER_AUTH_INFO, dto);
    }
}
