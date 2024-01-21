package com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.apiclient;

import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSession;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;

import java.util.HashMap;

public class GCSAPIClientSecurityAuthSession extends GalSecurityAuthSession<String> {

    public GCSAPIClientSecurityAuthSession(String apiClientDeviceName, String apiKey) {
        super(apiClientDeviceName, apiKey);
    }

    public String getAPIClientDeviceName(){
        return this.getEntityIdentifier();
    }

    @Override
    public GalSecurityAuthSessionType getSecurityAuthSessionType() {
        return GalSecurityAuthSessionType.GCS_API_CLIENT;
    }

    @Override
    public HashMap<String, String> getHeaderParameters() {
        return GalSecurityAuthSession.toHeaderParameters(this.getSecurityAuthSessionType(), this.getToken(), this.getAPIClientDeviceName());
    }
}
