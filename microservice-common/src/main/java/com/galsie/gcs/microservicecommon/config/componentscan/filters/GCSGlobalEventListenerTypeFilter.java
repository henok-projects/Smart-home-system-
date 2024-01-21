package com.galsie.gcs.microservicecommon.config.componentscan.filters;

import com.galsie.gcs.microservicecommon.lib.gcsevents.GCSGlobalEventListener;
import org.springframework.core.type.filter.AnnotationTypeFilter;

public class GCSGlobalEventListenerTypeFilter extends AnnotationTypeFilter {
    public GCSGlobalEventListenerTypeFilter() {
        super(GCSGlobalEventListener.class);
    }
}