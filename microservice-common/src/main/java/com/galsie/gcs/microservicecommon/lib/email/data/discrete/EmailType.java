package com.galsie.gcs.microservicecommon.lib.email.data.discrete;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EmailType {

    USER_EMAIL_VERIFICATION(0, "Galsie", "Verify your account"),
    EMAIL_BASED_USER_JOINED_NOTIFICATION(1, "Galsie", "New user joined"),
    EMAIL_BASED_INVITATION_NOTIFICATION(2, "Galsie", "you are invited");


    private int id;
    String fromName;
    String subject;
}