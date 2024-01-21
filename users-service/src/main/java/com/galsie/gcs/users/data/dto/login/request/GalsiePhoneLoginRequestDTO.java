package com.galsie.gcs.users.data.dto.login.request;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GalsiePhoneLoginRequestDTO extends GalsieLoginRequestDTO {
    short countryCode;
    String  phoneNumber;
}
