package com.galsie.gcs.resources.resources.assetprovider.datamodel.common;

import com.galsie.gcs.resources.utils.ArrayUtils;
import com.galsie.gcs.resources.utils.XmlUtils;
import com.galsie.lib.datamodel.common.TypeDefinedObject;
import com.galsie.lib.utils.NumericUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jsoup.nodes.Element;

@AllArgsConstructor
@Getter
public class TypeDefinedObjectFactory {
    public static TypeDefinedObject fromElement(Element element) throws Exception {
        var tagData = XmlUtils.readStringDataFromCssQuery(element, ArrayUtils.of("id", "definition", "name"));
        var idText = tagData.get("id");
        var id = NumericUtils.parseLong(idText);
        if (id.isEmpty()){
            throw new Exception("Couldn't create new instance: Failed to parse id '" + idText  + "' to int");
        }
        return new TypeDefinedObject(id.get(), tagData.get("definition"), tagData.get("name"));
    }
}
