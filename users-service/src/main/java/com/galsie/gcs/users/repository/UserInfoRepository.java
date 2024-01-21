package com.galsie.gcs.users.repository;


import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import com.galsie.gcs.users.data.entity.UserInfoEntity;
import org.springframework.stereotype.Repository;
@Repository
public interface UserInfoRepository extends GalRepository<UserInfoEntity, Long> {
}
