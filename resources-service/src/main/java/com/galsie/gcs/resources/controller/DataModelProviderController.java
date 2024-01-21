package com.galsie.gcs.resources.controller;


import com.galsie.gcs.microservicecommon.lib.galassets.core.AssetGroupType;
import com.galsie.gcs.resources.service.assetgroup.AssetGroupGeneralService;
import com.galsie.gcs.resources.service.assetgroup.DataModelProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dataModelProvider")
public class DataModelProviderController {

    @Autowired
    DataModelProviderService dataModelProviderService;

    @Autowired
    AssetGroupGeneralService assetGroupGeneralService;

    @GetMapping("/fetchModel")
    public ResponseEntity<?> fetchDataModel(){
        return dataModelProviderService.getDataModel().toResponseEntity();
    }
    @GetMapping("/getLastUpdateInfo")
    public ResponseEntity<?> getLastUpdateInfo(){
        return assetGroupGeneralService.getLastUpdateInfo(AssetGroupType.DATA_MODEL).toResponseEntity();
    }


}
