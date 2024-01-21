package com.galsie.gcs.homes.data.dto.invites.response.qr;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.homeinvite.HomeQRUserInviteResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import lombok.*;
import org.springframework.lang.Nullable;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HomeQRUserInviteResponseDTO {

    @JsonProperty("invite_qr_response_error")
    @Nullable
    private HomeQRUserInviteResponseErrorType inviteQRResponseErrorType;

    @JsonProperty("invite_data")
    @Nullable
    private HomeQRUserInviteDataDTO inviteData;


    public static HomeQRUserInviteResponseDTO error(HomeQRUserInviteResponseErrorType inviteQRResponseErrorType) {
        return new HomeQRUserInviteResponseDTO(inviteQRResponseErrorType, null);
    }
    public static HomeQRUserInviteResponseDTO success(HomeQRUserInviteDataDTO qrCodeHomeInviteDataDTO) {
        return new HomeQRUserInviteResponseDTO(null,  qrCodeHomeInviteDataDTO);
    }
    public static GCSResponse<HomeQRUserInviteResponseDTO> responseError(HomeQRUserInviteResponseErrorType inviteQRResponseErrorType) {
        return GCSResponse.response(error(inviteQRResponseErrorType));
    }
    public static GCSResponse<HomeQRUserInviteResponseDTO> responseSuccess(HomeQRUserInviteResponseDTO inviteQRResponseDataDTO) {
        return GCSResponse.response(success(inviteQRResponseDataDTO.getInviteData()));
    }
}
