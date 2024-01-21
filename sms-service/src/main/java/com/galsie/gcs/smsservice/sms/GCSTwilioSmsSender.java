package com.galsie.gcs.smsservice.sms;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class GCSTwilioSmsSender implements GCSSmsSender{

    private final List<String> phoneNumbers;

    public GCSTwilioSmsSender(String sid, String token, List<String> phoneNumbers) {
        Twilio.init(sid, token);
        this.phoneNumbers = phoneNumbers;
    }
    @Override
    public boolean sendSMS(String destinationPhoneNumber, String message) {
        for(int i =0 ; i < phoneNumbers.size() ; i++){
            try {
                log.info("Sending sms from: " + phoneNumbers.get(i)+ " to: " + destinationPhoneNumber);
                Message.creator(new PhoneNumber(destinationPhoneNumber), new PhoneNumber(phoneNumbers.get(i)), message).create();
                return true;
            } catch (ApiException e) {
                log.warn("Failed to send sms to " + destinationPhoneNumber + " reason: " + e.getLocalizedMessage() + (i <phoneNumbers.size() -1 ? " Retrying..." : ""));
            }
        }
        return false;
    }

}
