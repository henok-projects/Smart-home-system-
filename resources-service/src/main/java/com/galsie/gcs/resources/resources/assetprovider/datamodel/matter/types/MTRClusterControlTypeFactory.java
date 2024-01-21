package com.galsie.gcs.resources.resources.assetprovider.datamodel.matter.types;


import com.galsie.gcs.resources.resources.assetprovider.datamodel.common.TypeDefinedObjectFactory;
import com.galsie.lib.datamodel.matter.types.MTRClusterControlType;
import org.jsoup.nodes.Element;

public class MTRClusterControlTypeFactory{

    public static MTRClusterControlType fromXmlElement(Element element) throws Exception{
        var typeInfo = TypeDefinedObjectFactory.fromElement(element);
        return new MTRClusterControlType(typeInfo.getId(), typeInfo.getDefinition(), typeInfo.getName());
    }
}
