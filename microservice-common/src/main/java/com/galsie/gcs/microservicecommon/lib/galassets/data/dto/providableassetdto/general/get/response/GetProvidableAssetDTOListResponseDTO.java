package com.galsie.gcs.microservicecommon.lib.galassets.data.dto.providableassetdto.general.get.response;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.galsie.gcs.microservicecommon.lib.galassets.data.discrete.ProvidableAssetResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.AbstractProvidedAssetDTO;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.deserialize.providableassetdto.general.get.DeserializerGetProvidableAssetDTOListResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import lombok.*;
import org.springframework.lang.Nullable;

import java.util.Map;

@GalDTO
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonDeserialize(using = DeserializerGetProvidableAssetDTOListResponse.class)
public class GetProvidableAssetDTOListResponseDTO{

    @Nullable
    ProvidableAssetResponseErrorType providableAssetResponseError;

    /**
     *  returns for each index in the requested list, the provided asset dto. Indices which don't have a provided asset
     *  dto won't exist in the map
     */
    @Nullable
    Map<Integer, AbstractProvidedAssetDTO> providedAssetDTOs;

    private static GetProvidableAssetDTOListResponseDTO success(Map<Integer, AbstractProvidedAssetDTO> providedAssetDTOs){
        return new GetProvidableAssetDTOListResponseDTO(null, providedAssetDTOs);
    }

    private static GetProvidableAssetDTOListResponseDTO error(ProvidableAssetResponseErrorType providableAssetResponseError){
        return new GetProvidableAssetDTOListResponseDTO(providableAssetResponseError, null);
    }

    public static GCSResponse<GetProvidableAssetDTOListResponseDTO> responseSuccess(Map<Integer, AbstractProvidedAssetDTO> providedAssetDTOs){
        return GCSResponse.response(success(providedAssetDTOs));
    }

    public static GCSResponse<GetProvidableAssetDTOListResponseDTO> responseError(ProvidableAssetResponseErrorType providableAssetResponseErrorType){
        return GCSResponse.response(error(providableAssetResponseErrorType));
    }

}
