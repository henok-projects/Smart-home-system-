package com.galsie.gcs.resources.resources.assetprovider.datamodel.matter.types;


import com.galsie.gcs.resources.resources.assetprovider.datamodel.common.TypeDefinedObjectFactory;
import com.galsie.lib.datamodel.common.OptionalRequirementType;
import com.galsie.lib.datamodel.matter.types.MTRAttributeType;
import com.galsie.lib.datamodel.matter.types.MTRClusterType;
import com.galsie.lib.datamodel.matter.types.MTRDeviceCluster;
import com.galsie.lib.datamodel.matter.types.MTRDeviceType;
import com.galsie.lib.utils.NumericUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Slf4j
public class MTRDeviceTypeFactory {

    public static MTRDeviceType fromXmlElement(Element element, List<MTRClusterType> clusterTypes) throws Exception {
        var typeInfo = TypeDefinedObjectFactory.fromElement(element);
        //log.info("Loading the Matter Model device type " + typeInfo.getDefinition());
        var defaultId = NumericUtils.parseLong(element.select("defaultId").text()).orElseThrow();
        var deviceClusters = new HashMap<OptionalRequirementType, List<MTRDeviceCluster>>();
        for (OptionalRequirementType type: OptionalRequirementType.values()) {
            var optionalReqClusters = new ArrayList<MTRDeviceCluster>();
            String cssQuery = "clusters > " + type.getXmlTagName() + " > cluster";
            for (Element clusterEl : element.select(cssQuery)) {
                var definition = clusterEl.attributes().get("definition");
                if (definition.isEmpty()) {
                    throw new Exception("Couldn't create new MTRDeviceType '" + typeInfo.getDefinition() + "' from Xml Element: Attribute 'definition' in tag '" + cssQuery + "' doesn't have a value");
                }
                var cluster = clusterTypes.stream().filter((clusterType) -> clusterType.getDefinition().equals(definition)).findFirst().orElseThrow();
                var reqAttrs = new ArrayList<MTRAttributeType>();
                for (Element reqAttrEl : clusterEl.select("requiredAttribute")) {
                    var attrDefinition = reqAttrEl.attributes().get("definition");
                    if (attrDefinition.isEmpty()) {
                        throw new Exception("Couldn't create new MTRDeviceType '" + typeInfo.getDefinition() + "' from Xml Element: Attribute 'definition' in tag '" + cssQuery + " > requiredAttribute' doesn't have a value");
                    }
                    var attributeOpt = cluster.getAllAttributes().stream().filter((attr) -> attr.getDefinition().equals(attrDefinition)).findFirst();
                    if (attributeOpt.isEmpty()){
                        throw new Exception("Couldn't create new MTRDeviceType '" + typeInfo.getDefinition()  + "' from Xml Element: MTRAttributeType '" + attrDefinition + "' was not found in the cluster '" + cluster.getDefinition() + "'");
                    }

                    reqAttrs.add(attributeOpt.get());
                }
                optionalReqClusters.add(new MTRDeviceCluster(cluster, reqAttrs));
            }
            deviceClusters.put(type, optionalReqClusters);
        }
        return new MTRDeviceType(typeInfo.getId(), defaultId, typeInfo.getDefinition(), typeInfo.getName(), deviceClusters);
    }
}
