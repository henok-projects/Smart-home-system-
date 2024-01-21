package com.galsie.gcs.homescommondata.bootstrap.galmodel;

import com.galsie.gcs.homescommondata.bootstrap.helpers.EntityBootstrapService;
import com.galsie.gcs.homescommondata.bootstrap.helpers.TypedEntityBootstrapService;
import com.galsie.gcs.homescommondata.data.entity.protocol.galsie.DeviceTypeEntity;
import com.galsie.gcs.homescommondata.repository.protocol.galsie.CategoryTypeRepository;
import com.galsie.gcs.homescommondata.repository.protocol.galsie.DeviceTypeRepository;
import com.galsie.gcs.homescommondata.repository.protocol.mtr.MTRDeviceTypeRepository;
import com.galsie.lib.datamodel.galsie.types.device.DeviceType;
import com.galsie.lib.utils.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DeviceTypesBootstrapService {

    @Autowired
    DeviceTypeRepository deviceTypeRepository;

    @Autowired
    CategoryTypeRepository categoryTypeRepository;

    @Autowired
    MTRDeviceTypeRepository mtrDeviceTypeRepository;

    private final TypedEntityBootstrapService<DeviceType, DeviceTypeEntity> deviceTypeBootstrapService = new TypedEntityBootstrapService<>(deviceTypeRepository, (dbEnt, dataEnt) -> {
        var updated = false;
        if (!ArrayUtils.listsEqualIgnoreOrder(dbEnt.getPossibleMtrDevices(), dataEnt.getPossibleMtrDevices())){
            dbEnt.setPossibleMtrDevices(dataEnt.getPossibleMtrDevices());
            updated = true;
        }
        if (!dbEnt.getCategoryType().equals(dataEnt.getCategoryType())){
            dbEnt.setCategoryType(dataEnt.getCategoryType());
            updated = true;
        }
        return updated;
    });



    public void bootstrap(List<DeviceType> deviceTypes) throws Exception{
        deviceTypeBootstrapService.bootstrapManyDataObjects(deviceTypes, (deviceType) -> {
           var mtrDevices = ArrayUtils.mapCollectionThrows(deviceType.getAllPossibleMTRDeviceTypes(), (dType) -> EntityBootstrapService.getEntityThrows(mtrDeviceTypeRepository, deviceType.getId()));
           var categoryType = EntityBootstrapService.getEntityThrows(categoryTypeRepository, deviceType.getCategoryType().getId());
           return DeviceTypeEntity.builder().typeId(deviceType.getId()).definition(deviceType.getDefinition()).name(deviceType.getName()).categoryType(categoryType).possibleMtrDevices(mtrDevices).build();
        }, (dataEnt, dbEnt) -> {

        }, true);
    }


}
