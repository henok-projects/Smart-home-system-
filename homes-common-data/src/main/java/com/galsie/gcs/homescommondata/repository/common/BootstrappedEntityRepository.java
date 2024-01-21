package com.galsie.gcs.homescommondata.repository.common;

import com.galsie.gcs.homescommondata.data.entity.protocol.common.BootstrappedEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;
@NoRepositoryBean
public interface BootstrappedEntityRepository<entType extends BootstrappedEntity<idType>, idType> extends GalRepository<entType, idType> {
    List<entType> findAllByUniqueIdsNotMatching(@Param("ids") List<idType> ids);
}
