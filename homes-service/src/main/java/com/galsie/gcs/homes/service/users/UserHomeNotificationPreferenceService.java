package com.galsie.gcs.homes.service.users;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.galsie.gcs.homes.data.discrete.notificationpreferences.HomeUserNotificationPreference;
import com.galsie.gcs.homes.data.entity.home.user.HomeUserEntity;
import com.galsie.gcs.homes.data.entity.home.user.preferences.HomeUserNotificationPreferenceEntity;
import com.galsie.gcs.homes.data.entity.home.user.preferences.HomeUserPreferencesEntity;
import com.galsie.gcs.homes.repository.home.user.HomeUserNotificationsPreferencesRepository;
import com.galsie.gcs.microservicecommon.lib.gcsjackson.GCSObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

import static com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse.saveEntityThrows;

@Service
public class UserHomeNotificationPreferenceService {

    @Autowired
    private HomeUserNotificationsPreferencesRepository preferencesRepository;

    @Autowired
    private GCSObjectMapper objectMapper;

    public void updatePreferences(HomeUserEntity user, HashMap<HomeUserNotificationPreference, Object> preferences) throws JsonProcessingException {

        // Retrieve (or create) the HomeUserPreferencesEntity associated with the given HomeUserEntity
        var userPreferences = user.getHomeUserPreferences();
        if (userPreferences == null) {
            userPreferences = new HomeUserPreferencesEntity();
            userPreferences.setHomeUser(user);

        }

        var finalUserPreferences = userPreferences;
        preferences.entrySet().stream()
                .filter(entry -> {
                    var expectedClass = entry.getKey().getValueType();
                    return expectedClass.isInstance(entry.getValue());
                })
                .forEach(entry -> {
                    Optional<HomeUserNotificationPreferenceEntity> existingEntityOpt = preferencesRepository.findByHomeUserAndPreferenceKey(user, entry.getKey().getKey());

                    HomeUserNotificationPreferenceEntity entity;
                    if (existingEntityOpt.isPresent()) {
                        entity = existingEntityOpt.get();
                    } else {
                        entity = new HomeUserNotificationPreferenceEntity();
                        entity.setHomeUserPreferences(finalUserPreferences); // set the relation here
                        entity.setPreferenceKey(entry.getKey().getKey());
                    }

                    try {
                        entity.setPreferenceValue(objectMapper.writeValueAsString(entry.getValue()));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                    saveEntityThrows(preferencesRepository, entity);
                });
    }



    public HashMap<HomeUserNotificationPreference, Object> getPreferences(HomeUserEntity user, List<HomeUserNotificationPreference> preferences) throws JsonProcessingException {
        HashMap<HomeUserNotificationPreference, Object> resultMap = new HashMap<>();
        var preferenceKeys = preferences.stream().map(HomeUserNotificationPreference::getKey).collect(Collectors.toList());
        var entities = preferencesRepository.findByHomeUserAndPreferenceKeyIn(user, preferenceKeys);

        entities.forEach(entity -> {
            var preference = HomeUserNotificationPreference.valueOfKey(entity.getPreferenceKey());
            if (preference != null) {
                try {
                    var value = objectMapper.readValue(entity.getPreferenceValue(), preference.getValueType());
                    resultMap.put(preference, value);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return resultMap;
    }


    /**
     * IF NOT in database, uses default value.
     * @param user
     * @param preference
     * @param classOfT
     * @return
     * @param <T>
     * @throws JsonProcessingException
     */
    public <T> T getPreferenceValue(HomeUserEntity user, HomeUserNotificationPreference preference, Class<T> classOfT) throws JsonProcessingException {
        var entityOpt = preferencesRepository.findByHomeUserAndPreferenceKey(user, preference.getKey());

        if (entityOpt.isPresent()) {
            return objectMapper.readValue(entityOpt.get().getPreferenceValue(), classOfT);
        }

        return (T) preference.getDefaultValue();
    }

}

