package com.galsie.gcs.users.data.dto.login.request;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@GalDTO
public abstract class GalsieLoginRequestDTO {
    String hashedPwd;
}









