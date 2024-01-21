package com.galsie.gcs.microservicecommon.lib.galassets;

import com.galsie.gcs.microservicecommon.lib.galassets.data.discrete.assetsubscription.GCSSubscriptionState;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.AbstractProvidableAssetDTOType;
import com.galsie.gcs.microservicecommon.lib.galassets.init.*;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.AbstractProvidableAssetDTOWithTypeDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.AbstractProvidedAssetDTO;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
public class GCSGalAssetsProvidedDTOCache {

    static final long CACHE_SIZE_LIMIT = 1000;

    static final long EXPIRE_AFTER_HOURS = 6;

    private final Cache<AbstractProvidableAssetDTOType, AbstractProvidedAssetDTO> assetCache = Caffeine.newBuilder().maximumSize(CACHE_SIZE_LIMIT).expireAfterAccess(EXPIRE_AFTER_HOURS, TimeUnit.HOURS).build();


    /*
    Check if in cache
    */
    public boolean isInCache(AbstractProvidableAssetDTOType abstractProvidableDTOType){
        return assetCache.getIfPresent(abstractProvidableDTOType) != null;
    }
    /*
    Update
    */
    public void updateProvidedAssetList(List<AbstractProvidableAssetDTOWithTypeDTO> abstractProvidableAssetDTOWithTypeDTOList){  // Note: renamed UpdateCacheDTO to abstractProvidableAssetWithTypeDTO
        for(AbstractProvidableAssetDTOWithTypeDTO assetWithTypeDTO: abstractProvidableAssetDTOWithTypeDTOList){
            receiveProvidedAssetDTOUpdate(assetWithTypeDTO);
        }
    }

    public void receiveProvidedAssetDTOUpdate(AbstractProvidableAssetDTOWithTypeDTO abstractProvidableAssetDTOWithTypeDTO){
        receiveProvidedAssetDTOUpdate(abstractProvidableAssetDTOWithTypeDTO.getProvidableAssetDTOType(), abstractProvidableAssetDTOWithTypeDTO.getProvidedAssetDTO());
    }


    public void receiveProvidedAssetDTOUpdate(AbstractProvidableAssetDTOType dtoType, AbstractProvidedAssetDTO  dto){
        assetCache.put(dtoType, dto);
    }



    /**
    Purge
    remove all values from cache
    */
    public void purgeAllCache(){
        assetCache.invalidateAll();
    }

    /**
     * remove the values for the dto types from cache
     * @param abstractProvidableAssetDTOTypeList
     */
    public void purgeProvidedAssetList(List<AbstractProvidableAssetDTOType> abstractProvidableAssetDTOTypeList){
        assetCache.invalidateAll(abstractProvidableAssetDTOTypeList);
    }

    /**
     * remove the value for the dto type from cache
     * @param dtoType
     */
    public void purgeProvidedAsset(AbstractProvidableAssetDTOType dtoType){
        assetCache.invalidate(dtoType);
    }

    public <T> Optional<T> getCachedProvidableAssetDTO(AbstractProvidableAssetDTOType assetType, Class<T> providableAssetDTOClass){
        if(ProvidableAssetDTOsStartupAndSubscriptionService.getSubscriptionState().equals(GCSSubscriptionState.NOT_SUBSCRIBED)){
            log.warn("Failed to get cached dto because subscription state is not subscribed");
            return Optional.empty();
        }
        var cachedDTO = assetCache.getIfPresent(assetType);
        if(cachedDTO == null){
            return Optional.empty();
        }
        try{
            return Optional.of(providableAssetDTOClass.cast(cachedDTO));
        }catch (ClassCastException e){
            log.error("Failed to cast cached dto to requested type because of class cast exception");
        }
        return Optional.empty();
    }

}
