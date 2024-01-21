package com.galsie.gcs.users.data.dto.getcontactinfo.request.many;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.users.data.dto.getcontactinfo.request.common.UserReferenceDTO;
import com.galsie.gcs.users.data.dto.getcontactinfo.request.common.UserReferenceDTODeserializer;
import com.sun.istack.NotNull;
import lombok.*;

import java.util.List;
@GalDTO
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
@Builder
@JsonDeserialize(using = GetManyUserContactInfoRequestDTODeserializer.class)
public class GetManyUserContactInfoRequestDTO {

    @JsonDeserialize(using = UserReferenceDTODeserializer.class)
    @JsonProperty("for_users")
    @NotNull
    private List<UserReferenceDTO> forUsers;


    @JsonProperty("include_profile_photos")
    @NotNull
    private boolean includeProfilePhotos;
}
