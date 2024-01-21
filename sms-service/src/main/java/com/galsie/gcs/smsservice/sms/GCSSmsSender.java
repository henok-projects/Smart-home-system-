package com.galsie.gcs.smsservice.sms;

/**
 * In the future we may have multiple SMS providers with different configurations, this interface unifies the functionality
 * and is implemented by the provider specific implementations found in:
 * {@link GCSTwilioSmsSender}
 *  the run time provider is defined as a bean in {@link com.galsie.gcs.smsservice.configuration.sms.GCSSmsConfiguration}
 */
public interface GCSSmsSender {

    boolean sendSMS(String destinationPhoneNumber, String message);

}
