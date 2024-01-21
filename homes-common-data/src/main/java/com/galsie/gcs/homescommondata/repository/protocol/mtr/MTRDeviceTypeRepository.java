package com.galsie.gcs.homescommondata.repository.protocol.mtr;

import com.galsie.gcs.homescommondata.data.entity.protocol.matter.MTRDeviceTypeEntity;
import com.galsie.gcs.homescommondata.repository.common.TypedEntityRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MTRDeviceTypeRepository extends TypedEntityRepository<MTRDeviceTypeEntity> {

    @Override
    @Query(value = "SELECT t FROM MTRDeviceTypeEntity t WHERE t.uniqueId NOT IN :ids")
    List<MTRDeviceTypeEntity> findAllByUniqueIdsNotMatching(@Param("ids") List<Long> ids);
}
