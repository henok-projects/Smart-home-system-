package com.galsie.gcs.certauthority.config.certs;

import com.galsie.lib.certificates.SecurityProvider;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.context.annotation.Configuration;

import java.security.Security;

@Configuration
public class SecurityProviderConfiguration {

    static {
        for (SecurityProvider securityProvider: SecurityProvider.values()) {
            try {
                securityProvider.registerProvider();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
