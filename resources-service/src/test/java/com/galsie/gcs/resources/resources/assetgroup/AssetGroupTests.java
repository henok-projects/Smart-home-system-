package com.galsie.gcs.resources.resources.assetgroup;

import com.galsie.gcs.microservicecommon.lib.galassets.core.AssetGroupType;
import com.galsie.gcs.resources.resources.assetgroup.loaded.LoadedAssetGroup;
import com.galsie.lib.utils.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AssetGroupTests {


    @Test
    public void galModelAssetGroupLoaded() throws Exception{
        var assetGroup = LoadedAssetGroup.sharedInstance(AssetGroupType.DATA_MODEL);
        assert assetGroup.getPaths().length > 0;
        var fileData = new HashMap<String, String[]>();
        fileData.put("features", new String[]{"attribute_structures.xml", "feature_types.xml", "units.xml"});
        fileData.put("mtr", new String[]{"mtr_cluster_control_types.xml", "mtr_clusters.xml", "mtr_devices.xml"});
        fileData.put("", new String[]{"categories.xml", "device_types.xml", "diverse_group_types.xml"});
        /*
        TODO: Test for render info being loaded
         */
        for (String path: fileData.keySet()){
            var files = fileData.get(path);
            for (String file: files) {
                var filePath = StringUtils.joinPaths(path, file);
                assert assetGroup.getLoadedFile(filePath).isPresent() : "The file '" + filePath + "' was not present" ;
            }
        }
    }

    @Test
    public void langsAssetGroupLoaded() throws Exception{
        var ymlAsset = LoadedAssetGroup.sharedInstance(AssetGroupType.LANG);
        assert ymlAsset.getPaths().length > 0;
        String[] dirs = {"ar", "en"};
        /*
        whether we add the extension or not, the file will be found - because LoadedAssetGroup, like Galsie, is smart. hahaha
         */
        String[] files = {"countries_translations.yml", "app_main_translations.yml", "feature_data_rep", "formatting.yml", "lang_config.yml"};
        for (String dir: dirs) {
            for (String file: files) {
                String path = StringUtils.joinPaths( dir, file);
                assert ymlAsset.getLoadedFile(path).isPresent();
            }
        }
    }

}
