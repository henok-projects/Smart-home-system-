package com.galsie.gcs.resources.service.assetsync;

import com.galsie.gcs.microservicecommon.lib.galassets.data.discrete.assetsync.GalAssetsSyncResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.galassets.data.discrete.assetsync.GetSynchronizedPageErrorType;
import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.assetsync.request.AssetGroupFileListSyncRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.assetsync.request.AssetGroupFileSyncRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.assetsync.request.GalAssetsSyncRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.assetsync.request.GetSynchronizedPageRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.assetsync.response.GalAssetsSyncResponseDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.assetsync.response.GetSynchronizedPageResponseDTO;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.resources.data.entity.assetgroup.AssetGroupFileEntity;
import com.galsie.gcs.resources.data.entity.sync.GalAssetsSynchronizationEntity;
import com.galsie.gcs.resources.data.entity.sync.GalAssetsSynchronizedFileEntity;
import com.galsie.gcs.resources.repository.assetgroup.AssetGroupFileRepository;
import com.galsie.gcs.resources.repository.assetgroup.AssetGroupRepository;
import com.galsie.gcs.resources.repository.sync.assetsync.GalAssetsSynchronizationEntityRepository;
import com.galsie.gcs.resources.repository.sync.assetsync.GalAssetsSynchronizedFileEntityRepository;
import com.galsie.gcs.resources.resources.assetgroup.loaded.LoadedAssetGroup;
import com.galsie.gcs.resources.utils.ResourceReaderUtils;
import com.galsie.lib.utils.StringUtils;
import com.galsie.lib.utils.pair.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class GalAssetsSynchronizationService {

    @Autowired
    AssetGroupRepository assetGroupRepository;

    @Autowired
    AssetGroupFileRepository assetGroupFileRepository;

    @Autowired
    GalAssetsSynchronizedFileEntityRepository galAssetsSynchronizedFileEntityRepository;

    @Autowired
    GalAssetsSynchronizationEntityRepository galAssetsSynchronizationEntityRepo;

    @Value("${galsie.assetsync.page-size}")
    int pageSize;

    @Value("${galsie.assetsync.file-size}")
    long pageMaxFileSize;

    static final int SYNC_ALL_ASSETS_AFTER_LAST_SYNC_TIME = 1;

    static final int SYNC_ALL_UPDATED_ASSETS_CONTAINED_ASSET_LIST = 2;

    static final int SYNC_ALL_UPDATED_ASSETS_CONTAINED_ASSET_LIST_AFTER_LAST_SYNC_TIME = 3;

    @Transactional
    public GCSResponse<GalAssetsSyncResponseDTO> processAssetSynchronizationRequest(GalAssetsSyncRequestDTO syncRequestDTO) {
        var lastSyncTime = syncRequestDTO.getLastSyncTime();
        var assetsToSync = syncRequestDTO.getAssetsToSync();
        List<GalAssetsSynchronizedFileEntityCollection> collectionAssetSyncedEntities = new LinkedList<>();
        List<AssetGroupFileEntity> fileEntities = new LinkedList<>();
        int noOfFiles;
        int condition = getSyncRequestType(lastSyncTime, assetsToSync);
        switch (condition){
            default ->{ return GalAssetsSyncResponseDTO.responseError(GalAssetsSyncResponseErrorType.MUST_HAVE_FILES_OR_LAST_SYNC_TIME);}
            case SYNC_ALL_ASSETS_AFTER_LAST_SYNC_TIME-> fileEntities = assetGroupFileRepository.findAllByUpdatedAtAfter(lastSyncTime);
            case SYNC_ALL_UPDATED_ASSETS_CONTAINED_ASSET_LIST-> {
                var errorResponse = processRequestsWithAssetToSync(fileEntities, syncRequestDTO, false);
                if(errorResponse.isPresent()) return GalAssetsSyncResponseDTO.responseError(errorResponse.get());
            }
            case SYNC_ALL_UPDATED_ASSETS_CONTAINED_ASSET_LIST_AFTER_LAST_SYNC_TIME ->{
                var errorResponse = processRequestsWithAssetToSync( fileEntities, syncRequestDTO,  true);
                if(errorResponse.isPresent()) return GalAssetsSyncResponseDTO.responseError(errorResponse.get());
            }
        }
        var returnPair = populateCollectionAssetSyncedEntityList(collectionAssetSyncedEntities, fileEntities);
        noOfFiles = returnPair.getSecond();
        int pageNumbers  = collectionAssetSyncedEntities.size();
        Long syncId = null;
        for (GalAssetsSynchronizedFileEntityCollection galAssetsSynchronizedFileEntityCollection : collectionAssetSyncedEntities) {
            if (syncId == null) {
                syncId = galAssetsSynchronizedFileEntityCollection.getGalAssetsSynchronizedFileEntityList().get(0).getGalAssetsSynchronizationEntity().getId();
            }
            GCSResponse.saveEntitiesThrows(galAssetsSynchronizedFileEntityRepository, galAssetsSynchronizedFileEntityCollection.getGalAssetsSynchronizedFileEntityList());
        }
        return GalAssetsSyncResponseDTO.responseSuccess(syncId ,noOfFiles, pageNumbers);
    }

    /**
     * This method queries the database with the getSynchronizedPageRequestDTO variables and converts the result to a
     * FilesPageSyncResponseDTO
     * @param getSynchronizedPageRequestDTO
     * @return
     */
    public GCSResponse<GetSynchronizedPageResponseDTO> getSyncPage(GetSynchronizedPageRequestDTO getSynchronizedPageRequestDTO){
        var galAssetsSynchronizationEntityOpt = galAssetsSynchronizationEntityRepo.findById(getSynchronizedPageRequestDTO.getSyncId());
        if(galAssetsSynchronizationEntityOpt.isEmpty()){
            return GetSynchronizedPageResponseDTO.responseError(GetSynchronizedPageErrorType.INVALID_SYNC_ID);
        }
        var galAssetsSynchronizationEntity = galAssetsSynchronizationEntityOpt.get();
        var galAssetSynchronizedFileEntities = galAssetsSynchronizedFileEntityRepository
                .findAllByGalAssetsSynchronizationEntityAndPage(galAssetsSynchronizationEntity,
                        getSynchronizedPageRequestDTO.getPage() - 1);
        if(galAssetSynchronizedFileEntities.isEmpty()){
            return GetSynchronizedPageResponseDTO.responseError(GetSynchronizedPageErrorType.INVALID_PAGE_NUMBER);
        }
        var fileSyncedDataDTOList = galAssetSynchronizedFileEntities.stream().map(GalAssetsSynchronizedFileEntity
                ::toAssetFileSyncedDataDTO).toList();
        return GetSynchronizedPageResponseDTO.responseSuccess(fileSyncedDataDTOList);
    }

    /**
     * this method takes the in dbFileEntities and puts in assetGroupFileEntities
     * after checking if the version in the database is greater than the version provided
     * @param fileEntities
     * @param assetGroupFileListSyncRequestDTO
     * @param dbFileEntities
     */
    //Look into this
    private void getFinalListFromVersion(List<AssetGroupFileEntity> fileEntities, AssetGroupFileListSyncRequestDTO assetGroupFileListSyncRequestDTO, List<AssetGroupFileEntity> dbFileEntities) {
        Map<String, AssetGroupFileEntity> pathVersionMap = new HashMap<>();
        for(AssetGroupFileEntity assetGroupFileEntity: dbFileEntities){
            pathVersionMap.put(assetGroupFileEntity.getPath(), assetGroupFileEntity);
        }
        for(AssetGroupFileSyncRequestDTO assetGroupFileSyncRequestDTO : assetGroupFileListSyncRequestDTO.getFilesToSync()){
            var mappedAssetGroupFileEntity = pathVersionMap.get(assetGroupFileSyncRequestDTO.getPath().replaceAll("@[^\\/]+", ""));
            if( mappedAssetGroupFileEntity == null) {
                continue;
            }
            if(compareVersion(mappedAssetGroupFileEntity.getVersion(), assetGroupFileSyncRequestDTO.getVersion()) > 0){
                fileEntities.add(mappedAssetGroupFileEntity);
            }
        }
    }

    /**
     * We get assets after a certain time (if syncUsingTime was true) or all assets
     * We compare their versions with the versions provided in the DTO
     * Add to the list of assetGroupFileEntities the result.
     * @param assetsSyncRequest
     * @param assetGroupFileEntities
     * @param syncUsingTime to indicate if we want to use a different repository call when lastSyncTime is not null
     * @return
     */
    private Optional<GalAssetsSyncResponseErrorType> processRequestsWithAssetToSync(List<AssetGroupFileEntity> assetGroupFileEntities, GalAssetsSyncRequestDTO assetsSyncRequest, boolean syncUsingTime){
        var assetsToSync =  assetsSyncRequest.getAssetsToSync();
        for(AssetGroupFileListSyncRequestDTO assetGroupFileListSyncRequestDTO : assetsToSync){
                    var assetGroupType = assetGroupRepository.findByAssetGroupType(assetGroupFileListSyncRequestDTO.getAssetGroupType());
                    if(assetGroupType.isPresent()) {
                        var assetRequestPaths = assetGroupFileListSyncRequestDTO.getFilesToSync().stream()
                                .map(AssetGroupFileSyncRequestDTO::getPath).map(s-> s.replaceAll("@[^\\/]+", "")).toList();
                        List<AssetGroupFileEntity> dbFileEntities;
                        if(syncUsingTime){
                            dbFileEntities = assetGroupFileRepository
                                    .findAllByAssetGroupAndPathInAndUpdatedAtAfter(assetGroupType.get(), assetRequestPaths, assetsSyncRequest.getLastSyncTime());
                        }else{
                            dbFileEntities = assetGroupFileRepository.findAllByAssetGroupAndPathIn(assetGroupType.get(), assetRequestPaths);
                        }
                        getFinalListFromVersion(assetGroupFileEntities, assetGroupFileListSyncRequestDTO, dbFileEntities);
                    }
        }
        return Optional.empty();
    }

    /**
     * this method populates the collectionAssetSyncedEntities with AssetGroupFileEntities, making sure that every
     * GalAssetsSynchronizedFileEntityCollection has a total file size less than or equal to pageMaxFileSize and/or a total assetFileSyncedEntityList
     * size of less than or equal to pageSize
     * @param collectionAssetSyncedEntities
     * @param assetGroupFileEntities
     * @return an int indicating the total number of valid FilePageSyncDTO found
     * @throws Exception
     */
    private Pair<GalAssetsSyncResponseErrorType, Integer> populateCollectionAssetSyncedEntityList(List<GalAssetsSynchronizedFileEntityCollection> collectionAssetSyncedEntities, List<AssetGroupFileEntity> assetGroupFileEntities) {
        var galAssetsSynchronizationEntity = GalAssetsSynchronizationEntity.builder().build();
        GCSResponse.saveEntityThrows(galAssetsSynchronizationEntityRepo, galAssetsSynchronizationEntity);
        var collectionDTO =new GalAssetsSynchronizedFileEntityCollection();
        var currentSize = 0L;
        int noOfFiles = 0;
        for(AssetGroupFileEntity entity: assetGroupFileEntities){
            var assetGroupType = entity.getAssetGroup().getAssetGroupType();
            try {
                var loadedAssetGroup = LoadedAssetGroup.sharedInstance(assetGroupType, false);
                if (loadedAssetGroup == null) continue;
                var cleanPath = entity.getPath().replaceAll("@[^\\/]+", "");
                String pathActual = loadedAssetGroup.getActualPath(cleanPath);
                if (pathActual == null) continue;
                var actualPath = StringUtils.joinPaths(entity.getAssetGroup().getAssetGroupType().getPathInResources(), pathActual);//This represents the path to file on disk
                var fileSize = ResourceReaderUtils.getFileSize(actualPath);
                var loadedAssetFile = loadedAssetGroup.getLoadedFile(pathActual);
                if (loadedAssetFile.isEmpty()) continue;
                var tempSize = currentSize + fileSize;
                var collectionSize = collectionDTO.getGalAssetsSynchronizedFileEntityList().size();
                if (tempSize > pageMaxFileSize || collectionSize == pageSize) {
                    collectionAssetSyncedEntities.add(collectionDTO);
                    collectionDTO = new GalAssetsSynchronizedFileEntityCollection();
                    currentSize = 0L;
                }
                collectionDTO.addFilesPageSyncDTO(GalAssetsSynchronizedFileEntity.fromLoadedAssetFile(loadedAssetFile.get(),
                        entity.getAssetGroup().getAssetGroupType(), galAssetsSynchronizationEntity,
                        collectionAssetSyncedEntities.size(), cleanPath));
                currentSize += fileSize;
            }catch (Exception e){
                e.printStackTrace();
                continue;
            }
            noOfFiles++;
        }
        if(!collectionDTO.getGalAssetsSynchronizedFileEntityList().isEmpty()) collectionAssetSyncedEntities.add(collectionDTO);
        return Pair.ofSecond(noOfFiles);
    }

    /**
     * this method compares two version strings and returns an int indicating which one is greater
     * @param compare the database version of the asset
     * @param against the requested version of the asset
     * @return
     */
    private int compareVersion(String compare, String against){
        if(against == null || against.isEmpty()) return 1;
        String[] compareArray = compare.split("\\.");
        String[] againstArray = against.split("\\.");

        int minLength = Math.min(compareArray.length, againstArray.length);

        for (int i = 0; i < minLength; i++) {
            int num1 = Integer.parseInt(compareArray[i]);
            int num2 = Integer.parseInt(againstArray[i]);
            if (num1 < num2) {
                return -1;
            } else if (num1 > num2) {
                return 1;
            }
        }
        return Integer.compare(compareArray.length, againstArray.length);
    }

    /**
     * given the params lastSyncTime and assetsToSync, this method returns an int indicating the type of sync request we have
     * based on the agreed specs
     */
    private int getSyncRequestType(LocalDateTime lastSyncTime, List<AssetGroupFileListSyncRequestDTO> assetsToSync){
        if(lastSyncTime != null && (assetsToSync == null || assetsToSync.isEmpty()))return SYNC_ALL_ASSETS_AFTER_LAST_SYNC_TIME;
        if(lastSyncTime == null & assetsToSync !=null)return SYNC_ALL_UPDATED_ASSETS_CONTAINED_ASSET_LIST;
        if(lastSyncTime != null && assetsToSync != null) return SYNC_ALL_UPDATED_ASSETS_CONTAINED_ASSET_LIST_AFTER_LAST_SYNC_TIME;
        return 0;
    }

}
