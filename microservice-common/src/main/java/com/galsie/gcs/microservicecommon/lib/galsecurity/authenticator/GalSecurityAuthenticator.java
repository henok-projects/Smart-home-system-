package com.galsie.gcs.microservicecommon.lib.galsecurity.authenticator;

import com.galsie.gcs.microservicecommon.lib.galsecurity.GalSecurityContextProvider;
import com.galsie.gcs.microservicecommon.lib.galsecurity.authenticator.cache.GalSecurityAuthenticatorCacheManager;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.lastaccess.AuthSessionLastAccessUpdateDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.lastaccess.AuthSessionLastAccessUpdateResponseDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSession;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionGroup;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.activesessions.ActiveSessionDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.activesessions.ActiveSessionListDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.request.AuthenticationRequestDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.response.AuthenticationResponseDTO;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.response.AuthenticationResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.rabbitmq.GCSRabbitTemplate;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


/**
 * Associated with a {@link com.galsie.gcs.microservicecommon.lib.galsecurity.request.filters.GalSecurityRequestsFilter}
 */
@Getter
@Slf4j
public abstract class GalSecurityAuthenticator {

    @Autowired
    GalSecurityAuthenticatorCacheManager galSecurityAuthenticatorCacheManager;

    public abstract GalSecurityAuthSessionType galSecurityAuthSessionType();

    public abstract Optional<? extends AuthenticationRequestDTO> getAuthenticationRequestDTO(HttpServletRequest httpServletRequest);

    public abstract GCSResponse<? extends AuthenticationResponseDTO> performAuthentication(AuthenticationRequestDTO authenticationRequestDTO);

    /**
     * NOTE: Remote authenticators can returns {@link com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponseErrorType#NOT_SUPPORTED}
     * @param authSessionLastAccessUpdateDTO
     * @return
     */
    public abstract GCSResponse<AuthSessionLastAccessUpdateResponseDTO> receiveAuthSessionLastAccessUpdate(AuthSessionLastAccessUpdateDTO authSessionLastAccessUpdateDTO);
    /**
     * This method is responsible for created the {@link GalSecurityAuthSession}
     * - It takes in the request DTO & the response DTO (since the request DTO contains the access token which should be passed into the session)
     *
     * NOTE:  After this is done, it must be added to the {@link GalSecurityAuthSessionGroup} that exists in {@link org.springframework.security.core.context.SecurityContext} so that it can be conveniently fetched through {@link GalSecurityContextProvider#getSecurityContext()}
     */
    public abstract Optional<? extends GalSecurityAuthSession<?>> createAuthSession(AuthenticationRequestDTO authenticationRequestDTO, AuthenticationResponseDTO authenticationResponseDTO);


    public <idType> Optional<ActiveSessionListDTO<idType>> getCachedSessionList(idType id){
        var cacheOpt = galSecurityAuthenticatorCacheManager.getSessionListCacheFor(galSecurityAuthSessionType());
        if (cacheOpt.isEmpty()){
            return Optional.empty();
        }
        return cacheOpt.get().getActiveSessionListFor(id);
    }

    /**
     * IF Not in cache, returns an empty optional
     * If No matching session in cache, returns an empty optional
     * If session Expired, returns an error
     * If session valid, returns success
     * If session invalid due to validity time ending, returns an empty optional (since the validity of a token may be extended)
     * @param id The id identfying the items cache
     * @param token The token to get the specific session matching this token
     * @return
     */
    public <idType>  Optional<GCSResponse<AuthenticationResponseDTO>> getSessionListCacheAuthenticationResponseFor(idType id, String token, GCSResponseErrorType expiredResponseType){
        var activeSessionListOpt = getCachedSessionList(id);
        if (activeSessionListOpt.isEmpty()){
            return Optional.empty();
        }
        var matchingSessionOpt = activeSessionListOpt.get().getWithTokenMatching(token);
        if (matchingSessionOpt.isEmpty()){
            return Optional.empty();
        }
        return getAuthenticationResponseForActiveSessionDTO(matchingSessionOpt.get(), expiredResponseType);
    }


    /**
     * If Expired, returns an error
     * If valid, returns success
     * If invalid (due to not being expired), returns an empty optional (since the validity of a token may be extended)
     * @param activeSessionDTO The active sesion dto for which the authentication response DTO needs to be gotten
     * @return
     */
    public static Optional<GCSResponse<AuthenticationResponseDTO>> getAuthenticationResponseForActiveSessionDTO(ActiveSessionDTO activeSessionDTO, GCSResponseErrorType expiredResponseType){
        if (activeSessionDTO.isForceExpired()){
            return Optional.of(AuthenticationResponseDTO.responseError(expiredResponseType));
        }
        if (!activeSessionDTO.isExpired()){
            return Optional.of(AuthenticationResponseDTO.responseSuccess());
        }
        return Optional.empty(); // If it's invalid and not force expired, we don't know if its actually invalid and not expired unless its the local authenticator
    }
}
