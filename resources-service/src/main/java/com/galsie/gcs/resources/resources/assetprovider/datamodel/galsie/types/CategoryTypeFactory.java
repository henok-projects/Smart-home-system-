package com.galsie.gcs.resources.resources.assetprovider.datamodel.galsie.types;


import com.galsie.gcs.resources.resources.assetprovider.datamodel.common.TypeDefinedObjectFactory;
import com.galsie.lib.datamodel.galsie.types.CategoryType;
import com.galsie.lib.datamodel.galsie.types.device.BaseDeviceType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Element;

import java.util.List;

public class CategoryTypeFactory {
    static Logger logger = LogManager.getLogger();

    /**
     *
     * @param element The <type> element to load the category from
     * @param loadedCategories The already loaded categories, in case this category extends another one
     * @return The CategoryType
     * @throws Exception
     */
    public static CategoryType fromXmlElement(Element element, List<CategoryType> loadedCategories) throws Exception{
        var typeInfo = TypeDefinedObjectFactory.fromElement(element);
        var isAbstract = Boolean.parseBoolean(element.attributes().get("abstract"));
        var extendsCategoryTxt = element.attributes().get("extends");
        CategoryType extendsCategory = extendsCategoryTxt.isEmpty() ? null: loadedCategories.stream().filter((a) -> a.getDefinition().equals(extendsCategoryTxt)).findFirst().orElseThrow(); // null if attribute is empty, throw if attribute has text but category type not found
        if (extendsCategory != null && !extendsCategory.isAbstract()){
            logger.warn("Warning while creating the CategoryType '" + typeInfo.getName() + "': CategoryType extends a non-abstract Category Type '" + extendsCategoryTxt + "'");
        }
        return new CategoryType(typeInfo.getId(), typeInfo.getDefinition(), typeInfo.getName(), isAbstract, extendsCategory);
    }
}
