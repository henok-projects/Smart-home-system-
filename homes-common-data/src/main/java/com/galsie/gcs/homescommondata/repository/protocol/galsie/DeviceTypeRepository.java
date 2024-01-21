package com.galsie.gcs.homescommondata.repository.protocol.galsie;

import com.galsie.gcs.homescommondata.data.entity.protocol.galsie.DeviceTypeEntity;
import com.galsie.gcs.homescommondata.repository.common.TypedEntityRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeviceTypeRepository extends TypedEntityRepository<DeviceTypeEntity> {

    @Override
    @Query(value = "SELECT t FROM DeviceTypeEntity t WHERE t.uniqueId NOT IN :ids")
    List<DeviceTypeEntity> findAllByUniqueIdsNotMatching(@Param("ids") List<Long> ids);

}