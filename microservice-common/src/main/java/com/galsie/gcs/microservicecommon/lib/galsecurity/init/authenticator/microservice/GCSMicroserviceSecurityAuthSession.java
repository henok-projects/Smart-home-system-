package com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.microservice;

import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSession;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;

import java.util.HashMap;

public class GCSMicroserviceSecurityAuthSession extends GalSecurityAuthSession<String> {

    public GCSMicroserviceSecurityAuthSession(String microserviceName, String token) {
        super(microserviceName, token);
    }

    public String getMicroserviceName(){
        return this.getEntityIdentifier();
    }

    @Override
    public GalSecurityAuthSessionType getSecurityAuthSessionType() {
        return GalSecurityAuthSessionType.GCS_MICROSERVICE;
    }

    @Override
    public HashMap<String, String> getHeaderParameters() {
        return GalSecurityAuthSession.toHeaderParameters(this.getSecurityAuthSessionType(), this.getToken());
    }
}
