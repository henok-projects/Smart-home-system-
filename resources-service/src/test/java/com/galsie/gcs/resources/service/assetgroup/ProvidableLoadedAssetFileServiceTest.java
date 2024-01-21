package com.galsie.gcs.resources.service.assetgroup;

import com.galsie.gcs.microservicecommon.lib.galassets.core.AssetGroupType;
import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.providableassetdto.general.get.request.GetProvidableAssetDTORequestDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.providableassetfile.get.request.GetProvidableAssetFileRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.ProvidableAssetDTOType;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset.areaconfig.AreaConfigurationProvidedAssetDTO;
import com.galsie.gcs.resources.resources.assetgroup.loaded.LoadedAssetGroup;
import com.galsie.gcs.resources.service.assetgroup.mock.discrete.MockProvidableAssetDTOType;
import com.galsie.gcs.resources.service.assetgroup.mock.dto.request.MockProvidableAssetDTORequestDTOGet;
import com.galsie.gcs.resources.service.providableassetdtos.MicroserviceProvidableAssetDTOsService;
import com.galsie.gcs.resources.service.providableassetfile.ProvidableLoadedAssetFileService;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static com.galsie.gcs.microservicecommon.lib.galassets.data.discrete.ProvidableAssetResponseErrorType.ASSET_FILE_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class ProvidableLoadedAssetFileServiceTest {

    @Autowired
    private ProvidableLoadedAssetFileService providableLoadedAssetFileService;

    @Autowired
    MicroserviceProvidableAssetDTOsService microserviceProvidableAssetDTOsService;

    Map<AssetGroupType, List<String>> assetMap = new HashMap<>();
    GetProvidableAssetFileRequestDTO providableAssetDTORequestDTO = GetProvidableAssetFileRequestDTO.builder()
                .assetGroupType(AssetGroupType.COUNTRIES).path("").includeVersion(true).build();

    @Before
    void setUp() throws Exception {
        Map<AssetGroupType, List<String>> assetMap = new HashMap<>();
        int i=0;
        for(AssetGroupType groupType: AssetGroupType.values()){
            LoadedAssetGroup assetGroup = LoadedAssetGroup.sharedInstance(groupType, false);
            if(i == 20) {break;}
            assetMap.put(groupType , new ArrayList<>());
            for(String path: assetGroup.getPaths()){
                if(i == 20) {break;}
                List<String> paths = assetMap.get(groupType);
                paths.add(path);
                assetMap.put(groupType, paths);
                i++;
            }
        }
    }

    @Test
    void getLoadedAssetFileBadPath() {
        for(Map.Entry<AssetGroupType, List<String>> entry: assetMap.entrySet()){
            providableAssetDTORequestDTO.setAssetGroupType(entry.getKey());
            for(String path: entry.getValue()) {
                path += "test";
                providableAssetDTORequestDTO.setPath(path);
                var response = providableLoadedAssetFileService.getLoadedAssetFile(providableAssetDTORequestDTO);
                var responseError = response.getResponseData().getProvidableAssetResponseError();
                Assertions.assertEquals(ASSET_FILE_NOT_FOUND, responseError );
            }
        }
    }

    @Test
    void getLoadedAssetFileGoodPaths(){
        for(Map.Entry<AssetGroupType, List<String>> entry: assetMap.entrySet()){
            providableAssetDTORequestDTO.setAssetGroupType(entry.getKey());
            for(String path: entry.getValue()) {
                providableAssetDTORequestDTO.setPath(path);
                var responseFile = providableLoadedAssetFileService.getLoadedAssetFile(providableAssetDTORequestDTO);
                assertNull(responseFile.getResponseData().getProvidableAssetResponseError());
                assertNotNull(responseFile.getResponseData().getLoadedAssetFile());
            }
        }
    }

    @Test
    void getProvidableAssetEncodedStringBadPaths(){
        for(Map.Entry<AssetGroupType, List<String>> entry: assetMap.entrySet()){
            providableAssetDTORequestDTO.setAssetGroupType(entry.getKey());
            for(String path: entry.getValue()) {
                path += "test";
                providableAssetDTORequestDTO.setPath(path);
                var responseString = providableLoadedAssetFileService.getLoadedAssetFileAsString(providableAssetDTORequestDTO);
                var errorResponse = responseString.getResponseData().getProvidableAssetResponseError();
                Assertions.assertEquals(ASSET_FILE_NOT_FOUND, errorResponse );
            }
        }
    }

    @Test
    void getProvidableAssetEncodedStringGoodPaths(){
        for(Map.Entry<AssetGroupType, List<String>> entry: assetMap.entrySet()){
            providableAssetDTORequestDTO.setAssetGroupType(entry.getKey());
            for(String path: entry.getValue()) {
                providableAssetDTORequestDTO.setPath(path);
                var responseString = providableLoadedAssetFileService.getLoadedAssetFileAsString(providableAssetDTORequestDTO);
                assertNull(responseString.getResponseData().getProvidableAssetResponseError());
                assertNotNull(responseString.getResponseData().getBase64EncodedString());
            }
        }
    }

    @Test
    void badProvidableAssetDTOType() {
//        var providableAssetDTORequestDTO = new ProvidableAssetDTORequestDTOGet
//                ("SIZE_CONTENT");
//        var response = assetsProviderService.getLoadedAssetDTO(providableAssetDTORequestDTO);
//        var errorResponse =response.getResponseData().getProvidableAssetResponseError();
//        assertEquals(DTO_NOT_FOUND, errorResponse);
//        assertNull(response.getResponseData().getProvidedAssetDTO());
        //test no longer works because you ask me to change the constructor from String to ProvidableAssetDTOType
    }

    @Test
    void invalidPathInProvidableAssetType() {
        var providableAssetDTORequestDTO = new MockProvidableAssetDTORequestDTOGet
                (MockProvidableAssetDTOType.AREA_CONFIGURATION_BAD_PATH.name());
        var response = microserviceProvidableAssetDTOsService.getLoadedAssetDTO(providableAssetDTORequestDTO);
        var errorResponse =response.getResponseData().getProvidableAssetResponseError();
        assertEquals(ASSET_FILE_NOT_FOUND, errorResponse);
        assertNull(response.getResponseData().getProvidedAssetDTO());
    }

    @Test
    void successfulAssetDTORequest() {
        var providableAssetDTORequestDTO = new GetProvidableAssetDTORequestDTO
                (ProvidableAssetDTOType.AREA_CONFIGURATION_CONTENT);
        var response = microserviceProvidableAssetDTOsService.getLoadedAssetDTO(providableAssetDTORequestDTO);
        var errorResponse = response.getResponseData().getProvidableAssetResponseError();
        assertNull(errorResponse);

        var responseData = response.getResponseData().getProvidedAssetDTO();
        assertInstanceOf(AreaConfigurationProvidedAssetDTO.class, responseData);
    }

//    @Test
//    void testRequestExistingAssetFilesSucceeds(){
//        var providableAssetDTORequestDTO = new MockProvidableAssetDTORequestDTOGet
//                (auxRandomizeText(MockProvidableAssetDTOType.BOOK_CONTENT.name()));
//        var response = microserviceProvidableAssetDTOsService.getLoadedAssetDTO(providableAssetDTORequestDTO);
//        var errorResponse =response.getResponseData().getProvidableAssetResponseError();
//        assertNull(errorResponse);
//
//        var responseData = response.getResponseData().getProvidedAssetDTO();
//        assertInstanceOf(BookAssetDTO.class, responseData);
//
//        providableAssetDTORequestDTO = new MockProvidableAssetDTORequestDTOGet("person_CONTENT");
//        response = microserviceProvidableAssetDTOsService.getLoadedAssetDTO(providableAssetDTORequestDTO);
//        errorResponse =response.getResponseData().getProvidableAssetResponseError();
//        assertNull(errorResponse);
//
//        responseData = response.getResponseData().getProvidedAssetDTO();
//        assertInstanceOf(PersonAssetDTO.class, responseData);
//    }

    private String auxRandomizeText(String word){
        String[] wordArray = word.split("");
        for(int i=0; i< wordArray.length; i++){
            int calc = (int) (Math.random() * 10);
            if(calc%2==0){
                wordArray[i] = wordArray[i].toLowerCase(Locale.ROOT);
            }
        }
        return String.join("", wordArray);
    }
}