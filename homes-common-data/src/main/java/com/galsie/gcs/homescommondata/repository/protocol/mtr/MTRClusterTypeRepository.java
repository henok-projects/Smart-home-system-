package com.galsie.gcs.homescommondata.repository.protocol.mtr;

import com.galsie.gcs.homescommondata.data.entity.protocol.matter.MTRClusterTypeEntity;
import com.galsie.gcs.homescommondata.repository.common.TypedEntityRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MTRClusterTypeRepository extends TypedEntityRepository<MTRClusterTypeEntity> {
    @Override
    @Query(value = "SELECT t FROM MTRClusterTypeEntity t WHERE t.uniqueId NOT IN :ids")
    List<MTRClusterTypeEntity> findAllByUniqueIdsNotMatching(@Param("ids") List<Long> ids);
}
