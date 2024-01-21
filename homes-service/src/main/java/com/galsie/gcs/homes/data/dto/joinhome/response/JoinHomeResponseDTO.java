package com.galsie.gcs.homes.data.dto.joinhome.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.joinhome.JoinHomeResponseErrorType;
import com.galsie.gcs.homes.data.dto.common.BasicHomeInfoDTO;
import com.galsie.gcs.homes.data.dto.common.HomeAddressDTO;
import com.galsie.gcs.homes.data.entity.home.HomeEntity;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import lombok.*;
import org.springframework.lang.Nullable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class JoinHomeResponseDTO {

    @JsonProperty("join_home_response_error")
    @Nullable
    private JoinHomeResponseErrorType joinHomeResponseError;

    @JsonProperty("home_info")
    @Nullable
    private BasicHomeInfoDTO homeInfoDTO;


    public static BasicHomeInfoDTO fromEntity(HomeEntity homeEntity) {


        return BasicHomeInfoDTO.builder()
                .homeId(homeEntity.getHomeId())
                .homeName(homeEntity.getName())
                .homeType(homeEntity.getType())
                .homeAddress(HomeAddressDTO.fromHomeAddressEntity(homeEntity))
                .build();

    }

    public static JoinHomeResponseDTO error(JoinHomeResponseErrorType joinHomeResponseErrorType) {
        return new JoinHomeResponseDTO(joinHomeResponseErrorType,null );
    }
    public static JoinHomeResponseDTO success(BasicHomeInfoDTO homeInfo) {
        return new JoinHomeResponseDTO(null,  homeInfo);
    }
    public static GCSResponse<JoinHomeResponseDTO> responseError(JoinHomeResponseErrorType joinHomeResponseErrorType) {
        return GCSResponse.response(error(joinHomeResponseErrorType));
    }

    public static GCSResponse<JoinHomeResponseDTO> responseSuccess(JoinHomeResponseDTO joinHomeResponseDTO) {
        return GCSResponse.response(success(joinHomeResponseDTO.homeInfoDTO));
    }
}
