package com.galsie.gcs.microservicecommon.lib.galassets.providableentities.countries.response;

import com.galsie.gcs.microservicecommon.lib.galassets.data.discrete.providableentities.ProvidableEntityResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.galassets.providableentities.countries.CountriesProvidableEntityDTO;
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
public class GetCountriesListResponseDTO {

    @Nullable
    private ProvidableEntityResponseErrorType providableEntityError;

    @Nullable
    private CountriesProvidableEntityDTO countriesEntityDTO;

    private static GetCountriesListResponseDTO success(CountriesProvidableEntityDTO countries){
        return new GetCountriesListResponseDTO(null, countries);
    }

    private static GetCountriesListResponseDTO error(ProvidableEntityResponseErrorType providableEntityError){
        return new GetCountriesListResponseDTO(providableEntityError, null);
    }

    public static GCSResponse<GetCountriesListResponseDTO> responseSuccess(CountriesProvidableEntityDTO countries){
        return GCSResponse.response(success(countries));
    }

    public static GCSResponse<GetCountriesListResponseDTO> responseError(ProvidableEntityResponseErrorType providableEntityError){
        return GCSResponse.response(error(providableEntityError));
    }

}
