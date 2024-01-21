package com.galsie.gcs.homes.repository.home.user;
import com.galsie.gcs.homes.data.entity.home.user.HomeUserEntity;
import com.galsie.gcs.homes.data.entity.home.invites.EmailOrPhoneBasedHomeInviteEntity;
import com.galsie.gcs.homes.data.entity.home.invites.HomeInviteEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface HomeUserInviteEntityRepository extends GalRepository<HomeInviteEntity,Long> {

    Optional<HomeInviteEntity> findByInviteUniqueCode(String inviteUniqueCode);

    @Query("SELECT e FROM EmailOrPhoneBasedHomeInviteEntity e WHERE e.invitingUser = :user")
    Optional<EmailOrPhoneBasedHomeInviteEntity> findByInvitor(HomeUserEntity user);
}