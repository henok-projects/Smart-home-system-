package com.galsie.gcs.resources.resources.assetprovider.datamodel.galsie;


import com.galsie.gcs.microservicecommon.lib.galassets.core.loaded.asset.LoadedXmlAssetFile;
import com.galsie.gcs.resources.resources.assetgroup.loaded.LoadedAssetGroup;
import com.galsie.gcs.resources.resources.assetprovider.datamodel.galsie.types.CategoryTypeFactory;
import com.galsie.gcs.resources.resources.assetprovider.datamodel.galsie.types.DiverseGroupTypeFactory;
import com.galsie.gcs.resources.resources.assetprovider.datamodel.galsie.types.device.BaseDeviceTypeFactory;
import com.galsie.lib.datamodel.galsie.CategoryTypesList;
import com.galsie.lib.datamodel.galsie.DeviceTypesList;
import com.galsie.lib.datamodel.galsie.GalModel;
import com.galsie.lib.datamodel.galsie.types.CategoryType;
import com.galsie.lib.datamodel.galsie.types.DiverseGroupType;
import com.galsie.lib.datamodel.galsie.types.device.AbstractDeviceType;
import com.galsie.lib.datamodel.galsie.types.device.BaseDeviceType;
import com.galsie.lib.datamodel.galsie.types.device.DeviceType;
import com.galsie.lib.datamodel.matter.MTRModel;
import com.galsie.lib.datamodel.matter.types.MTRDeviceType;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class GalModelFactory {


    public static GalModel newInstance(LoadedAssetGroup assetGroup, MTRModel mtrModel) throws Exception {
        log.info("Loading the Galsie Model (Categories, Device Types, Clusters, Diverse group types)");
        var categories = loadCategories(assetGroup);
        var devices = loadDeviceTypes(assetGroup, categories.getCategoryTypes(), mtrModel.getMtrDeviceTypes());
        var diverseGroups = loadDiverseGroups(assetGroup, devices.getNormalDeviceTypes());
        log.info("Galsie model LOADED");
        return new GalModel(categories, diverseGroups, devices);
    }

    private static CategoryTypesList loadCategories(LoadedAssetGroup assetGroup) throws Exception {
        var file = (LoadedXmlAssetFile) assetGroup.getLoadedFileThrows("categories.xml");
        var types = new ArrayList<CategoryType>();
        for (Element categoryData : file.getDocument().select("root > categories > type")) {
            types.add(CategoryTypeFactory.fromXmlElement(categoryData, types));
        }
        return new CategoryTypesList(types);
    }

    private static DeviceTypesList loadDeviceTypes(LoadedAssetGroup assetGroup, List<CategoryType> categories, List<MTRDeviceType> mtrDevices) throws Exception {
        var file = (LoadedXmlAssetFile) assetGroup.getLoadedFileThrows("device_types.xml");

        var types = new ArrayList<BaseDeviceType>();
        var abstractDeviceTypes = new ArrayList<AbstractDeviceType>();
        var normalDeviceTypes = new ArrayList<DeviceType>();
        for (Element typeData : file.getDocument().select("root > deviceTypes > type")) {
            var deviceType = BaseDeviceTypeFactory.fromXmlElement(typeData, categories, mtrDevices, types);
            types.add(deviceType);
            if (deviceType instanceof AbstractDeviceType abstractDeviceType) {
                abstractDeviceTypes.add(abstractDeviceType);
                continue;
            }
            // If it's not AbstractDeviceType, it's definitely DeviceType
            normalDeviceTypes.add((DeviceType) deviceType);
        }
        return new DeviceTypesList(abstractDeviceTypes, normalDeviceTypes);
    }

    private static List<DiverseGroupType> loadDiverseGroups(LoadedAssetGroup assetGroup, List<DeviceType> deviceTypes) throws Exception {
        var file = (LoadedXmlAssetFile) assetGroup.getLoadedFileThrows("diverse_group_types.xml");
        var types = new ArrayList<DiverseGroupType>();
        for (Element typeData : file.getDocument().select("root > diverseGroupTypes > type")) {
            types.add(DiverseGroupTypeFactory.fromXmlElement(typeData, deviceTypes));
        }
        return types;
    }
}
