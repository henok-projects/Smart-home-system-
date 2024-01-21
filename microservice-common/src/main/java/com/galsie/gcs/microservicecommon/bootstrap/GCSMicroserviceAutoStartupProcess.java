package com.galsie.gcs.microservicecommon.bootstrap;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GCSMicroserviceAutoStartupProcess implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    GCSMicroserviceStartupLoginService gcsMicroserviceStartupLoginService;


    @Override
    @SneakyThrows
    public void onApplicationEvent(ApplicationReadyEvent event) {
        gcsMicroserviceStartupLoginService.login();
    }

}
