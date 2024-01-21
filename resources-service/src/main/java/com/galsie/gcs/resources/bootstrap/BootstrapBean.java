package com.galsie.gcs.resources.bootstrap;

import com.galsie.gcs.resources.bootstrap.assetgroup.AssetGroupBootstrap;
import com.galsie.gcs.resources.bootstrap.assetgroup.countries.CountriesBootstrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BootstrapBean {

    @Autowired
    CountriesBootstrap countriesBootstrap;

    @Autowired
    AssetGroupBootstrap assetGroupBootstrap;


    public void bootstrap() throws Exception {
        countriesBootstrap.bootstrap();
        assetGroupBootstrap.bootstrap(true);
    }

}