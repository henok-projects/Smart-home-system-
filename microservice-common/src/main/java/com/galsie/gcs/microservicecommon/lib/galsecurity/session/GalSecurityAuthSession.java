package com.galsie.gcs.microservicecommon.lib.galsecurity.session;

import java.util.HashMap;

public abstract class GalSecurityAuthSession<T> {
    private T entityIdentifier;
    private  String token;
    public GalSecurityAuthSession(T entityIdentifier, String token){
        this.entityIdentifier = entityIdentifier;
        this.token = token;
    }

    public abstract GalSecurityAuthSessionType getSecurityAuthSessionType();

    public T getEntityIdentifier(){
        return this.entityIdentifier;
    }

    public String getToken(){
        return this.token;
    }

    public abstract HashMap<String, String> getHeaderParameters();

    public static HashMap<String, String> toHeaderParameters(GalSecurityAuthSessionType galSecurityAuthSessionType, String... values){
        HashMap<String, String> map = new HashMap<>();
        var tokenParams = galSecurityAuthSessionType.getTokenHeaderParams();
        for (int i = 0; i < Math.min(tokenParams.length, values.length); i++){
            map.put(tokenParams[i], values[i]);
        }
        return map;
    }
}
