package com.galsie.gcs.resources.controller.providableassetfiles;

import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.providableassetfile.get.request.GetProvidableAssetFileRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.request.AuthenticatedGCSRequest;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import com.galsie.gcs.resources.service.providableassetfile.ProvidableLoadedAssetFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/providableAssetFile")
public class ProvidableAssetFilesController {

    @Autowired
    ProvidableLoadedAssetFileService providableLoadedAssetFileService;

    @AuthenticatedGCSRequest(authSessionTypes = {GalSecurityAuthSessionType.GCS_MICROSERVICE, GalSecurityAuthSessionType.GCS_API_CLIENT})
    @GetMapping("/request/file")
    public ResponseEntity<?> getFile(@RequestBody GetProvidableAssetFileRequestDTO providableAssetDTORequestDTO) {
        return providableLoadedAssetFileService.getLoadedAssetFile(providableAssetDTORequestDTO).toResponseEntity();
    }

    @GetMapping("/request/base64LoadedAssetFile")
    public ResponseEntity<?> getFileAsString(@RequestBody GetProvidableAssetFileRequestDTO getProvidableAssetFileRequestDTO){
        return providableLoadedAssetFileService.getLoadedAssetFileAsString(getProvidableAssetFileRequestDTO).toResponseEntity();
    }


}
