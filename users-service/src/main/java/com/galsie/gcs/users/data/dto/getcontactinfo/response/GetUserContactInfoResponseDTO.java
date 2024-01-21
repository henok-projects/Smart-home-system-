package com.galsie.gcs.users.data.dto.getcontactinfo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.gcs.users.data.discrete.getcontactinfo.GetContactInfoResponseErrorType;
import com.galsie.gcs.users.data.dto.common.UserContactInfoDTO;
import lombok.*;
import org.springframework.lang.Nullable;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@Setter
@Builder
public class GetUserContactInfoResponseDTO {

        @JsonProperty("get_contact_info_response_error")
        @Nullable
        private GetContactInfoResponseErrorType getContactInfoResponseError;

        @JsonProperty("user_contact_info")
        @Nullable
        private UserContactInfoDTO userContactInfo;

        public static GetUserContactInfoResponseDTO error(GetContactInfoResponseErrorType getContactInfoResponseError) {
                return new GetUserContactInfoResponseDTO(getContactInfoResponseError, null);
        }

        public static GetUserContactInfoResponseDTO success(UserContactInfoDTO userContactInfo) {
                return new GetUserContactInfoResponseDTO(null, userContactInfo);
        }

        public static GCSResponse<GetUserContactInfoResponseDTO> responseError(GetContactInfoResponseErrorType getContactInfoResponseError) {
                return GCSResponse.response(error(getContactInfoResponseError));
        }

        public static GCSResponse<GetUserContactInfoResponseDTO> responseSuccess(UserContactInfoDTO userContactInfo) {
                return GCSResponse.response(success(userContactInfo));
        }
}
