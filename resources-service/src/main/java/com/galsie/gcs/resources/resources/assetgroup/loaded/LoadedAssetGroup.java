package com.galsie.gcs.resources.resources.assetgroup.loaded;


import com.galsie.gcs.microservicecommon.lib.galassets.core.AssetDataType;
import com.galsie.gcs.microservicecommon.lib.galassets.core.AssetGroupType;
import com.galsie.gcs.microservicecommon.lib.galassets.core.loaded.asset.LoadedAssetFile;
import com.galsie.gcs.resources.utils.ResourceReaderUtils;
import com.galsie.lib.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@Getter
@ToString
public class LoadedAssetGroup {

    private static final HashMap<AssetGroupType, LoadedAssetGroup> cachedLoadedGroups = new HashMap<>();

    private AssetGroupType assetGroupType;

    private Map<String, LoadedAssetFile> loadedFiles; // maps a path to its loaded file

    private Map<String, String> cleanToActualPathMap; // the path to a LoadedAssetFile is cleaned up before adding to an asset group, this may lead to issues get the actual files from the resources folder, so it is important to keep a map to the actual path in the resources folder

    private LocalDateTime lastUpdate = null; // would be set in AssetGroupBootstrap. It's the last time this group was updated

    private LoadedAssetGroup(AssetGroupType assetGroupType, Map<String, LoadedAssetFile> loadedFiles, Map<String, String> cleanToActualPathMap) {
        this.assetGroupType = assetGroupType;
        this.loadedFiles = loadedFiles;
        this.cleanToActualPathMap = cleanToActualPathMap;
    }

    public String[] getPaths() {
        return loadedFiles.keySet().toArray(new String[0]);
    }

    public Optional<LoadedAssetFile> getLoadedFile(String path) {
        String fixedPath = path.charAt(0) == '/' ? path.substring(1) : path;
        if (!path.contains(".")) { // doesn't have extension, search all
            for (String extension : Arrays.stream(assetGroupType.getDataTypes()).map(AssetDataType::getExtensionName).toList().toArray(new String[0])) {
                var file = getLoadedFile(fixedPath + "." + extension);
                if (file.isEmpty()) {
                    continue;
                }
                return file;
            }
        }
        return Optional.ofNullable(loadedFiles.get(fixedPath));
    }

    //get the actual path of file in the resources folder relative to its asset group using the clean path
    public String getActualPath(String cleanPath) {
        return cleanToActualPathMap.get(cleanPath);
    }

    public LoadedAssetFile getLoadedFileThrows(String path) throws Exception {
        var file = getLoadedFile(path);
        if (file.isEmpty()) {
            throw new Exception("The file '" + path + " couldn't be found in the asset group '" + this.assetGroupType.toString() + "'");
        }
        return file.get();
    }

    /**
    Must only be called by AssetGroupBootstrap
    */
    public void setLastUpdate(LocalDateTime time){
        this.lastUpdate = time;
    }
    /**
    The LoadedAssetGroup would have a null lastUpdateTime unless initially loaded by AssetGroupBootstrap, which would be the case.
     */
    public static LoadedAssetGroup sharedInstance(AssetGroupType groupType, boolean ignoreCache) throws Exception {
        if (!ignoreCache && cachedLoadedGroups.containsKey(groupType)) {
            return cachedLoadedGroups.get(groupType);
        }
        String path = groupType.getPathInResources();
        Map<String, LoadedAssetFile> loadedFiles = new HashMap<>();
        Map<String, String> cleanToActualPathMap = new HashMap<>();
        for (String p : ResourceReaderUtils.scanFiles(path, Arrays.stream(groupType.getDataTypes()).map(AssetDataType::getExtensionName).toList().toArray(new String[0]), true)) {
            try {
                var file = LoadedAssetFile.fromInputStream(p, ResourceReaderUtils.readInputStreamFromFile(StringUtils.joinPaths(path, p)), Arrays.stream(groupType.getDataTypes()).filter((a) -> p.endsWith(a.getExtensionName())).findFirst().get(), groupType.getVersioningType());
                loadedFiles.put(p, file);
                var cleanPath = p.replaceAll("@[^\\/]+", "");
                cleanToActualPathMap.put(cleanPath, p);
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("Couldn't create new instance: Failed to load LoadedAssetFile of path '" + p + "' reason - the previously printed exception.");
            }
        }
        var assetGroup = new LoadedAssetGroup(groupType, loadedFiles, cleanToActualPathMap);
        cachedLoadedGroups.put(groupType,assetGroup);
        return assetGroup;
    }

    public void setLoadedFiles(Map<String, LoadedAssetFile> loadedFiles){
        this.loadedFiles = loadedFiles;
    }
    /*
    Doesn't ignore cache
     */
    public static LoadedAssetGroup sharedInstance(AssetGroupType groupType) throws Exception {
       return sharedInstance(groupType, false);
    }

    public static void clearCache(){
        cachedLoadedGroups.clear();
    }

}