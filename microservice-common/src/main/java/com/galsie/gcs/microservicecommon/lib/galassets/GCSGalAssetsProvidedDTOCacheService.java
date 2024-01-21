package com.galsie.gcs.microservicecommon.lib.galassets;

import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.providableassetdto.general.get.request.GetProvidableAssetDTORequestDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.providableassetdto.general.get.response.GetProvidableAssetDTOResponseDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.AbstractProvidableAssetDTOType;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.AbstractProvidableAssetDTOWithTypeDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.ProvidableAssetDTOType;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.AbstractProvidedAssetDTO;
import com.galsie.gcs.microservicecommon.lib.gcsrequests.GCSRemoteRequests;
import com.galsie.gcs.microservicecommon.lib.gcsrequests.destination.microservice.GCSMicroservice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Optional;

/**
 * this service serves as an interface betweeen the cache and the rest of the application/microservice in term of provided asset DTOs.
 */
@Slf4j
public class GCSGalAssetsProvidedDTOCacheService {

    private final GCSGalAssetsProvidedDTOCache cache;


    @Autowired
    GCSRemoteRequests gcsRemoteRequests;

    @Autowired
    GCSGalAssetsConfigurationDataProvider gcsGalAssetsConfigurationDataProvider;

    public GCSGalAssetsProvidedDTOCacheService() {
        this.cache = new GCSGalAssetsProvidedDTOCache();
    }

    /**
     * Retrieves a providable asset DTO based on the specified DTO type and class. The method first attempts
     * to fetch the DTO from the cache. If not found in the cache, it then makes a remote request to retrieve
     * the asset DTO.
     * Once the DTO is fetched from a remote source, it is then updated in the cache(if it was subscribed to) for faster subsequent
     * access.
     */
    public <T> Optional<T> getProvidableAssetDTO(AbstractProvidableAssetDTOType dtoType, Class<T> providableAssetDTOClass){
        var cachedDTOOpt = this.cache.getCachedProvidableAssetDTO(dtoType, providableAssetDTOClass);
        if (cachedDTOOpt.isPresent()){
            var cached = cachedDTOOpt.get();
            return Optional.of(cached);
        }
        var dtoOpt = gcsInternalRemoteRequestProvidableAssetDTO(dtoType, providableAssetDTOClass);
        if (dtoOpt.isEmpty()){
            return Optional.empty();
        }
        if(gcsGalAssetsConfigurationDataProvider.getSubscribableProvidedAssetDTOTypes().contains(dtoType)) {
            this.cache.receiveProvidedAssetDTOUpdate(dtoType, (AbstractProvidedAssetDTO) dtoOpt.get());
        }
        return dtoOpt;
    }

    public <T> Optional<T> getProvidableAssetDTOWithoutRequest(AbstractProvidableAssetDTOType dtoType, Class<T> providableAssetDTOClass) {
        var cachedDTOOpt = this.cache.getCachedProvidableAssetDTO(dtoType, providableAssetDTOClass);
        if (cachedDTOOpt.isPresent()){
            var cached = cachedDTOOpt.get();
            return Optional.of(cached);
        }
        return Optional.empty();
    }


    private <T> Optional<T> gcsInternalRemoteRequestProvidableAssetDTO(AbstractProvidableAssetDTOType dtoType, Class<T> providableAssetDTOClass){
        var request = GetProvidableAssetDTORequestDTO.builder().providableAssetDTOType(dtoType.getName()).build();
        try {
            var response = gcsRemoteRequests.initiateRequest(GetProvidableAssetDTOResponseDTO.class)
                    .destination(GCSMicroservice.RESOURCES, "/providableAssetDTOs", "/request/dto")
                    .httpMethod(HttpMethod.POST)
                    .setRequestPayload(request)
                    .performRequestWithGCSResponse()
                    .toGCSResponse();
            if (response.hasError()) {
                log.info("Failed to get DTO : " + response.getGcsError().getErrorType().name() + " " + response.getGcsError().getErrorMsg());
                return Optional.empty();
            }
            var responseData = response.getResponseData();
            if (responseData.getProvidedAssetDTO() == null) {
                log.info("ProvidedAssetDTO is null");
                return Optional.empty();
            }
            return Optional.of((T) responseData.getProvidedAssetDTO());
        }catch (Exception e){
            log.error("Failed to get DTO : " + e.getMessage());
            return Optional.empty();
        }

    }
        // preform the request, cast the dto in the response to T if providableAssetDTOClass is assignable from its class


    public void receiveProvidedAssetDTOUpdate(boolean ignoreItemNotInCache, List<AbstractProvidableAssetDTOWithTypeDTO> abstractProvidableAssetDTOWithTypeDTOList){  // Note: renamed UpdateCacheDTO to abstractProvidableAssetWithTypeDTO
        for(var dto: abstractProvidableAssetDTOWithTypeDTOList){
            receiveProvidedAssetDTOUpdate(ignoreItemNotInCache, dto);
        }
    }

    /**
     * to update and item in cache if the item is not in cache and ignoreItemNotInCache is false, the item is not updated else it is updated
     * if the item is in cache, it is updated
     * @param ignoreItemNotInCache
     * @param abstractProvidableAssetDTOWithTypeDTO
     */
    public void receiveProvidedAssetDTOUpdate(boolean ignoreItemNotInCache, AbstractProvidableAssetDTOWithTypeDTO abstractProvidableAssetDTOWithTypeDTO){
        if (!gcsInternalShouldCache(ignoreItemNotInCache, abstractProvidableAssetDTOWithTypeDTO.getProvidableAssetDTOType())){
            return;
        }
        this.cache.receiveProvidedAssetDTOUpdate(abstractProvidableAssetDTOWithTypeDTO);
    }

    /**
     * removes the given providableAssetDTOType from cache
     * @param providableAssetDTOType
     */
    public void removeProvidedAssetDTO(AbstractProvidableAssetDTOType providableAssetDTOType){
        this.cache.purgeProvidedAsset(providableAssetDTOType);
    }

    /**
     * check if the given providableAssetDTOType is in cache, if ignoreItemNotInCache is true, return true, else return the result of the check
     */
    private boolean gcsInternalShouldCache(boolean ignoreItemNotInCache, AbstractProvidableAssetDTOType dtoType){
        boolean cached = cache.isInCache(dtoType);
        if(ignoreItemNotInCache){
            return true;
        }
        else return  cached;
    }

}
