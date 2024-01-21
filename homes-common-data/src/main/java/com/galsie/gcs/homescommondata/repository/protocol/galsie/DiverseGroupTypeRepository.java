package com.galsie.gcs.homescommondata.repository.protocol.galsie;

import com.galsie.gcs.homescommondata.data.entity.protocol.galsie.DiverseGroupTypeEntity;
import com.galsie.gcs.homescommondata.repository.common.TypedEntityRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiverseGroupTypeRepository extends TypedEntityRepository<DiverseGroupTypeEntity> {

    @Override
    @Query(value = "SELECT t FROM DiverseGroupTypeEntity t WHERE t.uniqueId NOT IN :ids")
    List<DiverseGroupTypeEntity> findAllByUniqueIdsNotMatching(@Param("ids") List<Long> ids);
}
