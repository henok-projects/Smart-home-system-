package com.galsie.gcs.microservicecommon.lib.galsecurity.session;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A {@link GalSecurityAuthSessionGroup} holds a group of {@link GalSecurityAuthSession} objects since some requests may require multiple authentications.
 * <p>
 * It is a subclass of {@link Authentication} so that the session can be set in the {@link org.springframework.security.core.context.SecurityContext}.
 * <p>
 * Note: We ignore all the methods inherited as a result of being a subclass of {@link Authentication}.
 */
public class GalSecurityAuthSessionGroup implements Authentication {

    String uuid = UUID.randomUUID().toString();
    /*
        Store for a session type, the session
        - If no session exists for that type, store nothing
     */
    private HashMap<GalSecurityAuthSessionType, GalSecurityAuthSession<?>> galSecurityAuthSessions;

    /**
     * Constructs a new {@link GalSecurityAuthSessionGroup} with the provided map of {@link GalSecurityAuthSession} objects.
     *
     * @param galSecurityAuthSessions a map containing the session type as the key and the corresponding session object as the value
     */
    public GalSecurityAuthSessionGroup(Map<GalSecurityAuthSessionType, GalSecurityAuthSession<?>> galSecurityAuthSessions) {
        this.galSecurityAuthSessions = new HashMap<>(galSecurityAuthSessions);
    }

    /**
     * Constructs a new {@link GalSecurityAuthSessionGroup} with the provided array of {@link GalSecurityAuthSession} objects.
     *
     * @param securityAuthSessions an array of session objects to be included in the session group
     */
    public GalSecurityAuthSessionGroup(GalSecurityAuthSession<?>... securityAuthSessions) {
        this(Arrays.stream(securityAuthSessions).collect(Collectors.toMap(GalSecurityAuthSession::getSecurityAuthSessionType, (session) -> session)));
    }

    /**
     * Constructs a new empty {@link GalSecurityAuthSessionGroup}.
     */
    public GalSecurityAuthSessionGroup() {
        this(new HashMap<>());
    }

    /**
     * Puts a {@link GalSecurityAuthSession} in the session group
     * - If a session with the same type already exists, it is overridden
     *
     * @param galSecurityAuthSession the session object to be put
     */
    public void putGalSecurityAuthSession(GalSecurityAuthSession<?> galSecurityAuthSession) {
        this.galSecurityAuthSessions.put(galSecurityAuthSession.getSecurityAuthSessionType(), galSecurityAuthSession);
    }

    /**
     * Retrieves the {@link GalSecurityAuthSession} object for the specified session type, if it exists.
     *
     * @param authSessionType the type of the session to retrieve
     * @return an optional containing the session object, or an empty optional if no session exists for the given type
     */
    public Optional<GalSecurityAuthSession<?>> getGalSecurityAuthSessionFor(GalSecurityAuthSessionType authSessionType) {
        return Optional.ofNullable(galSecurityAuthSessions.get(authSessionType));
    }

    /*
    These methods serve no purpose in a session group, we ignore them
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.uuid;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return null;
    }
}
