package com.galsie.gcs.users.repository;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import com.galsie.gcs.users.data.entity.UserEmailEntity;
import com.galsie.gcs.users.data.entity.UserProfilePhotoEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfilePhotoRepository extends GalRepository<UserProfilePhotoEntity, Long > {

        Optional<UserProfilePhotoEntity> findById(Long id);

}
