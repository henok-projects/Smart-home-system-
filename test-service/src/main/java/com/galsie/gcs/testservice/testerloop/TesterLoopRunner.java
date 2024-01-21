package com.galsie.gcs.testservice.testerloop;

import com.galsie.gcs.microservicecommon.lib.email.GCSRemoteEmailSender;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.GCSEventManager;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TesterLoopRunner {
    @Autowired
    GCSEventManager gcsGlobalEventManager;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    GCSRemoteEmailSender gcsRemoteEmailSender;

    private Thread t;

    @PostConstruct
    public void onInit(){
       start();
    }
    void start(){
        t = new Thread(new TesterLoop(gcsGlobalEventManager, rabbitTemplate, gcsRemoteEmailSender));
        this.t.start();
    }
}
