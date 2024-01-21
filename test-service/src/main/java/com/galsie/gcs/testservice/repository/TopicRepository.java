package com.galsie.gcs.testservice.repository;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import com.galsie.gcs.testservice.entity.inheritence.TopicEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends GalRepository<TopicEntity, Long> {

    @Query(value = "SELECT t FROM TopicEntity t WHERE t.uniqueId  NOT IN :ids")
    List<TopicEntity> findAllByUniqueIdsNotMatching(@Param("ids") List<Long> ids);
}
