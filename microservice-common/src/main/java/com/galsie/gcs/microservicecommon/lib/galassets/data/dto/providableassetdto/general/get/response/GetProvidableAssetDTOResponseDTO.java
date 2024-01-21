package com.galsie.gcs.microservicecommon.lib.galassets.data.dto.providableassetdto.general.get.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.galsie.gcs.microservicecommon.lib.deserialize.providableassetdto.general.get.DeserializerGetProvidableAssetDTOResponse;
import com.galsie.gcs.microservicecommon.lib.galassets.data.discrete.ProvidableAssetResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.AbstractProvidedAssetDTO;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

@GalDTO
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonDeserialize(using = DeserializerGetProvidableAssetDTOResponse.class)
public class GetProvidableAssetDTOResponseDTO<T extends AbstractProvidedAssetDTO> {

    @Nullable
    ProvidableAssetResponseErrorType providableAssetResponseError;

    @Nullable
    T providedAssetDTO;


    public static GetProvidableAssetDTOResponseDTO success(AbstractProvidedAssetDTO providedAssetDTO){
        return new GetProvidableAssetDTOResponseDTO(null, providedAssetDTO);
    }

    public static GetProvidableAssetDTOResponseDTO error(ProvidableAssetResponseErrorType providableAssetResponseError){
        return new GetProvidableAssetDTOResponseDTO(providableAssetResponseError, null);
    }

    public static GCSResponse<GetProvidableAssetDTOResponseDTO> responseSuccess(AbstractProvidedAssetDTO providedAssetDTO){
        return GCSResponse.response( success(providedAssetDTO));
    }

    public static GCSResponse<GetProvidableAssetDTOResponseDTO> responseError(ProvidableAssetResponseErrorType providableAssetResponseErrorType){
        return GCSResponse.response(error(providableAssetResponseErrorType));
    }
}