package com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.user;

import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSession;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import lombok.Getter;

import java.util.HashMap;


@Getter
public class UserSecurityAuthSession extends GalSecurityAuthSession<Long> {
    public UserSecurityAuthSession(long userId, String token) {
        super(userId, token); // principle is username according to docs
    }


    public long getUserId(){
        return this.getEntityIdentifier();
    }

    @Override
    public GalSecurityAuthSessionType getSecurityAuthSessionType() {
        return GalSecurityAuthSessionType.USER;
    }

    @Override
    public HashMap<String, String> getHeaderParameters() {
        return GalSecurityAuthSession.toHeaderParameters(this.getSecurityAuthSessionType(), this.getToken());
    }
}
