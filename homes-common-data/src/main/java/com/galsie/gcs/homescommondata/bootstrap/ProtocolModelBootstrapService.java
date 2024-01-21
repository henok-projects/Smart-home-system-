package com.galsie.gcs.homescommondata.bootstrap;

import com.galsie.lib.datamodel.galsie.GalModel;
import com.galsie.lib.datamodel.matter.MTRModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProtocolModelBootstrapService {

    @Autowired
    MTRModelBootstrapService mtrModelBootstrapService;
    @Autowired
    GalModelBootstrapService galModelBootstrapService;

    public void bootstrap(MTRModel mtrModel, GalModel galModel) throws Exception{
        mtrModelBootstrapService.bootstrap(mtrModel);
        galModelBootstrapService.bootstrap(galModel);
    }

}
