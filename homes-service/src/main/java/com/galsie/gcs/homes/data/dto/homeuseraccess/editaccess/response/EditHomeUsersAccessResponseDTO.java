package com.galsie.gcs.homes.data.dto.homeuseraccess.editaccess.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.HomeResponseErrorType;
import com.galsie.gcs.homes.data.discrete.homeuseraccess.editaccess.EditHomeUsersAccessResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import lombok.*;
import org.springframework.lang.Nullable;

import java.util.List;


@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EditHomeUsersAccessResponseDTO {

    @JsonProperty("home_response_error")
    @Nullable
    private HomeResponseErrorType homeResponseError;

    @JsonProperty("edit_home_users_access_response_error")
    @Nullable
    private EditHomeUsersAccessResponseErrorType editHomeUsersAccessResponseErrorType;

    @JsonProperty("edit_home_user_access_responses")
    @Nullable
    private List<EditSingleHomeUserAccessResponseDTO> editSingleHomeUserAccessResponseDTOList;


    public static EditHomeUsersAccessResponseDTO error(HomeResponseErrorType homeResponseError, EditHomeUsersAccessResponseErrorType editHomeUsersAccessResponseErrorType) {
        return new EditHomeUsersAccessResponseDTO(homeResponseError, editHomeUsersAccessResponseErrorType, null);
    }

    public static EditHomeUsersAccessResponseDTO success(List<EditSingleHomeUserAccessResponseDTO> editSingleHomeUserAccessResponseDTOList) {
        return new EditHomeUsersAccessResponseDTO(null, null, editSingleHomeUserAccessResponseDTOList);
    }

    public static GCSResponse<EditHomeUsersAccessResponseDTO> responseError(HomeResponseErrorType homeResponseError, EditHomeUsersAccessResponseErrorType editHomeUsersAccessResponseErrorType) {
        return GCSResponse.response(error(homeResponseError, editHomeUsersAccessResponseErrorType));
    }

    public static GCSResponse<EditHomeUsersAccessResponseDTO> responseSuccess(List<EditSingleHomeUserAccessResponseDTO> editSingleHomeUserAccessResponseDTOList) {
        return GCSResponse.response(success(editSingleHomeUserAccessResponseDTOList));
    }
}
