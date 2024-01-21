package com.galsie.gcs.microservicecommon.lib.galsecurity.authenticator.cache;

import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.activesessions.ActiveSessionListDTO;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class GalSecuritySessionListCache implements GalSecurityCache{
    static long CACHE_SIZE_LIMIT = 5000; // TODO: QA and move to application.yml
    static long EXPIRY_HOURS = 6; // TODO: QA and move to application.yml

    private final Cache<Object, ActiveSessionListDTO<Object>> activeSessionsCache = Caffeine.newBuilder().maximumSize(CACHE_SIZE_LIMIT).expireAfterAccess(EXPIRY_HOURS, TimeUnit.HOURS).build();


    public <idType> Optional<ActiveSessionListDTO<idType>> getActiveSessionListFor(idType id){
        var cachedSessionList = activeSessionsCache.getIfPresent(id);
        if (cachedSessionList == null){
            return Optional.empty();
        }
        if (!id.getClass().isAssignableFrom(cachedSessionList.getId().getClass())){
            return Optional.empty();
        }
        return Optional.ofNullable( (ActiveSessionListDTO<idType>) cachedSessionList);
    }

    public <idType> void updateActiveSessionList(ActiveSessionListDTO<idType> activeSessionListDTO){
        activeSessionsCache.put(activeSessionListDTO.getId(), (ActiveSessionListDTO<Object>) activeSessionListDTO);
    }
}
