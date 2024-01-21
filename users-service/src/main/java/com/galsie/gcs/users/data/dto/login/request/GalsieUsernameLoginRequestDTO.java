package com.galsie.gcs.users.data.dto.login.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GalsieUsernameLoginRequestDTO extends GalsieLoginRequestDTO {
    String username;
}