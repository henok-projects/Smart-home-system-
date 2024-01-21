package com.galsie.gcs.users.repository;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import com.galsie.gcs.users.data.entity.UserAccountEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserAccountRepository extends GalRepository<UserAccountEntity, Long> {
    Optional<UserAccountEntity> findByUsername(String username);
    List<UserAccountEntity> searchByUsername(String username);
    UserAccountEntity getByUsername(String username);
    Optional<UserAccountEntity> findByUserEmail(String email);

}
