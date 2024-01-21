package com.galsie.gcs.microservicecommon.config.componentscan;

import com.galsie.gcs.microservicecommon.config.componentscan.filters.GCSGlobalEventListenerTypeFilter;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan(basePackages = {"com.galsie.gcs"},
            includeFilters = @ComponentScan.Filter(type = FilterType.CUSTOM, classes = GCSGlobalEventListenerTypeFilter.class)) // All GCSGlobalEventListener annotated classess will be initiated as means
@EntityScan(basePackages = {"com.galsie.gcs"} )
@EnableJpaRepositories(basePackages = {"com.galsie.gcs"})
@EnableScheduling
public class ComponentScanConfiguration {
}
