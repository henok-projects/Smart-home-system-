package com.galsie.gcs.users.repository;


import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import com.galsie.gcs.users.data.entity.UserPhoneEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserPhoneRepository extends GalRepository<UserPhoneEntity, Long> {

    Optional<UserPhoneEntity> findByCountryCodeAndPhoneNumber(short countryCode, String phoneNumber);



}
