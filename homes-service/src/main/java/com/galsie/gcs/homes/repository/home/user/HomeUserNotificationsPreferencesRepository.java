package com.galsie.gcs.homes.repository.home.user;

import com.galsie.gcs.homes.data.entity.home.user.HomeUserEntity;
import com.galsie.gcs.homes.data.entity.home.user.preferences.HomeUserNotificationPreferenceEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface HomeUserNotificationsPreferencesRepository extends GalRepository<HomeUserNotificationPreferenceEntity,Long> {

    Optional<HomeUserNotificationPreferenceEntity> findByHomeUserAndPreferenceKey(HomeUserEntity homeUser, String preferenceKey);
    @Query("SELECT h from HomeUserNotificationPreferenceEntity h WHERE h.homeUser = :homeUser AND h.preferenceKey IN :preferenceKeys")
    List<HomeUserNotificationPreferenceEntity> findByHomeUserAndPreferenceKeyIn(@Param("homeUser") HomeUserEntity homeUser, @Param("preferenceKeys") List<String> preferenceKeys);

//    List<HomeUserNotificationPreferenceEntity> findByHomeUserAndPreferenceKeyIn(HomeUserEntity homeUser, List<String> preferenceKeys);


}
