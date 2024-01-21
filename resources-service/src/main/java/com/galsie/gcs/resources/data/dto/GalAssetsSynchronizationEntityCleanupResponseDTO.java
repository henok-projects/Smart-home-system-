package com.galsie.gcs.resources.data.dto;

import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.Nullable;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@GalDTO
public class GalAssetsSynchronizationEntityCleanupResponseDTO {

    @Nullable
    private String errorMessage;

    @Nullable
    private Integer noOfDeletedFiles;

    public boolean hasError(){
        return errorMessage != null;
    }

    private static GalAssetsSynchronizationEntityCleanupResponseDTO success(Integer noOfDeletedFiles){
        return GalAssetsSynchronizationEntityCleanupResponseDTO.builder().noOfDeletedFiles(noOfDeletedFiles).build();
    }

    private static GalAssetsSynchronizationEntityCleanupResponseDTO error(String errorMessage){
        return GalAssetsSynchronizationEntityCleanupResponseDTO.builder().errorMessage(errorMessage).build();
    }

    public static GCSResponse<GalAssetsSynchronizationEntityCleanupResponseDTO> responseSuccess(Integer noOfDeletedFiles){
        return GCSResponse.response(success(noOfDeletedFiles));
    }

    public static GCSResponse<GalAssetsSynchronizationEntityCleanupResponseDTO> responseError(String errorMessage){
        return GCSResponse.response(error(errorMessage));
    }

}
