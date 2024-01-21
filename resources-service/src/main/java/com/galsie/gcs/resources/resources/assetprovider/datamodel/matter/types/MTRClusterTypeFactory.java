package com.galsie.gcs.resources.resources.assetprovider.datamodel.matter.types;


import com.galsie.gcs.resources.resources.assetprovider.datamodel.common.TypeDefinedObjectFactory;
import com.galsie.lib.datamodel.common.OptionalRequirementType;
import com.galsie.lib.datamodel.matter.types.MTRAttributeType;
import com.galsie.lib.datamodel.matter.types.MTRClusterType;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MTRClusterTypeFactory {

    public static MTRClusterType fromXmlElement(Element element) throws Exception{
        var typeInfo = TypeDefinedObjectFactory.fromElement(element);
        Map<OptionalRequirementType, List<MTRAttributeType>> attributes = new HashMap<>();
        attributes.put(OptionalRequirementType.REQUIRED, new ArrayList<>());
        attributes.put(OptionalRequirementType.OPTIONAL, new ArrayList<>());
        for (Element attrEl : element.select("mtrAttributes > required > attribute")) {
            attributes.get(OptionalRequirementType.REQUIRED).add(MTRAttributeTypeFactory.fromXmlElement(attrEl));
        }
        for (Element attrEl : element.select("mtrAttributes > optional > attribute")) {
            attributes.get(OptionalRequirementType.OPTIONAL).add(MTRAttributeTypeFactory.fromXmlElement(attrEl));
        }

        return new MTRClusterType(typeInfo.getId(), typeInfo.getDefinition(), typeInfo.getName(), attributes);

    }
}
