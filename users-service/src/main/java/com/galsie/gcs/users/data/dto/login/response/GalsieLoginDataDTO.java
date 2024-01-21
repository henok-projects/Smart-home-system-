package com.galsie.gcs.users.data.dto.login.response;


import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.lang.Nullable;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
@Builder
public class GalsieLoginDataDTO {
    @NotNull
    String username;

    @NotNull
    String authToken;

    @Nullable
    String twoFAVerificationToken;
}
