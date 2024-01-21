package com.galsie.gcs.users.repository.usersecuritypin;


import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import com.galsie.gcs.users.data.entity.security.usersecuritypin.UserPinSecurityEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSecurityPinRepository extends GalRepository<UserPinSecurityEntity,Long> {
    Optional<UserPinSecurityEntity> findById(Long id);
}

