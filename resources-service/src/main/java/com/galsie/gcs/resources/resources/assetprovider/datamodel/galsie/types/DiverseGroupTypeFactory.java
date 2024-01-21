package com.galsie.gcs.resources.resources.assetprovider.datamodel.galsie.types;


import com.galsie.gcs.resources.resources.assetprovider.datamodel.common.TypeDefinedObjectFactory;
import com.galsie.lib.datamodel.galsie.types.DiverseGroupType;
import com.galsie.lib.datamodel.galsie.types.device.DeviceType;
import lombok.Getter;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DiverseGroupTypeFactory{

    public static DiverseGroupType fromXmlElement(Element element, List<DeviceType> deviceTypes) throws  Exception{
        var typeInfo = TypeDefinedObjectFactory.fromElement(element);
        var isSystemGroup = Boolean.parseBoolean(element.attributes().get("isSystemGroup"));
        var possibleDeviceTypes = new ArrayList<DeviceType>();
        for (Element deviceTypeData: element.select("possibleDeviceTypes > deviceType")){
            possibleDeviceTypes.add(deviceTypes.stream().filter((dType) -> dType.getDefinition().equals(deviceTypeData.attributes().get("definition"))).findFirst().orElseThrow());
        }
        return new DiverseGroupType(typeInfo.getId(), typeInfo.getDefinition(), typeInfo.getName(), isSystemGroup, possibleDeviceTypes);
    }
}
