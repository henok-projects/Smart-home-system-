package com.galsie.gcs.homes.data.dto.invites.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.HomeResponseErrorType;
import com.galsie.gcs.homes.data.discrete.homeinvite.HomeDirectUserSetInviteResponseErrorType;
import com.galsie.gcs.homes.data.discrete.homeinvite.HomeQRUserInviteResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import lombok.*;
import org.springframework.lang.Nullable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@GalDTO
@Builder
public class HomeDirectUserSetInviteResponseDTO {

    @JsonProperty("home_response_error")
    @Nullable
    private HomeResponseErrorType homeResponseError;

    @JsonProperty("invite_user_set_response_error")
    @Nullable
    private HomeQRUserInviteResponseErrorType homeQRUserInviteResponseErrorType;

    @JsonProperty("invite_data")
    @Nullable
    private HomeDirectUserSetInviteDataDTO inviteData;

    public static HomeDirectUserSetInviteResponseDTO error(HomeResponseErrorType homeResponseError, HomeQRUserInviteResponseErrorType homeQRUserInviteResponseErrorType, HomeDirectUserSetInviteResponseErrorType homeDirectUserSetInviteResponseErrorType) {
        return new HomeDirectUserSetInviteResponseDTO(homeResponseError, homeQRUserInviteResponseErrorType, null);
    }
    public static HomeDirectUserSetInviteResponseDTO success(HomeDirectUserSetInviteDataDTO inviteData) {
        return new HomeDirectUserSetInviteResponseDTO(null, null,  inviteData);
    }
    public static GCSResponse<HomeDirectUserSetInviteResponseDTO> responseError(HomeResponseErrorType homeResponseError, HomeQRUserInviteResponseErrorType homeQRUserInviteResponseErrorType, HomeDirectUserSetInviteResponseErrorType homeDirectUserSetInviteResponseErrorType) {
        return GCSResponse.response(error(homeResponseError, homeQRUserInviteResponseErrorType, homeDirectUserSetInviteResponseErrorType));
    }
    public static GCSResponse<HomeDirectUserSetInviteResponseDTO> responseSuccess(HomeDirectUserSetInviteDataDTO homeDirectUserSetInviteDataDTO) {
        return GCSResponse.response(success(homeDirectUserSetInviteDataDTO));
    }

}
