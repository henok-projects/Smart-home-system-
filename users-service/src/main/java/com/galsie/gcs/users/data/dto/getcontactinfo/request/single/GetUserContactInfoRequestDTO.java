package com.galsie.gcs.users.data.dto.getcontactinfo.request.single;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.users.data.dto.getcontactinfo.request.common.UserReferenceDTO;
import com.galsie.gcs.users.data.dto.getcontactinfo.request.common.UserReferenceDTODeserializer;
import com.sun.istack.NotNull;
import lombok.*;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
public class GetUserContactInfoRequestDTO {

        @JsonDeserialize(using = UserReferenceDTODeserializer.class)
        @JsonProperty("user_reference")
        @NotNull
        private UserReferenceDTO userReference;


        @JsonProperty("include_profile_photo")
        @NotNull
        private boolean includeProfilePhoto;
}
