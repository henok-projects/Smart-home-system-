package com.galsie.gcs.homescommondata.repository.protocol.galsie;

import com.galsie.gcs.homescommondata.data.entity.protocol.galsie.CategoryTypeEntity;
import com.galsie.gcs.homescommondata.repository.common.TypedEntityRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryTypeRepository extends TypedEntityRepository<CategoryTypeEntity> {

    @Override
    @Query(value = "SELECT t FROM CategoryTypeEntity t WHERE t.uniqueId NOT IN :ids")
    List<CategoryTypeEntity> findAllByUniqueIdsNotMatching(@Param("ids") List<Long> ids);

}
