package com.galsie.gcs.homescommondata.repository.common;

import com.galsie.gcs.homescommondata.data.entity.protocol.common.TypedEntity;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/*
Common to all typed entity repos
 */
@NoRepositoryBean
public interface TypedEntityRepository<T extends TypedEntity> extends BootstrappedEntityRepository<T, Long> {

    List<T> findAllByTypeId(Long typeId);
    List<T> findAllByTypeIdAndArchived(Long typeId, boolean archived);
}
