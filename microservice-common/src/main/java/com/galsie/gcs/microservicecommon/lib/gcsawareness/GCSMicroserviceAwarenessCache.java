package com.galsie.gcs.microservicecommon.lib.gcsawareness;

import com.galsie.gcs.microservicecommon.lib.gcsawareness.data.dto.GCSMicroserviceAwarenessStatusDTO;
import com.galsie.gcs.microservicecommon.lib.gcsrequests.destination.microservice.GCSMicroservice;
import com.galsie.lib.utils.pair.Pair;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class GCSMicroserviceAwarenessCache {

    static final long CACHE_SIZE_LIMIT = 500;
    static final long EXPIRE_AFTER_DAYS = 5 * 3;
    private final Cache<Pair<GCSMicroservice, String>, GCSMicroserviceAwarenessStatusDTO> microserviceAwarenessCache = Caffeine.newBuilder().maximumSize(CACHE_SIZE_LIMIT).expireAfterAccess(EXPIRE_AFTER_DAYS * 2, TimeUnit.DAYS).build();

    public void updateMicroserviceAwarenessStatus(Pair<GCSMicroservice, String> cacheKey, GCSMicroserviceAwarenessStatusDTO gcsMicroserviceAwarenessStatusDTO) {
        microserviceAwarenessCache.put(cacheKey, gcsMicroserviceAwarenessStatusDTO);
    }

    public List<GCSMicroserviceAwarenessStatusDTO> getAllMicroserviceInstanceAwareStatus(GCSMicroservice gcsMicroservice) {
        return microserviceAwarenessCache.asMap().values().stream().filter(gcsMicroserviceAwarenessStatusDTO -> gcsMicroserviceAwarenessStatusDTO.getServiceName().equals(gcsMicroservice)).toList();
    }

    public GCSMicroserviceAwarenessStatusDTO getMicroserviceAwarenessStatus(Pair<GCSMicroservice, String> cacheKey){
        return microserviceAwarenessCache.getIfPresent(cacheKey);
    }

    public List<GCSMicroserviceAwarenessStatusDTO> getAllMicroserviceAwarenessStatus() {
        return microserviceAwarenessCache.asMap().values().stream().toList();
    }

}
