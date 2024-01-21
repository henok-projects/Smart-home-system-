package com.galsie.gcs.microservicecommon.lib.galassets.providableentities.countries.response;

import com.galsie.gcs.microservicecommon.lib.galassets.data.discrete.providableentities.ProvidableEntityResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.galassets.providableentities.countries.ZoneEntityDetailedInfoDTO;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetZoneResponseDTO {
    @Nullable
    private ProvidableEntityResponseErrorType providableEntityError;

    @Nullable
    private ZoneEntityDetailedInfoDTO zone;

    private static GetZoneResponseDTO success(ZoneEntityDetailedInfoDTO state){
        return new GetZoneResponseDTO(null, state);
    }

    private static GetZoneResponseDTO error(ProvidableEntityResponseErrorType providableEntityError){
        return new GetZoneResponseDTO(providableEntityError, null);
    }

    public static GCSResponse<GetZoneResponseDTO> responseSuccess(ZoneEntityDetailedInfoDTO state){
        return GCSResponse.response(success(state));
    }

    public static GCSResponse<GetZoneResponseDTO> responseError(ProvidableEntityResponseErrorType providableEntityError){
        return GCSResponse.response(error(providableEntityError));
    }
}
