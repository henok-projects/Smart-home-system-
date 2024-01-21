package com.galsie.gcs.resources.data.dto;

import com.galsie.lib.datamodel.galsie.GalModel;
import com.galsie.lib.datamodel.matter.MTRModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@GalDTO
public class DataModelAssetProviderDTO {

    private GalModel galModel;

    private MTRModel mtrModel;

    private AssetGroupLastUpdateInfoDTO lastUpdateInfo;

}
