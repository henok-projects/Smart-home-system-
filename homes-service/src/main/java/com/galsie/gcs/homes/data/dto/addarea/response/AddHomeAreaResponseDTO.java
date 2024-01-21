package com.galsie.gcs.homes.data.dto.addarea.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.HomeResponseErrorType;
import com.galsie.gcs.homes.data.discrete.addarea.AddHomeAreaResponseErrorType;
import com.galsie.gcs.homes.data.dto.common.*;
import com.galsie.gcs.homes.data.entity.home.area.HomeAreaEntity;
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
public class AddHomeAreaResponseDTO {

    @JsonProperty("home_response_error")
    @Nullable
    private HomeResponseErrorType homeResponseError;

    @JsonProperty("add_area_response_error")
    @Nullable
    private AddHomeAreaResponseErrorType addAreaResponseError;

    @JsonProperty("area_info")
    @Nullable
    private AreaBasicInfoDTO areaInfo;


    public static AreaBasicInfoDTO AreaBasicInfoFromEntity(HomeAreaEntity homeAreaEntity) {

        var homeAreaDetails = homeAreaEntity.getAreaDetailsEntity();
        var floorOfArea = homeAreaEntity.getFloorOfArea();
        var areaDetailsDTO = HomeAreaDetailsDTO.builder()
                .initials(homeAreaDetails.getInitials())
                .initialsColor(homeAreaDetails.getInitialsColor())
                .areaName(homeAreaDetails.getName())
                .areaType(String.valueOf(homeAreaDetails.getType())).
                build();


        return AreaBasicInfoDTO.builder()
                .homeId(homeAreaEntity.getHome().getHomeId())
                .areaDetailsDTO(areaDetailsDTO)
                .floorId(floorOfArea != null ? floorOfArea.getUniqueId() : null)
                .areaId(homeAreaEntity.getId())
                .build();
    }

    public static AddHomeAreaResponseDTO error(HomeResponseErrorType homeResponseErrorType, AddHomeAreaResponseErrorType addAreaResponseErrorType) {
        return new AddHomeAreaResponseDTO(homeResponseErrorType, addAreaResponseErrorType, null);
    }

    public static AddHomeAreaResponseDTO success(AreaBasicInfoDTO areaInfo) {
        return new AddHomeAreaResponseDTO(null, null, areaInfo);
    }

    public static GCSResponse<AddHomeAreaResponseDTO> responseError(HomeResponseErrorType homeResponseErrorType, AddHomeAreaResponseErrorType addAreaResponseErrorType) {
        return GCSResponse.response(error(homeResponseErrorType, addAreaResponseErrorType));
    }

    public static GCSResponse<AddHomeAreaResponseDTO> responseSuccess(AreaBasicInfoDTO areaInfo) {
        return GCSResponse.response(success(areaInfo));
    }

}
