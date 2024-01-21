package com.galsie.gcs.microservicecommon.lib.galsecurity.request;

import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticatedGCSRequestObject {

    private AuthenticationStrategy authenticationStrategy;
    private GalSecurityAuthSessionType[] authSessionTypes;

    public AuthenticatedGCSRequestObject(GalSecurityAuthSessionType[] authSessionTypes) {
        this.authenticationStrategy = AuthenticationStrategy.AND;
        this.authSessionTypes = authSessionTypes;
    }
}
