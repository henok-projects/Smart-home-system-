package com.galsie.gcs.resources.resources.assetprovider.datamodel.matter.types;

import com.galsie.lib.datamodel.matter.types.MTRAttributeType;
import com.galsie.lib.datamodel.matter.types.MTRAttributeTypeSide;
import org.jsoup.nodes.Element;

public class MTRAttributeTypeFactory {

    public static MTRAttributeType fromXmlElement(Element element) throws Exception{
        String definition = element.attributes().get("definition");
        if (definition.isEmpty()){
            throw new Exception("Couldn't create new MtrAttributeType from Xml Element: Attribute 'definition' doesn't have a value");
        }
        String sideStr = element.attr("side");
        if (sideStr.isEmpty()){
            throw new Exception("Couldn't create new MtrAttributeType from Xml Element: Attribute 'side' doesn't have a value");
        }
        var typeSide = MTRAttributeTypeSide.fromXmlName(sideStr).orElseThrow();
        return new MTRAttributeType(definition, typeSide);
    }
}