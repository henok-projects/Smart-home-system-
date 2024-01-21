package com.galsie.gcs.resources.service.assetgroup;

import com.galsie.gcs.microservicecommon.lib.galassets.core.AssetGroupType;
import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.assetsync.request.AssetGroupFileListSyncRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.assetsync.request.AssetGroupFileSyncRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.assetsync.request.GalAssetsSyncRequestDTO;
import com.galsie.gcs.resources.data.entity.assetgroup.AssetGroupFileEntity;
import com.galsie.gcs.resources.repository.assetgroup.AssetGroupFileRepository;
import com.galsie.gcs.resources.repository.assetgroup.AssetGroupRepository;
import com.galsie.gcs.resources.service.assetsync.GalAssetsSynchronizationService;
import com.galsie.lib.utils.pair.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class GalAssetsSynchronizationServiceTest {

    @Autowired
    GalAssetsSynchronizationService synchronizationService;

    @Autowired
    AssetGroupFileRepository assetGroupFileRepository;

    @Autowired
    AssetGroupRepository assetGroupRepository;

    int upVersioned = 0;

    Map<AssetGroupFileEntity, Pair<String, String>>  assetGroupFileToVersionChange = new HashMap<>();

    List<AssetGroupFileEntity> assetGroupFiles = new LinkedList<>();
    LocalDateTime earliestSyncTime = LocalDateTime.now().minusYears(10);
    LocalDateTime latestSyncTime = LocalDateTime.now();



    @BeforeEach
    void setUp(){
        assetGroupFiles  = assetGroupFileRepository.findAll();
        assetGroupFiles.forEach(entity ->{
            var updatedTime = entity.getUpdatedAt();
            if(updatedTime != null) {
                if (updatedTime.isBefore(earliestSyncTime)) earliestSyncTime = updatedTime;
                if (updatedTime.isAfter(latestSyncTime)) latestSyncTime = updatedTime;
            }
        });
    }

    @Test
    void requestSyncUpdateWithTimeGetAll() {
        var request  = GalAssetsSyncRequestDTO.builder().lastSyncTime(earliestSyncTime.minusDays(1)).build();
        var response = synchronizationService.processAssetSynchronizationRequest(request);
        assertNull(response.getResponseData().getGalAssetsSyncResponseErrorType());
        assertNotNull(response.getResponseData().getSyncInfo());
        assertEquals(assetGroupFiles.size(), response.getResponseData().getSyncInfo().getFilesToSyncCount());
    }

    @Test
    void requestSyncUpdateWithTimeGetNone(){
        var request  = GalAssetsSyncRequestDTO.builder().lastSyncTime(latestSyncTime).build();
        var response = synchronizationService.processAssetSynchronizationRequest(request);
        assertNull(response.getResponseData().getGalAssetsSyncResponseErrorType());
        assertNotNull(response.getResponseData().getSyncInfo());
        assertEquals(0, response.getResponseData().getSyncInfo().getFilesToSyncCount());
    }


    @Test
    void requestSyncWithAssetSyncOnly(){
        var processedFile = getMeAssets();
        Map<AssetGroupType, List<AssetGroupFileEntity>> map = new TreeMap<>();
        Map<AssetGroupType, List<AssetGroupFileEntity>> upVersionedMap = new TreeMap<>();
        processedFile.forEach(entity->{
            AssetGroupType  assetGroupType = entity.getAssetGroup().getAssetGroupType();
            if(map.containsKey(assetGroupType)){
                var list = map.get(assetGroupType);
                list.add(entity);
                map.put(assetGroupType, list);
            }else{
                List<AssetGroupFileEntity> temp = new LinkedList<>();
                temp.add(entity);
                map.put(entity.getAssetGroup().getAssetGroupType(), temp);
            }
        });
        var assetSyncList = getRequestList(map);
        var request = GalAssetsSyncRequestDTO.builder().assetsToSync(assetSyncList).build();
        upVersion(upVersionedMap);
        var response = synchronizationService.processAssetSynchronizationRequest(request);
        assertNull(response.getResponseData().getGalAssetsSyncResponseErrorType());
        assertNotNull(response.getResponseData().getSyncInfo());
        assertEquals(upVersioned, response.getResponseData().getSyncInfo().getFilesToSyncCount());
        for(var entry: assetGroupFileToVersionChange.entrySet()){
            entry.getKey().setVersion(entry.getValue().getSecond());
        }
        assetGroupFileRepository.saveAll(assetGroupFileToVersionChange.keySet());
    }

    void upVersion(Map<AssetGroupType, List<AssetGroupFileEntity>> map){
        upVersioned = 0;
        for(Map.Entry<AssetGroupType, List<AssetGroupFileEntity>> entry: map.entrySet()){
            for(AssetGroupFileEntity assetGroupFile: entry.getValue()) {
                int random = (int) (Math.random() * 10);
                if (random % 2 == 0) {
                    String previousVersion = assetGroupFile.getVersion();
                    var newVersion = (Integer.parseInt(previousVersion.substring(0,1)) + 1) + previousVersion.substring(1);
                    assetGroupFile.setVersion(newVersion);
                    assetGroupFileToVersionChange.put(assetGroupFile, Pair.of(newVersion, previousVersion));
                    upVersioned ++;
                }
            }
        }
        assetGroupFileRepository.saveAll(assetGroupFileToVersionChange.keySet());

    }

    Set<AssetGroupFileEntity> getMeAssets(){//get 20 random assets from the assetGroupFiles which is all the assets in the db
        Set<AssetGroupFileEntity> processedFile= new HashSet<>();
        for(int i=0; i< 20; i++){
            int rando = (int) (Math.random() * assetGroupFiles.size());
            processedFile.add(assetGroupFiles.get(rando));
        }
        return processedFile;
    }

    List<AssetGroupFileListSyncRequestDTO> getRequestList(Map<AssetGroupType, List<AssetGroupFileEntity>> map){
        List<AssetGroupFileListSyncRequestDTO> list = new LinkedList<>();
        for(Map.Entry<AssetGroupType, List<AssetGroupFileEntity>> entry: map.entrySet()){
            list.add(AssetGroupFileListSyncRequestDTO.builder().assetGroupType(entry.getKey()).filesToSync(getDTO(entry.getValue())).build());
        }
        return list;
    }

    List<AssetGroupFileSyncRequestDTO> getDTO(List<AssetGroupFileEntity> list){
        List<AssetGroupFileSyncRequestDTO> returnList = new LinkedList<>();
        for(AssetGroupFileEntity entity: list){
            returnList.add(AssetGroupFileSyncRequestDTO.builder().path(entity.getPath()).version(entity.getVersion()).build());
        }
        return returnList;
    }

}