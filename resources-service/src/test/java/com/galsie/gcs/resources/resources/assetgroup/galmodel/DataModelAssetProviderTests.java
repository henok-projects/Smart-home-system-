package com.galsie.gcs.resources.resources.assetgroup.galmodel;

import com.galsie.gcs.microservicecommon.lib.galassets.core.AssetGroupType;
import com.galsie.gcs.resources.resources.assetgroup.loaded.LoadedAssetGroup;
import com.galsie.gcs.resources.resources.assetprovider.datamodel.DataModelAssetProvider;
import com.galsie.lib.datamodel.common.OptionalRequirementType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class DataModelAssetProviderTests {
    private static DataModelAssetProvider assetProvider;
    @BeforeAll
    public static void initAssetProvider() throws Exception{
        assetProvider = DataModelAssetProvider.newInstance(LoadedAssetGroup.sharedInstance(AssetGroupType.DATA_MODEL));

    }

    @Test
    public void mtrModelLoaded(){
        var mtrModel = assetProvider.getMtrModel();

        assert mtrModel.getClusterControlTypes().size() == 3;
        var clusterCount = mtrModel.getClusters().size();
        assert  clusterCount == 71;

        //var testCluster = mtrModel.getClusters().stream().filter((cluster) -> cluster.getDefinition().equals("MTR_TEST")).findFirst().orElseThrow();


        var allClustersDevice = mtrModel.getMtrDeviceTypes().stream().filter((mtrDeviceType) -> mtrDeviceType.getDefinition().equals("MTR_ALL_CLUSTERS_APP_SERVER_EXAMPLE")).findFirst().orElseThrow();
        assert allClustersDevice.getClusters(OptionalRequirementType.REQUIRED).size() == 8;
        assert allClustersDevice.getClusters(OptionalRequirementType.OPTIONAL).size() == 3;
    }

    @Test
    public void galModelLoaded(){
        var galModel = assetProvider.getGalModel();
        assert galModel.getCategoryTypesList().getCategoryTypes().size() == 9;
        assert galModel.getDiverseGroups().size() == 2;
        assert galModel.getDeviceTypes().getNormalDeviceTypes().size() > 2;
        assert galModel.getDeviceTypes().getAbstractDeviceTypes().size() > 1;
    }
}
