package com.galsie.gcs.microservicecommon.lib.galassets.providableentities.countries.response;

import com.galsie.gcs.microservicecommon.lib.galassets.data.discrete.providableentities.ProvidableEntityResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.galassets.providableentities.countries.CountryEntityDetailedInfoDTO;
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
public class GetCountryResponseDTO {
    @Nullable
    private ProvidableEntityResponseErrorType providableEntityError;

    @Nullable
    private CountryEntityDetailedInfoDTO country;

    private static GetCountryResponseDTO success(CountryEntityDetailedInfoDTO country){
        return new GetCountryResponseDTO(null, country);
    }

    private static GetCountryResponseDTO error(ProvidableEntityResponseErrorType providableEntityError){
        return new GetCountryResponseDTO(providableEntityError, null);
    }

    public static GCSResponse<GetCountryResponseDTO> responseSuccess(CountryEntityDetailedInfoDTO country){
        return GCSResponse.response(success(country));
    }

    public static GCSResponse<GetCountryResponseDTO> responseError(ProvidableEntityResponseErrorType providableEntityError){
        return GCSResponse.response(error(providableEntityError));
    }
}
