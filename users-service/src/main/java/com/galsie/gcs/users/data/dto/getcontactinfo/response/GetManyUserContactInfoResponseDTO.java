package com.galsie.gcs.users.data.dto.getcontactinfo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import lombok.*;
import org.springframework.lang.Nullable;

import java.util.List;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
@Builder
public class GetManyUserContactInfoResponseDTO {

    @JsonProperty("user_contact_info_responses")
    @Nullable
    private List<GetUserContactInfoResponseDTO> userContactInfoResponses;



    public static GetManyUserContactInfoResponseDTO success(List<GetUserContactInfoResponseDTO> userContactInfoResponses) {
        return new GetManyUserContactInfoResponseDTO(userContactInfoResponses);
    }

    public static GCSResponse<GetManyUserContactInfoResponseDTO> responseSuccess(List<GetUserContactInfoResponseDTO> userContactInfoResponses) {
        return GCSResponse.response(success(userContactInfoResponses));
    }
}
