package com.galsie.gcs.users.repository;


import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import com.galsie.gcs.users.data.entity.UserEmailEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserEmailRepository extends GalRepository<UserEmailEntity, Long > {

    Optional<UserEmailEntity> findByEmail(String email);
}
