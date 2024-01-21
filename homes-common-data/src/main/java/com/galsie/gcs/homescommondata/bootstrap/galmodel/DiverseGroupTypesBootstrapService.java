package com.galsie.gcs.homescommondata.bootstrap.galmodel;

import com.galsie.gcs.homescommondata.bootstrap.helpers.EntityBootstrapService;
import com.galsie.gcs.homescommondata.bootstrap.helpers.TypedEntityBootstrapService;
import com.galsie.gcs.homescommondata.data.entity.protocol.galsie.DiverseGroupTypeEntity;
import com.galsie.gcs.homescommondata.repository.protocol.galsie.DeviceTypeRepository;
import com.galsie.gcs.homescommondata.repository.protocol.galsie.DiverseGroupTypeRepository;
import com.galsie.lib.datamodel.galsie.types.DiverseGroupType;
import com.galsie.lib.utils.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class DiverseGroupTypesBootstrapService {
    @Autowired
    DiverseGroupTypeRepository diverseGroupTypeRepository;

    @Autowired
    DeviceTypeRepository deviceTypeRepository;

    TypedEntityBootstrapService<DiverseGroupType, DiverseGroupTypeEntity> diverseGroupTypeBootstrapService;

    @PostConstruct
    private void onBeansInit(){
        diverseGroupTypeBootstrapService = new TypedEntityBootstrapService<>(diverseGroupTypeRepository, (dbEnt, dataEnt) -> {
            var updated = false;
            if (ArrayUtils.listsEqualIgnoreOrder(dbEnt.getPossibleDeviceTypes(), dataEnt.getPossibleDeviceTypes())){
                dbEnt.setPossibleDeviceTypes(dataEnt.getPossibleDeviceTypes());
                updated = true;
            }

            if (dbEnt.isSystemGroup() != dataEnt.isSystemGroup()){
                dbEnt.setSystemGroup(dataEnt.isSystemGroup());
                updated = true;
            }

            return updated;
        });
    }
    public void bootstrap(List<DiverseGroupType> diverseGroupTypes) throws Exception {
        diverseGroupTypeBootstrapService.bootstrapManyDataObjects(diverseGroupTypes,
                (diverseGroupType) -> DiverseGroupTypeEntity.builder().typeId(diverseGroupType.getId()).definition(diverseGroupType.getDefinition())
                        .name(diverseGroupType.getName()).isSystemGroup(diverseGroupType.isSystemGroup())
                        .possibleDeviceTypes(ArrayUtils.mapCollectionThrows(diverseGroupType.getPossibleDeviceTypes(),
                                        (deviceType) -> EntityBootstrapService.getEntityThrows(deviceTypeRepository, deviceType.getId()))
                        ).build(), (dataEnt, dbEnt) -> {}, true);
    }
}
