package com.galsie.gcs.homescommondata.bootstrap;

import com.galsie.gcs.microservicecommon.lib.gcsrequests.GCSRemoteRequests;
import com.galsie.gcs.microservicecommon.lib.gcsrequests.destination.microservice.GCSMicroservice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HomesCommonBootstrapBean implements InitializingBean {

    @Autowired
    GCSRemoteRequests gcsRemoteRequests;
    @Override
    public void afterPropertiesSet() throws Exception {
        log.warn("MODELS BOOTSTRAP WILL NOT RUN");
        /*var response = gcsRemoteRequests.initiateRequest(DataModelAssetProviderDTO.class)
                .destination(GCSMicroservice.RESOURCES, "/dataModelProvider", "/fetchModel")
                .httpMethod(HttpMethod.GET)
                .setMaxInMemorySize(5 * 1024 * 1024) // 5 MB
                .performRequestWithGCSResponse()
                .toGCSResponse();

        if(response.hasError()) {
            throw new Exception("BOOTSTRAP Failed: Couldn't get Data Model : " + response.getGcsError().getErrorType().name() + " " + response.getGcsError().getErrorMsg());
        }

        // Log the raw JSON response for debugging
        System.out.println("Received JSON: " + response.getResponseData());

        var dataModelAssetProviderDTO = response.getResponseData();
        modelBootstrapService.bootstrap(dataModelAssetProviderDTO.getMtrModel(), dataModelAssetProviderDTO.getGalModel())
        */
    }
}
