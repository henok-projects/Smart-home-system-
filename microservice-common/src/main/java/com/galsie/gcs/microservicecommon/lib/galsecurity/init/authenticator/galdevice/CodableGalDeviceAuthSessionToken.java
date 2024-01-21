package com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.galdevice;

import com.galsie.gcs.microservicecommon.lib.galsecurity.session.codabletoken.CodableAuthSessionToken;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Getter
public class CodableGalDeviceAuthSessionToken implements CodableAuthSessionToken {

    final long sessionId;
    final String serialNumber;

    @Override
    public Map<String, Object> getTokenData() {
        var map = new HashMap<String, Object>();
        map.put("session_id", sessionId);
        map.put("serial_number", serialNumber);
        return map;
    }
    public static Optional<CodableGalDeviceAuthSessionToken> fromStringToken(String token) {
        var claimsOpt = CodableAuthSessionToken.decodeToken(token);
        if (claimsOpt.isEmpty()){
            return Optional.empty();
        }
        var claims = claimsOpt.get();

        var sessionId = claims.get("session_id", Long.class);
        var serialNumber = claims.get("serial_number", String.class);
        if (sessionId == null || serialNumber == null){
            return Optional.empty();
        }
        return Optional.of(new CodableGalDeviceAuthSessionToken(sessionId, serialNumber));
    }
}
