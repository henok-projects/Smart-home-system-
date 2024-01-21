package com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.galdevice;

import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSession;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;

import java.util.HashMap;

public class GalDeviceSecurityAuthSession extends GalSecurityAuthSession<String> {

    public GalDeviceSecurityAuthSession(String serialNum, String token) {
        super(serialNum, token);
    }

    /**
     *
     * @return the devices serial number
     */
    public String getSerialNumber(){
        return this.getEntityIdentifier();
    }

    @Override
    public GalSecurityAuthSessionType getSecurityAuthSessionType() {
        return GalSecurityAuthSessionType.GALDEVICE;
    }

    @Override
    public HashMap<String, String> getHeaderParameters() {
        return GalSecurityAuthSession.toHeaderParameters(this.getSecurityAuthSessionType(), this.getToken());
    }
}
