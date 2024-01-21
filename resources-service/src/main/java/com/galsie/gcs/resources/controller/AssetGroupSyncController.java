package com.galsie.gcs.resources.controller;

import com.galsie.gcs.resources.service.assetgroup.AssetGroupGeneralService;
import com.galsie.gcs.resources.service.assetgroup.AssetGroupSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/assetGroupSync")
public class AssetGroupSyncController {

    @Autowired
    AssetGroupGeneralService assetGroupGeneralService;

    @Autowired
    AssetGroupSyncService assetGroupSyncService;

    @RequestMapping("/getLastUpdateInfo/{assetGroup}")
    public ResponseEntity<?> getLastUpdateInfo(@PathVariable(value="assetGroup") String assetGroup){
        return assetGroupGeneralService.getLastUpdateInfo(assetGroup).toResponseEntity();
    }
}
