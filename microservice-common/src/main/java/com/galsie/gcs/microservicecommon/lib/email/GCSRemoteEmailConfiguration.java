package com.galsie.gcs.microservicecommon.lib.email;

import org.springframework.context.annotation.Bean;

public abstract class GCSRemoteEmailConfiguration {

    @Bean
    public GCSRemoteEmailSender gcsRemoteEmailSender(){
        return new GCSRemoteEmailSender();
    }
}
