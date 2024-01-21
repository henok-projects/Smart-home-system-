package com.galsie.gcs.microservicecommon.lib.gcsevents.init;

import com.galsie.gcs.microservicecommon.lib.gcsevents.GCSGlobalEventListener;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.GCSEventListener;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.events.GCSEventManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.annotation.PostConstruct;

/**
 * NOTE: {@link com.galsie.gcs.microservicecommon.config.componentscan.ComponentScanConfiguration} is configured to load all {@link GCSGlobalEventListener} annotated classes as beans
 */
public class GCSGlobalEventListenerAutoConfig implements BeanPostProcessor {

    @Autowired
    GCSEventManager gcsGlobalEventManager;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(!bean.getClass().isAnnotationPresent(GCSGlobalEventListener.class)) {
            return bean;
        }
        if (!(bean instanceof GCSEventListener gcsEventListener)) {
            throw new IllegalStateException("A GCSGlobalEventListener annotated class must conform to " + GCSEventListener.class.getName());
        }
        gcsGlobalEventManager.registerListener(gcsEventListener);
        return bean;
    }
}
