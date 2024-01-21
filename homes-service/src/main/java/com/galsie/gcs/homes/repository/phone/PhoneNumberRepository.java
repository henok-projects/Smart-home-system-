package com.galsie.gcs.homes.repository.phone;

import com.galsie.gcs.homes.data.entity.home.common.PhoneNumberEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneNumberRepository extends GalRepository<PhoneNumberEntity, Long> {
}
