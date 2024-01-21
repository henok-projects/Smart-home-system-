package com.galsie.gcs.homescommondata.bootstrap;

import com.galsie.gcs.homescommondata.bootstrap.helpers.TypedEntityBootstrapService;
import com.galsie.gcs.homescommondata.data.entity.protocol.matter.MTRClusterTypeEntity;
import com.galsie.gcs.homescommondata.data.entity.protocol.matter.MTRDeviceTypeEntity;
import com.galsie.gcs.homescommondata.repository.protocol.mtr.MTRClusterTypeRepository;
import com.galsie.gcs.homescommondata.repository.protocol.mtr.MTRDeviceTypeRepository;
import com.galsie.lib.datamodel.matter.MTRModel;
import com.galsie.lib.datamodel.matter.types.MTRClusterType;
import com.galsie.lib.datamodel.matter.types.MTRDeviceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MTRModelBootstrapService {

    @Autowired
    MTRDeviceTypeRepository mtrDeviceTypeRepository;

    @Autowired
    MTRClusterTypeRepository mtrClusterTypeRepository;

    TypedEntityBootstrapService<MTRDeviceType, MTRDeviceTypeEntity> mtrDeviceTypeBootstrap = new TypedEntityBootstrapService<>(mtrDeviceTypeRepository, (dbEnt, dataEnt) -> false);
    TypedEntityBootstrapService<MTRClusterType, MTRClusterTypeEntity> mtrClusterTypeBootstrap = new TypedEntityBootstrapService<>(mtrClusterTypeRepository, (dbEnt, dataEnt) -> false);

    /*
    Note:
    - Archive when entity was in database but is not in MTRModel
    - Archive when entity has same id as MTRModel, but different data. Maintain any links from other entities to archive, so that no data is lost (devices may have stored data for feature attributes for example)
        - Create a new entity with the same id that is not archived and re-link
     */
    public void bootstrap(MTRModel mtrModel) throws Exception{
        mtrClusterTypeBootstrap.bootstrapManyDataObjects(mtrModel.getClusters(), (mtrClusterType) -> MTRClusterTypeEntity.builder().typeId(mtrClusterType.getId()).name(mtrClusterType.getName()).definition(mtrClusterType.getDefinition()).build(), (mtrClusterType, mtrClusterTypeDbEnt) -> {}, true);
        mtrDeviceTypeBootstrap.bootstrapManyDataObjects(mtrModel.getMtrDeviceTypes(), (mtrDeviceType) -> MTRDeviceTypeEntity.builder().typeId(mtrDeviceType.getId()).definition(mtrDeviceType.getDefinition()).name(mtrDeviceType.getName()).build(), (mtrDeviceType, mtrDbEnt) -> {}, true);
    }
}
