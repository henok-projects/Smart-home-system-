package com.galsie.gcs.microservicecommon.lib.galsecurity.session.codabletoken;

import com.galsie.gcs.microservicecommon.lib.galsecurity.session.codabletoken.jwt.DecodableToken;
import io.jsonwebtoken.Claims;

import java.util.Map;
import java.util.Optional;

/**
 * {@link CodableAuthSessionToken} is used to
 */
public interface CodableAuthSessionToken {

    /**
     * Auxiliary method that encodes the session data (BUT DOES NOT produce a token)
     * - Method must join the parameters thorough the TOKEN_PARAM_SEPARATOR
     */
    Map<String, Object> getTokenData();

    default String toStringToken(){
        return DecodableToken.encodeData(this.getTokenData());
    }

    static Optional<Claims> decodeToken(String token){
        try {
            return Optional.ofNullable(DecodableToken.decodeToken(token));
        }catch (Exception ex){
            ex.printStackTrace();
            return Optional.empty();
        }
    }
}
