package com.galsie.gcs.microservicecommon.lib.galassets;

import com.galsie.gcs.microservicecommon.lib.galassets.init.*;
import com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.ProvidableAssetDTOType;
import org.springframework.context.annotation.Bean;

import java.util.Set;

/**
 * This class is used to defines the configuration of a microservice that want to interface with resources services.
 * the service must override the methods defined in this class and return the list of DTOs it wants to subscribe to and
 * the ones required at start up
 */
public abstract class GCSGalAssetsConfiguration {

    /*
    * This method is used to get the list of DTOs that the microservice subscribes to
    * so the microservice that extends this class must define this method and return the list of DTOs
    * it wants to subscribe to
    * After a successful microservice login ThisMicroserviceLoggedInEvent is invoked and the listener calls methods
    * that use this method to get the list of DTOs to subscribe to
     */
    protected abstract Set<ProvidableAssetDTOType> getSubscribableProvidedAssetDTOTypes();

    /*
     * This method is used to get the list of DTOs that the microservice want to get at start up
     * so the microservice that extends this class must define this method and return the list of DTOs
     * it wants at start up
     * After a successful microservice login ThisMicroserviceLoggedInEvent is invoked and the listener calls methods
     * that use this method to get the list of DTOs that the microservice wants at start up
     * DTOs return in this must also be present in the list returned by getSubscribableProvidedAssetDTOTypes
     */
    protected abstract Set<ProvidableAssetDTOType> getStartUpProvidableAssetDTOTypes();


    @Bean
    public GCSGalAssetsProvidedDTOCacheService getGcsGalAssetsProvidedDTOCacheService() {
        return new GCSGalAssetsProvidedDTOCacheService();
    }

    @Bean
    public GCSGalAssetsConfigurationDataProvider getGcsGalAssetsConfigurationDataProvider() {
        return new GCSGalAssetsConfigurationDataProvider(this.getSubscribableProvidedAssetDTOTypes(), this.getStartUpProvidableAssetDTOTypes());
    }
    @Bean
    public GCSProvidableAssetDTOsConsumer getGcsProvidableAssetDTOsConsumer() {
        return new GCSProvidableAssetDTOsConsumer();
    }

    @Bean
    public ProvidableAssetDTOsStartupAndSubscriptionService getGcsStartupSubscriptionService() {
        return new ProvidableAssetDTOsStartupAndSubscriptionService();
    }

    @Bean
    public ProvidableAssetDTOsStartupAndSubscriptionHandler getGcsStartupSubscriptionHandler() {
        return new ProvidableAssetDTOsStartupAndSubscriptionHandler();
    }

}
