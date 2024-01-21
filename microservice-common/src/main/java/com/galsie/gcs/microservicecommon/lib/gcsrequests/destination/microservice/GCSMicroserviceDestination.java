package com.galsie.gcs.microservicecommon.lib.gcsrequests.destination.microservice;

import com.galsie.gcs.microservicecommon.lib.gcsrequests.destination.GCSRemoteRequestDestination;
import com.galsie.gcs.microservicecommon.lib.gcsrequests.destination.microservice.GCSMicroservice;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GCSMicroserviceDestination implements GCSRemoteRequestDestination {

    private GCSMicroservice gcsMicroservice;
    private String[] paths;

    public GCSMicroserviceDestination(GCSMicroservice gcsMicroservice, String... paths){
        this.gcsMicroservice = gcsMicroservice;
        this.paths = paths;
    }

    @Override
    public String getDestinationUri() {
        return gcsMicroservice.constructRequest(this.paths);
    }
}
