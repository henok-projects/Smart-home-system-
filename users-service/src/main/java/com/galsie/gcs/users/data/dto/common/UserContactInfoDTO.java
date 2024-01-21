package com.galsie.gcs.users.data.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class UserContactInfoDTO {

        @JsonProperty("user_id")
        @NotNull
        private Long userId;

        @JsonProperty("full_name")
        @Nullable
        private String fullName;

        @JsonProperty("username")
        @NotNull
        private String username;

        @JsonProperty("email")
        @Nullable
        private String email;

        @JsonProperty("phone")
        @Nullable
        private PhoneNumberDTO phone;

        @JsonProperty("profile_photo")
        @Nullable
        private String profilePhoto; // Base64 encoded image

}
