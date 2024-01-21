package com.galsie.gcs.microservicecommon.lib.galsecurity.authenticator.cache;

import com.galsie.gcs.microservicecommon.lib.galsecurity.GalSecurityConfiguration;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.activesessions.ActiveSessionListDTO;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Optional;

/**
 * Manages a cache for each {@link GalSecurityAuthSessionType}
 * - It associates each session type with {@link GalSecurityCache} which actually holds the cache data
 * - Cache for each session type can be {@link GalSecuritySessionListCache}
 *
 * NOTE:
 * - Bean created in {@link GalSecurityConfiguration}
 */
@Component
public class GalSecurityAuthenticatorCacheManager {

    private HashMap<GalSecurityAuthSessionType, GalSecurityCache> cacheHashMap = new HashMap<>();

    /**
     * Gets the {@link GalSecurityCache} associated with the provided {@link GalSecurityAuthSessionType}
     *
     * @param galSecurityAuthSessionType The session type to get the cache for
     * @return An Optional holding the {@link GalSecurityCache}, empty if no cache is associated with the session type
     */
    public Optional<GalSecurityCache> getCacheFor(GalSecurityAuthSessionType galSecurityAuthSessionType){
        return Optional.ofNullable(cacheHashMap.get(galSecurityAuthSessionType));
    }

    /**
     * Gets the existing {@link GalSecuritySessionListCache} for the session type
     * - If the cache map contains another type of cache, other than {@link GalSecuritySessionListCache}, an empty optional is returned
     * - If the cache map doesn't contain any cache, adds a new session list cache, and returns that
     * @param sessionType The session type to return the session list cache for
     * @return An Optional holding the {@link GalSecuritySessionListCache}
     */
    public Optional<GalSecuritySessionListCache> getSessionListCacheOrAddDefaultFor(GalSecurityAuthSessionType sessionType){
        var cache = cacheHashMap.getOrDefault(sessionType, new GalSecuritySessionListCache());
        if (!(cache instanceof GalSecuritySessionListCache galSecuritySessionListCache)){
            return Optional.empty();
        }
        cacheHashMap.put(sessionType, cache);
        return Optional.of(galSecuritySessionListCache);
    }

    /**
     * Gets the existing {@link GalSecuritySessionListCache}  for the session type
     * - If the session list cache doesn't exist in the map, an empty optional is returned
     * - If the cache in the map is of another type of cache, other than {@link GalSecuritySessionListCache}, an empty optional is returned
     * @param sessionType  The session type to return the session list cache for
     * @return An Optional holding the {@link GalSecuritySessionListCache}
     */
    public Optional<GalSecuritySessionListCache> getSessionListCacheFor(GalSecurityAuthSessionType sessionType){
        var cache = cacheHashMap.get(sessionType);
        if (cache == null){
            return Optional.empty();
        }
        if (!(cache instanceof GalSecuritySessionListCache securitySessionListCache)){
            return Optional.empty();
        }
        return Optional.of(securitySessionListCache);
    }
}
