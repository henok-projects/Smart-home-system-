package com.galsie.gcs.microservicecommon.lib.galsecurity;

import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSession;
import com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.user.UserSecurityAuthSession;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionGroup;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;

import java.util.Optional;

public class GalSecurityContextProvider {

    static Logger logger = LogManager.getLogger();

    public static SecurityContext getSecurityContext() {
        return SecurityContextHolder.getContext();
    }

    public static Optional<GalSecurityAuthSessionGroup> getContextualAuthenticationSessionGroup() {
        var authentication = getSecurityContext().getAuthentication();
        if (authentication == null) { /* may be null due to: developer trying to get context in an unauthenticated request OR authenticated request not setting the context in {@link GalSecurityAuthenticator#createAuthSession}*/
            return Optional.empty();
        }
        if (!(authentication instanceof GalSecurityAuthSessionGroup)) {
            logger.error("Couldn't get Contextual authentication session: " + authentication + " is not an instance of GalSecurityAuthSessionGroup.");
            return Optional.empty();
        }
        return Optional.of((GalSecurityAuthSessionGroup) authentication);
    }

    /**
     * Gets the User Auth Session from the security context
     * @return
     * - An Empty Optional if:
     *  - the contextual authentication session doesn't exist
     *  - The authentication session is not an instance of {@link UserSecurityAuthSession}
     * Otherwise returns a non-empty optional with the session
     */
    public static Optional<UserSecurityAuthSession> getContextualUserAuthSession() {
        var galSecurityAuthSessionGroupOpt = getContextualAuthenticationSessionGroup();
        if (galSecurityAuthSessionGroupOpt.isEmpty()) {
            return Optional.empty();
        }
        var galSecurityAuthSessionGroup = galSecurityAuthSessionGroupOpt.get();

        var galSecurityAuthSessionOpt =  galSecurityAuthSessionGroup.getGalSecurityAuthSessionFor(GalSecurityAuthSessionType.USER);
        if (galSecurityAuthSessionOpt.isEmpty()){
            return Optional.empty();
        }
        var galSecurityAuthSession = galSecurityAuthSessionOpt.get();
        if (!(galSecurityAuthSession instanceof UserSecurityAuthSession)) {
            return Optional.empty();
        }
        return Optional.of((UserSecurityAuthSession) galSecurityAuthSession);
    }
}
