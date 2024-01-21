package com.galsie.gcs.gcssentry.controller;

import com.galsie.gcs.gcssentry.service.apiclient.GCSAPIKeyGenerationService;
import com.galsie.gcs.gcssentry.data.dto.generate.request.GCSApiKeyGenerateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gcsApiKey")
public class GCSApiKeyController {

    @Autowired
    GCSAPIKeyGenerationService gcsapiKeyGenerationService;

    @PostMapping("/generate")
    public ResponseEntity<?> login(@RequestBody GCSApiKeyGenerateRequest gcsApiKeyGenerateRequest){
        return gcsapiKeyGenerationService.generateAPIClientKey(gcsApiKeyGenerateRequest).toResponseEntity();
    }

}
