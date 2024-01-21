package com.galsie.gcs.microservicecommon.lib.gcsrequests.destination.microservice;

import com.galsie.gcs.microservicecommon.lib.gcsrequests.destination.RemoteRequestProtocolType;
import com.galsie.lib.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
@Getter
public enum GCSMicroservice {
    GCS_SENTRY("gcs-sentry"),
    RESOURCES("resources-service"),
    EMAIL("email-service"),
    USERS("users-service"),
    HOMES("homes-service"),
    HOMES_DATAWAREHOUSE("homes-datawarehouse-service"),
    SMART_DEVICES("smart-devices-service"),
    CONTINUOUS_SERVICE("continuous-service"),
    CERT_AUTHORITY("cert-authority-service"),
    TEST("test-service");

    private final String name;


    /**
     * Uses {GCSMicroservice#DEFAULT_PROTOCOL}
     * @param paths The request paths, with or without / between them
     * @return equest url in the form of protocol://microservice-name/path1/path2/path3
     */
    public String constructRequest(String... paths){
        return constructRequest(RemoteRequestProtocolType.DEFAULT_PROTOCOL, paths);
    }

    /**
     *
     * @param protocol http, https, idk..
     * @param paths The request paths, with or without / between them
     * @return request url in the form of protocol://microservice-name/path1/path2/path3
     */
    public String constructRequest(RemoteRequestProtocolType protocol, String... paths){
        return protocol.constructRequestPath(StringUtils.joinPathsWithStart(this.getName(), paths));
    }


    public static Optional<GCSMicroservice> fromName(String name){
        return Arrays.stream(GCSMicroservice.values()).filter(microservice -> microservice.getName().equals(name)).findFirst();
    }


}
