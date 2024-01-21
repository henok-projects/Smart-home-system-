package com.galsie.gcs.users.repository;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import com.galsie.gcs.users.data.entity.UserAccountEntity;
import com.galsie.gcs.users.data.entity.security.UserAuthSessionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface UserAuthSessionRepository extends GalRepository<UserAuthSessionEntity, Long> {

    @Query("select a from UserAuthSessionEntity a where a.user.id = ?1 and a.forceExpired = false and a.validUntil > CURRENT_TIMESTAMP")
    List<UserAuthSessionEntity> getAllActiveByUserId(Long userId);

    @Query("select a from UserAuthSessionEntity a where a.user.id = ?1 and a.forceExpired = false and a.validUntil > CURRENT_TIMESTAMP and a.lastAccess >= ?2")
    List<UserAuthSessionEntity> getAllActiveByUserIdAccessedAfter(Long userId, LocalDateTime accessedAfter);

    Optional<UserAuthSessionEntity> findBySessionToken(String sessionToken);


}
