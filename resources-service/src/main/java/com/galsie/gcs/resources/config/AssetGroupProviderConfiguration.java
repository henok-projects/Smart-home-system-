package com.galsie.gcs.resources.config;

import com.galsie.gcs.microservicecommon.lib.galassets.core.AssetGroupType;
import com.galsie.gcs.resources.resources.assetgroup.loaded.LoadedAssetGroup;
import com.galsie.gcs.resources.resources.assetprovider.datamodel.DataModelAssetProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class AssetGroupProviderConfiguration {

    @Bean
    @DependsOn({"bootstrapBean"})
    public DataModelAssetProvider getDataModelProvider() throws Exception {
        return DataModelAssetProvider.newInstance(LoadedAssetGroup.sharedInstance(AssetGroupType.DATA_MODEL));
    }

}
