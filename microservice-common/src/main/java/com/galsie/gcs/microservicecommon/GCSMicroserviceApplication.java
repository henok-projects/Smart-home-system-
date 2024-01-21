package com.galsie.gcs.microservicecommon;

import com.galsie.gcs.microservicecommon.config.componentscan.ComponentScanConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;

/**
 * This is a common abstract base class that serves as the entry point for all of Galsie's microservices.
 *
 * This class uses the @Import annotation to include the {@link ComponentScanConfiguration}, which configures
 * component scanning for the base packages of all microservices. This ensures a consistent setup across
 * all microservices by automatically registering all necessary components, entities, and repositories as
 * beans in the Spring application context.
 *
 * Any new microservice in the Galsie's Cloud Services should extend this class to inherit this standardized configuration.
 */
@Import(ComponentScanConfiguration.class)
@EnableEurekaClient
public abstract class GCSMicroserviceApplication {
}
