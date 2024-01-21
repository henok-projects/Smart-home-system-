package com.galsie.gcs.users.repository.security;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import com.galsie.gcs.users.data.entity.security.UserAccountSecurityPreferencesEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountSecurityPreferencesRepository extends GalRepository<UserAccountSecurityPreferencesEntity, Long> {
}
