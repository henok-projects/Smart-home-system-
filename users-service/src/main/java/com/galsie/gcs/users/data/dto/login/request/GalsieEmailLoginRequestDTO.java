package com.galsie.gcs.users.data.dto.login.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GalsieEmailLoginRequestDTO extends GalsieLoginRequestDTO {
    String email;

    public GalsieEmailLoginRequestDTO(String hashedPwd, String email) {
        super(hashedPwd);
        this.email = email;
    }
}