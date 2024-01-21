package com.galsie.gcs.resources.controller.assetsync;

import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.assetsync.request.GalAssetsSyncRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.assetsync.request.GetSynchronizedPageRequestDTO;
import com.galsie.gcs.resources.service.assetsync.GalAssetsSynchronizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/assetsSync")
public class GalAssetsSynchronizationController {

    @Autowired
    GalAssetsSynchronizationService galAssetsSynchronizationService;

    @PostMapping("/request")
    public ResponseEntity<?> processSync(@RequestBody GalAssetsSyncRequestDTO galAssetsSyncRequestDTO){
        return galAssetsSynchronizationService.processAssetSynchronizationRequest(galAssetsSyncRequestDTO).toResponseEntity();
    }

    @PostMapping("/getFiles")
    public ResponseEntity<?> getSyncedPage(@RequestBody GetSynchronizedPageRequestDTO getSynchronizedPageRequestDTO){
        return galAssetsSynchronizationService.getSyncPage(getSynchronizedPageRequestDTO).toResponseEntity();
    }

}
