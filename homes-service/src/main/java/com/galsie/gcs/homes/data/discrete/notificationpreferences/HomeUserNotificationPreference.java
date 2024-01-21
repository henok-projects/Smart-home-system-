package com.galsie.gcs.homes.data.discrete.notificationpreferences;

import lombok.AllArgsConstructor;
import lombok.Getter;


//	these preferences are stored in the HomeUserNotificationPreferenceEntity, and in which service this is handled.
@AllArgsConstructor
@Getter
public enum HomeUserNotificationPreference {
    SHOULD_SEND_USER_JOINED_EMAIL("should_send_user_joined_email", Boolean.class,true),
    MAX_NUMER_OF_EMAILS_PER_DAY("max_number_of_emails_per_day", Integer.class, true),
    SHOULD_SEND_USER_JOINED_SMS("should_send_user_joined_sms",Boolean.class, 100);
    // ... add more preferences here

    private final String key;
    private final Class<?> valueType;
    private final Object defaultValue;

    public static HomeUserNotificationPreference valueOfKey(String key) {
        for (HomeUserNotificationPreference preference : values()) {
            if (preference.getKey().equals(key)) {
                return preference;
            }
        }
        return null;
    }

    }

