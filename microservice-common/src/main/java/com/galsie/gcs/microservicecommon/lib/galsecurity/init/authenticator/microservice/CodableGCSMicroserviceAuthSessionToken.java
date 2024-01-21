package com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.microservice;

import com.galsie.gcs.microservicecommon.lib.galsecurity.session.codabletoken.CodableAuthSessionToken;
import com.sun.istack.NotNull;
import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CodableGCSMicroserviceAuthSessionToken implements CodableAuthSessionToken {

    @NotNull
    long sessionId;

    @NotNull
    String serviceName;

    @Override
    public Map<String, Object> getTokenData() {
        var map = new HashMap<String, Object>();
        map.put("session_id", sessionId);
        map.put("service_name", serviceName);
        return map;
    }

    public static Optional<CodableGCSMicroserviceAuthSessionToken> fromStringToken(String token) {
        var claimsOpt = CodableAuthSessionToken.decodeToken(token);
        if (claimsOpt.isEmpty()) {
            System.out.println("failed to decode token");
            return Optional.empty();
        }
        var claims = claimsOpt.get();
        var sessionId = claims.get("session_id", Long.class);
        var serviceName = claims.get("service_name", String.class);
        if (sessionId == null || serviceName == null){
            System.out.println("no claims found in token");
            return Optional.empty();
        }
        return Optional.of(new CodableGCSMicroserviceAuthSessionToken(sessionId, serviceName));
    }
}
