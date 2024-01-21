package com.galsie.gcs.homes.data.dto.homeuseraccess.editaccess.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.homeuseraccess.editaccess.EditSingleHomeUserAccessResponseErrorType;
import com.galsie.gcs.homes.data.dto.common.UserHomeAccessInfoDTO;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.lang.Nullable;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EditSingleHomeUserAccessResponseDTO {

    @JsonProperty("edit_single_home_user_access_response_error")
    @Nullable
    private EditSingleHomeUserAccessResponseErrorType editSingleHomeUserAccessResponseErrorType;

    @JsonProperty("home_user_id")
    @NotNull
    private Long homeUserId;

    @JsonProperty("access_info")
    @Nullable
    private UserHomeAccessInfoDTO accessInfo;


    public static EditSingleHomeUserAccessResponseDTO error(EditSingleHomeUserAccessResponseErrorType editSingleHomeUserAccessResponseErrorType) {
        return new EditSingleHomeUserAccessResponseDTO(editSingleHomeUserAccessResponseErrorType, null, null);
    }

    public static EditSingleHomeUserAccessResponseDTO success(Long homeUserId, UserHomeAccessInfoDTO accessInfo) {
        return new EditSingleHomeUserAccessResponseDTO(null,homeUserId, accessInfo);
    }


    public static GCSResponse<EditSingleHomeUserAccessResponseDTO> responseError(EditSingleHomeUserAccessResponseErrorType editSingleHomeUserAccessResponseErrorType) {
        return GCSResponse.response(error(editSingleHomeUserAccessResponseErrorType));
    }


    public static GCSResponse<EditSingleHomeUserAccessResponseDTO> responseSuccess(EditSingleHomeUserAccessResponseDTO editSingleHomeUserAccessResponseDTO) {
        return GCSResponse.response(success(editSingleHomeUserAccessResponseDTO.getHomeUserId(),editSingleHomeUserAccessResponseDTO.getAccessInfo()));
    }

}
