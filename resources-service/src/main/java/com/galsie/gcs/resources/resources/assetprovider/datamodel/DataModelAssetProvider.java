package com.galsie.gcs.resources.resources.assetprovider.datamodel;


import com.galsie.gcs.resources.resources.assetgroup.loaded.LoadedAssetGroup;
import com.galsie.gcs.resources.resources.assetprovider.AssetProvider;
import com.galsie.gcs.resources.resources.assetprovider.datamodel.galsie.GalModelFactory;
import com.galsie.gcs.resources.resources.assetprovider.datamodel.matter.MTRModelFactory;
import com.galsie.lib.datamodel.galsie.GalModel;
import com.galsie.lib.datamodel.matter.MTRModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DataModelAssetProvider implements AssetProvider {

    private MTRModel mtrModel;

    private GalModel galModel;

    private LoadedAssetGroup loadedAssetGroup;

    public static DataModelAssetProvider newInstance(LoadedAssetGroup loadedAssetGroup) throws Exception{
        var mtrModel = MTRModelFactory.newInstance(loadedAssetGroup);
        var galModel = GalModelFactory.newInstance(loadedAssetGroup, mtrModel);
        return new DataModelAssetProvider(mtrModel, galModel, loadedAssetGroup);
    }
}
