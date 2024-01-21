package com.galsie.gcs.resources.service.providableassetdtos;

import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.ProvidableAssetDTOType;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.ProvidedAssetDTO;
import com.galsie.gcs.resources.resources.assetgroup.loaded.LoadedAssetGroup;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class ProvidableAssetDTOsProvider {

    private Map<ProvidableAssetDTOType, ProvidedAssetDTO> loadedProvidedAssetDTOs = new HashMap<>();

    @Getter
    private Set<ProvidableAssetDTOType> versionChangedDTOTypes = new HashSet<>();

    public void loadProvidedAssetDTO(ProvidableAssetDTOType type, boolean versionChanged) {
        LoadedAssetGroup loadedAssetGroup;
        try {
            loadedAssetGroup = LoadedAssetGroup.sharedInstance(type.getAssetGroupType());
        } catch (Exception e) {
            log.error("Failed to loadProvidedAssetDTO for type: " + type +" because there was an error loading asset group: ");
            return;
        }
        var loadedAssetFileOpt  = loadedAssetGroup.getLoadedFile(type.getPathRelativeToAssetGroupType());
        if (loadedAssetFileOpt.isEmpty()) {
            log.error("ProvidableAssetDTOsProvider: loadProvidedAssetDTO: loadedAssetFileOpt.isEmpty() for type: " + type);
            return;
        }
        var loadedAssetFile = loadedAssetFileOpt.get();
        try {
            var providedAssetDTO = loadedAssetFile.toDTO(type.getProvidedAssetDTOClassType());
            loadedProvidedAssetDTOs.put(type, providedAssetDTO);
            versionChangedDTOTypes.add(type);

        }catch ( Exception e ){
            log.error("ProvidableAssetDTOsProvider: loadProvidedAssetDTO: ProvidedAssetDTONotMappableFromAssetFileException for type: " + type);
        }
    }

    public Optional<ProvidedAssetDTO> getProvidedAssetDTO(ProvidableAssetDTOType type){
        return Optional.ofNullable(loadedProvidedAssetDTOs.get(type));
    }

    public void clearVersionChangedProvidedAssetDTOTypes(){
        versionChangedDTOTypes.clear();
    }


}
