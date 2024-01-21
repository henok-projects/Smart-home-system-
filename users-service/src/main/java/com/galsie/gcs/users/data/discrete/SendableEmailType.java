package com.galsie.gcs.users.data.discrete;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SendableEmailType {
    USER_EMAIL_VERIFICATION(0);

    int id;
}
