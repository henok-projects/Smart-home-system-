package com.galsie.gcs.microservicecommon.lib.sms;

import org.springframework.context.annotation.Bean;

public abstract class GCSRemoteSMSConfiguration {

    @Bean
    public GCSRemoteSMSSender gcsRemoteSmsSender(){ return new GCSRemoteSMSSender(); }
}
