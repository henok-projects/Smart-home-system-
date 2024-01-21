package com.galsie.gcs.microservicecommon.lib.galsecurity;


import com.galsie.gcs.microservicecommon.lib.galsecurity.authenticator.GalSecurityAuthenticator;
import com.galsie.gcs.microservicecommon.lib.galsecurity.init.AuthenticatedGCSRequestAutoConfPathsProvider;
import com.galsie.gcs.microservicecommon.lib.galsecurity.init.GalSecurityCacheEnabledSessions;
import com.galsie.gcs.microservicecommon.lib.galsecurity.init.authenticator.GalSecurityAuthenticatorRabbitMQCacheUpdator;
import com.galsie.gcs.microservicecommon.lib.galsecurity.request.filters.CustomGalSecurityRequestFilter;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSession;
import com.galsie.gcs.microservicecommon.lib.galsecurity.session.GalSecurityAuthSessionType;
import com.galsie.gcs.microservicecommon.lib.galsecurity.sessionconfig.AuthSessionTypeConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.Map;

/**
 * <p>{@link GalSecurityConfiguration} is a parent class to a security configuration bean for any microservice that need to implement authentications to http requests and/or web sockets.
 * It defines overridable methods to:</p>
 * <p>- setup http security {@link GalSecurityConfiguration#setupHttpSecurity(HttpSecurity)}</p>
 * <p>- Get the needed necessary request filters by defining a new{@link CustomGalSecurityRequestFilter}</p>
 * <hr>
 * <p>It operates the configuration by defining a {@link SecurityFilterChain} Bean that calls {@link GalSecurityConfiguration#setupHttpSecurity(HttpSecurity)},
 * adds the filters, and builds the filter chain.</p>
 * <hr>
 * <p><b>Implementation</b></p>
 * <p>- A microservice should implement an @Configuration annotated class that inherits from {@link GalSecurityConfiguration} and implement the {@link GalSecurityConfiguration#getSessionTypeConfigurations()} ()} & {@link GalSecurityConfiguration#getCacheEnabledSessions()} methods</p>
 * <p>- Note: check out {@link CustomGalSecurityRequestFilter}, {@link GalSecurityAuthSession}, and {@link GalSecurityAuthenticator}</p>
 * <hr>
 * <p><b>Helpers</b></p>
 * {@link GalSecurityConfigurationWithCommonItems} is a subclass of {@link GalSecurityConfiguration} which defines helper methods for common configuration items.
 *
 **/
public abstract class GalSecurityConfiguration {

    @Autowired
    private PlatformTransactionManager transactionManager;

    /**
        Setup the security filter. Override if necessary.
        - 'continuous-service' overrides this for disabling basic http
     */
    public HttpSecurity setupHttpSecurity(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable() // disable basic http authentication filter
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests().anyRequest().permitAll() // authorize all requests
                .and()
                .csrf().disable();
    }

    public boolean isMicroserviceAutoLoginWithSentryEnabled(){
        return true;
    }

    /**
    Get the list of session types for which cache should be enabled
     <p><b>NOTE</b></p>
     - The session type itself may not support caching (for instance, if the session type was locally authenticated, the implementing microservice may not implement cache for that)
     */
    public abstract List<GalSecurityAuthSessionType> getCacheEnabledSessions();


    /**
     * Load the filter chain
     */
    @Bean
    public AuthenticatedGCSRequestAutoConfPathsProvider authenticatedGCSRequestAutoConfPathsProvider(){
        return new AuthenticatedGCSRequestAutoConfPathsProvider();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        var httpSecurity = setupHttpSecurity(http);
        httpSecurity.addFilterBefore(new CustomGalSecurityRequestFilter(getSessionTypeConfigurations(), authenticatedGCSRequestAutoConfPathsProvider()), BasicAuthenticationFilter.class);
        return httpSecurity.build();
    }

    /**
     * return a map of {@link GalSecurityAuthSessionType} to {@link AuthSessionTypeConfiguration} to specify how each
     * session type will be authenticated, remote or local, if the session uses autoconfiguration and a list of manually
     * configured paths(Optional)
     * @return
     */
    public abstract Map<GalSecurityAuthSessionType, AuthSessionTypeConfiguration> getSessionTypeConfigurations();

    @Bean
    public GalSecurityCacheEnabledSessions galSecurityCacheEnabledSessions(){
        return new GalSecurityCacheEnabledSessions(this.getCacheEnabledSessions());
    }
    @Bean
    public GalSecurityAuthenticatorRabbitMQCacheUpdator galSecurityAuthenticatorRabbitMQCacheUpdator(){
        return new GalSecurityAuthenticatorRabbitMQCacheUpdator();
    }
}
