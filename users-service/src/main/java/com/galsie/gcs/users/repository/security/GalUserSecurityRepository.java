package com.galsie.gcs.users.repository.security;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import com.galsie.gcs.users.data.entity.UserAccountEntity;
import com.galsie.gcs.users.data.entity.security.GalUserSecurityEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GalUserSecurityRepository extends GalRepository<GalUserSecurityEntity, Long> {

    Optional<GalUserSecurityEntity> findByIdAndPassword(Long id, String password);  // NOTE: security entity id matches user id

}
