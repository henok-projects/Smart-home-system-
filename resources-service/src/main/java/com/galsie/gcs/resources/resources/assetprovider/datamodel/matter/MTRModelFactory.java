package com.galsie.gcs.resources.resources.assetprovider.datamodel.matter;


import com.galsie.gcs.microservicecommon.lib.galassets.core.loaded.asset.LoadedXmlAssetFile;
import com.galsie.gcs.resources.resources.assetgroup.loaded.LoadedAssetGroup;
import com.galsie.gcs.resources.resources.assetprovider.datamodel.matter.types.MTRClusterControlTypeFactory;
import com.galsie.gcs.resources.resources.assetprovider.datamodel.matter.types.MTRClusterTypeFactory;
import com.galsie.gcs.resources.resources.assetprovider.datamodel.matter.types.MTRDeviceTypeFactory;
import com.galsie.lib.datamodel.matter.MTRModel;
import com.galsie.lib.datamodel.matter.types.MTRClusterControlType;
import com.galsie.lib.datamodel.matter.types.MTRClusterType;
import com.galsie.lib.datamodel.matter.types.MTRDeviceType;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

@Slf4j
public class MTRModelFactory {

    public static MTRModel newInstance(LoadedAssetGroup assetGroup) throws Exception{
        log.info("Loading the Matter Model (Devices, Clusters, Cluster control types)");
        final var clustersFile = (LoadedXmlAssetFile) assetGroup.getLoadedFileThrows("mtr/mtr_clusters");
        final var clusterControlTypesFile = (LoadedXmlAssetFile) assetGroup.getLoadedFileThrows("mtr/mtr_cluster_control_types");
        final var mtrDevicesFile = (LoadedXmlAssetFile) assetGroup.getLoadedFileThrows("mtr/mtr_devices");

        var mtrClusters = new ArrayList<MTRClusterType>();
        for (Element clusterTypeData: clustersFile.getDocument().select("root > mtrClusters > type")){
            mtrClusters.add(MTRClusterTypeFactory.fromXmlElement(clusterTypeData));
        }

        var mtrDevices = new ArrayList<MTRDeviceType>();
        for (Element deviceTypeData: mtrDevicesFile.getDocument().select("root > mtrDevices > type")){
            mtrDevices.add(MTRDeviceTypeFactory.fromXmlElement(deviceTypeData, mtrClusters));
        }

        var clusterControlTypes = new ArrayList<MTRClusterControlType>();
        for (Element clusterControlTypeData: clusterControlTypesFile.getDocument().select("root > clusterControlTypes > type")){
            clusterControlTypes.add(MTRClusterControlTypeFactory.fromXmlElement(clusterControlTypeData));
        }
        log.info("Matter model LOADED");
        return new MTRModel(mtrClusters, clusterControlTypes, mtrDevices);
    }
}
