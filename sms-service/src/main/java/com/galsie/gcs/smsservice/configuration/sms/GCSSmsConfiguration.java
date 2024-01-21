package com.galsie.gcs.smsservice.configuration.sms;

import com.galsie.gcs.smsservice.sms.GCSSmsSender;
import com.galsie.gcs.smsservice.sms.GCSTwilioSmsSender;
import com.galsie.gcs.smsservice.sms.SMSTxtModelManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class GCSSmsConfiguration {

    @Value("${galsie.sms.twilio.sid}") String sid;

    @Value("${galsie.sms.twilio.token}") String token;

    @Value("#{'${galsie.sms.phone-numbers}'.split(',\\s*')}")
    private List<String> phoneNumbers;

    @Bean
    public GCSSmsSender gcsSmsSender() {
        return new GCSTwilioSmsSender(sid, token, phoneNumbers);
    }

    @Bean
    public SMSTxtModelManager txtSmsModelManager(){ return new SMSTxtModelManager();}

}
