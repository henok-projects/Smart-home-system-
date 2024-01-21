package com.galsie.gcs.microservicecommon.lib.galsecurity.session.codabletoken.jwt;

import com.galsie.lib.utils.crypto.coder.Coder;
import com.galsie.lib.utils.crypto.coder.CodingAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Map;

public class DecodableToken {
    // Secret key - should be private and securely stored.
    // Do not expose it publicly.
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Coder.decode(CodingAlgorithm.BASE64, "YSBjYXJlZmVlIGxpZmVzdHlsZSBwb3dlcmVkIGJ5IEdhbHNpZQ==")); // TODO: Better key, store in application or somewhhre more secure

    public static String encodeData(Map<String, Object> claims){
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SECRET_KEY)
                .compact();
    }

    public static Claims decodeToken(String token) throws Exception{
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
