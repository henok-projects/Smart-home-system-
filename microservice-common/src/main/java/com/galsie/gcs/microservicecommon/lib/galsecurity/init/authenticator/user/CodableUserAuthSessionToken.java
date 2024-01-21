package com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.user;

import com.galsie.gcs.microservicecommon.lib.galsecurity.session.codabletoken.CodableAuthSessionToken;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Getter
public class CodableUserAuthSessionToken implements CodableAuthSessionToken {

    long sessionId;
    long userId;
    String username;

    public Map<String, Object> getTokenData(){
        var map = new HashMap<String, Object>();
        map.put("session_id", sessionId);
        map.put("user_id", userId);
        map.put("username", username);
        return map;
    }

    public static Optional<CodableUserAuthSessionToken> fromStringToken(String token){
        var claimsOpt = CodableAuthSessionToken.decodeToken(token);
        if (claimsOpt.isEmpty()) {
            return Optional.empty();
        }
        var claims = claimsOpt.get();
        Long sessionId = claims.get("session_id", Long.class);
        Long userId = claims.get("user_id", Long.class);
        var username = claims.get("username", String.class);
        if (sessionId == null || userId == null || username == null) {
            return Optional.empty();
        }
        return Optional.of(new CodableUserAuthSessionToken(sessionId, userId, username));
    }

}
