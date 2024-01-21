package com.galsie.gcs.microservicecommon.lib.galassets.data.dto.providableassetfile.get.response;

import com.galsie.gcs.microservicecommon.lib.galassets.data.discrete.ProvidableAssetResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import lombok.*;
import org.springframework.lang.Nullable;


@GalDTO
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProvidableLoadedAssetStringResponseDTO {
    @Nullable
    private ProvidableAssetResponseErrorType providableAssetResponseError;

    @Nullable
    private String base64EncodedString;


    public static  ProvidableLoadedAssetStringResponseDTO success(String encodedString){
        return  ProvidableLoadedAssetStringResponseDTO.builder().base64EncodedString(encodedString).build();
    }

    public static ProvidableLoadedAssetStringResponseDTO error(ProvidableAssetResponseErrorType responseError){
        return ProvidableLoadedAssetStringResponseDTO.builder().providableAssetResponseError(responseError).build();
    }

    public static GCSResponse<ProvidableLoadedAssetStringResponseDTO> responseSuccess(String encodedString){
        return GCSResponse.response(success(encodedString));
    }

    public static GCSResponse<ProvidableLoadedAssetStringResponseDTO> responseError(ProvidableAssetResponseErrorType responseError){
        return GCSResponse.response(error(responseError));
    }
}
