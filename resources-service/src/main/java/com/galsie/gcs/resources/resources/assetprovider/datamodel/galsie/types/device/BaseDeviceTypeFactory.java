package com.galsie.gcs.resources.resources.assetprovider.datamodel.galsie.types.device;


import com.galsie.gcs.resources.resources.assetprovider.datamodel.common.TypeDefinedObjectFactory;
import com.galsie.lib.datamodel.galsie.types.CategoryType;
import com.galsie.lib.datamodel.galsie.types.device.AbstractDeviceType;
import com.galsie.lib.datamodel.galsie.types.device.BaseDeviceType;
import com.galsie.lib.datamodel.galsie.types.device.DeviceType;
import com.galsie.lib.datamodel.matter.types.MTRDeviceType;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public abstract class BaseDeviceTypeFactory {
    static Logger logger = LogManager.getLogger();

    /*
    Returns either AbstractDeviceType or DeviceType
     */
    public static BaseDeviceType fromXmlElement(Element element, List<CategoryType> categories, List<MTRDeviceType> mtrDeviceTypes, List<BaseDeviceType> loadedDeviceTypes) throws Exception {
        var typeInfo = TypeDefinedObjectFactory.fromElement(element);
        //logger.info("Loading the GalModel device type " + typeInfo.getDefinition());

        var isAbstract = Boolean.parseBoolean(element.attributes().get("abstract"));
        var extendsDeviceTxt = element.attributes().get("extends");
        BaseDeviceType extendsDevice = extendsDeviceTxt.isEmpty() ? null: loadedDeviceTypes.stream().filter((a) -> a.getDefinition().equals(extendsDeviceTxt)).findFirst().orElseThrow(); // null if attribute is empty, throw if attribute has text but device not found
        if (extendsDevice != null && !extendsDevice.isAbstract()){
            logger.warn("Warning while creating the DeviceType '" + typeInfo.getName() + "': Device extends a non-abstract device '" + extendsDeviceTxt + "'");
        }

        var possibleMTRDevices = new ArrayList<MTRDeviceType>(); // append the device types in the device it extends
        for (Element mtrDeviceData : element.select("possibleMTRDeviceTypes > mtrDevice")) {
            var mtrDeviceDef = mtrDeviceData.attributes().get("definition");
            var mtrDevice = mtrDeviceTypes.stream().filter((dtype) -> dtype.getDefinition().equals(mtrDeviceDef)).findFirst().orElseThrow(() -> new Exception("Couldn't find the Matter device type with definition " + mtrDeviceDef));
            possibleMTRDevices.add(mtrDevice);
        }

        if (isAbstract){
            return new AbstractDeviceType(typeInfo.getId(), typeInfo.getDefinition(), typeInfo.getName(), extendsDevice, possibleMTRDevices);
        }
        var categoryEl = element.select("category").first();
        if (categoryEl == null) {
            throw new Exception("Couldn't create new GalDeviceType of definition '" + typeInfo.getDefinition() + "': Tag 'category' wasn't found");
        }
        var category = categories.stream().filter((cat) -> categoryEl.attributes().get("definition").equals(cat.getDefinition())).findFirst().orElseThrow();

        return new DeviceType(typeInfo.getId(), typeInfo.getDefinition(), typeInfo.getName(), extendsDevice, possibleMTRDevices, category);
    }

}
