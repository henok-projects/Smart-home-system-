package com.galsie.gcs.resources.bootstrap.assetgroup;

import com.galsie.gcs.microservicecommon.lib.galassets.core.AssetGroupType;
import com.galsie.gcs.microservicecommon.lib.galassets.core.loaded.asset.LoadedAssetFile;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.resources.data.entity.assetgroup.AssetGroupEntity;
import com.galsie.gcs.resources.data.entity.assetgroup.AssetGroupFileEntity;
import com.galsie.gcs.resources.event.LoadedAssetGroupBootStrapDoneEvent;
import com.galsie.gcs.resources.event.SingleLoadedAssetFileBootStrapDoneEvent;
import com.galsie.gcs.resources.event.SingleLoadedAssetGroupBootstrapDoneEvent;
import com.galsie.gcs.resources.repository.assetgroup.AssetGroupFileRepository;
import com.galsie.gcs.resources.repository.assetgroup.AssetGroupRepository;
import com.galsie.gcs.resources.repository.sync.providableassetdtos.MicroserviceSubscribedProvidableAssetDTOEntityRepository;
import com.galsie.gcs.resources.resources.assetgroup.loaded.LoadedAssetGroup;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.GCSEventManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse.*;

/**
 * Bootstraps an AssetGroup
 * -
 */
@Service
public class AssetGroupBootstrap {
    private final static Logger logger = LogManager.getLogger();
    @Autowired
    AssetGroupRepository assetGroupRepository;

    @Autowired
    AssetGroupFileRepository assetGroupFileRepository;

    @Autowired
    MicroserviceSubscribedProvidableAssetDTOEntityRepository microserviceSubscribedProvidableAssetDTOEntityRepository;

    @Autowired
    GCSEventManager eventManager;

    @Transactional
    public void bootstrap(boolean ignoreCache) {
        var anyFileVersionChanged = false;
        for (AssetGroupType assetGroupType: AssetGroupType.values()){
            try {
                boolean changed = bootstrap(LoadedAssetGroup.sharedInstance(assetGroupType, ignoreCache));
                 logger.info("A file has changed in " + assetGroupType.name());
                eventManager.callEvent(new SingleLoadedAssetGroupBootstrapDoneEvent(assetGroupType, changed));
                anyFileVersionChanged |= changed;

            } catch (Exception ex) {
                logger.error("Couldn't bootstrap AssetGroupType." + assetGroupType.name() + " reason: " + ex.getLocalizedMessage());
            }
        }
        eventManager.callEvent(new LoadedAssetGroupBootStrapDoneEvent(anyFileVersionChanged));
    }

    private boolean bootstrap(LoadedAssetGroup loadedAssetGroup) throws Exception{
        var assetGroupEntOpt = assetGroupRepository.findByAssetGroupType(loadedAssetGroup.getAssetGroupType());
        if (assetGroupEntOpt.isEmpty()){
            insertNewAssetGroup(loadedAssetGroup); // would return a non-empty optional or throw an exception
//            return;
        }
        var assetGroupEnt = assetGroupEntOpt.get();
        var storedFiles = assetGroupEnt.getAssetGroupFiles();
        var storedPaths = storedFiles.stream().collect(Collectors.toMap(AssetGroupFileEntity::getPath, (fileEnt) -> fileEnt));//possible error
        var loadedAssetFiles = loadedAssetGroup.getLoadedFiles();
        var storedAssetFileEntitiesToDelete = new ArrayList<>(storedPaths.values()); // would delete any stored path that is not in assetFiles
        boolean versionChanged = false;
        for(String loadedPath: loadedAssetFiles.keySet()){
            LoadedAssetFile assetFile = loadedAssetFiles.get(loadedPath);
            if (storedPaths.containsKey(loadedPath)){
                // if the loadedPath was found in the stored paths, update that stored path.
                var storedFileEntity = storedPaths.get(loadedPath);
                var changed = updateAssetGroupFile(storedFileEntity, assetFile);
                eventManager.callEvent(new SingleLoadedAssetFileBootStrapDoneEvent(loadedAssetGroup.getAssetGroupType(), loadedPath, changed));
                versionChanged |= changed; // if any of these returned true, the versionChanged becomes true
                storedAssetFileEntitiesToDelete.remove(storedFileEntity); // remove it from the storedAssetFileEntitiesToDelete, as it the loadedPath is found in storedPaths
                continue;
            }
            // if it is not found in storedPaths, add it
            addAssetGroupFileFor(assetGroupEnt, loadedPath, assetFile.getVersion());

            versionChanged = true; // definitely version changed if added a new file
            eventManager.callEvent(new SingleLoadedAssetFileBootStrapDoneEvent(loadedAssetGroup.getAssetGroupType(), loadedPath, true));
        }
        versionChanged |= !storedAssetFileEntitiesToDelete.isEmpty(); // if storedPaths isn't empty, versionChanged would be true

        var deletion = removeEntitiesThrows(assetGroupFileRepository, storedAssetFileEntitiesToDelete);
        if (deletion.hasError()){
            throw new Exception("Failed to remove the entities storedAssetFileEntitiesToDelete, reason: " + deletion.getGcsError().getErrorMsg());
        }

        var lastUpdate = LocalDateTime.now();
        if (versionChanged){
            assetGroupEnt.setLastUpdate(lastUpdate);
            assetGroupEnt.setLastUpdateRequired(true);
        }

        var insertion =  saveEntityThrows(assetGroupRepository, assetGroupEnt);
        if (insertion.hasError()){
            throw new Exception("Failed to insert assetGroupEnt reason:" + insertion.getGcsError().getErrorMsg());
        }
        // only set after it has been successfully saved
        loadedAssetGroup.setLastUpdate(assetGroupEnt.getLastUpdate());
        return versionChanged;
    }

    /*
    Updates the version if its different
     */
    private boolean updateAssetGroupFile(AssetGroupFileEntity assetGroupFileEntity, LoadedAssetFile loadedAssetFile) throws Exception{
        if (assetGroupFileEntity.getVersion() == null){
            throw new Exception("Asset group file entity at " + assetGroupFileEntity.getPath() + " has a nil version");
        }
        if (assetGroupFileEntity.getVersion().equals(loadedAssetFile.getVersion())){

            return false;
        }
        assetGroupFileEntity.setVersion(loadedAssetFile.getVersion());
        return true;
    }

    private void addAssetGroupFileFor(AssetGroupEntity assetGroupEnt, String path, String version) throws Exception{

        if (version == null){
            throw new Exception("Failed to insert assetGroupEnt reason: Version is Null");
        }
        var insertion = saveEntityThrows(assetGroupFileRepository, getNewAssetGroupFileEntityFor(assetGroupEnt, path, version));
        if (insertion.hasError()){
            throw new Exception("Failed to insert assetGroupEnt reason:" + insertion.getGcsError().getErrorMsg());
        }
        insertion.getResponseData();
    }

    /*
     Note: lastUpdateRequired always set to true TODO: maybe change
     */
    private Optional<AssetGroupEntity> insertNewAssetGroup(LoadedAssetGroup loadedAssetGroup) throws Exception {
        var datetime = LocalDateTime.now();
        GCSResponse<AssetGroupEntity> insertion =  saveEntityThrows(assetGroupRepository, AssetGroupEntity.builder().lastUpdate(datetime).lastUpdateRequired(true).assetGroupType(loadedAssetGroup.getAssetGroupType()).build());
        if (insertion.hasError()){
            throw new Exception("Failed to insert assetGroupEnt reason:" + insertion.getGcsError().getErrorMsg());
        }
        AssetGroupEntity assetGroupEnt = insertion.getResponseData();
        var filesInsertion = saveEntitiesThrows(assetGroupFileRepository, getNewAssetGroupFileEntitiesFor(assetGroupEnt, loadedAssetGroup));
        if (filesInsertion.hasError()){
            throw new Exception("Error inserting assetGroupFiles into the repository: " + filesInsertion.getGcsError().getErrorMsg());
        }
        return insertion.getResponseDataOpt();
    }
    private List<AssetGroupFileEntity> getNewAssetGroupFileEntitiesFor(AssetGroupEntity assetGroupEntity, LoadedAssetGroup loadedAssetGroup) throws Exception{
        var assetFiles = loadedAssetGroup.getLoadedFiles();
        List<AssetGroupFileEntity> assetGroupFileEntities = new ArrayList<>();
        for (String path: assetFiles.keySet()){
            assetGroupFileEntities.add(getNewAssetGroupFileEntityFor(assetGroupEntity, path,  assetFiles.get(path).getVersion()));
        }
        return assetGroupFileEntities;
    }

    private AssetGroupFileEntity getNewAssetGroupFileEntityFor(AssetGroupEntity assetGroupEntity, String path, String version) throws Exception{
        path = path.replaceAll("@[^\\/]+","");
        if (version == null){
            throw new Exception("Failed to insert assetGroupEnt reason: Version is Null");
        }
        return AssetGroupFileEntity.builder().path(path).version(version).assetGroup(assetGroupEntity).build();
    }


}