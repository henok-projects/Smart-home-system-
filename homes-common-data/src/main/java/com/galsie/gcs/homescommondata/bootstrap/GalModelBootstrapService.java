package com.galsie.gcs.homescommondata.bootstrap;

import com.galsie.gcs.homescommondata.bootstrap.galmodel.CategoriesBootststrapService;
import com.galsie.gcs.homescommondata.bootstrap.galmodel.DeviceTypesBootstrapService;
import com.galsie.gcs.homescommondata.bootstrap.galmodel.DiverseGroupTypesBootstrapService;
import com.galsie.lib.datamodel.galsie.GalModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GalModelBootstrapService {

    @Autowired
    CategoriesBootststrapService categoriesBootststrapService;

    @Autowired
    DeviceTypesBootstrapService deviceTypesBootstrapService;

    @Autowired
    DiverseGroupTypesBootstrapService diverseGroupTypesBootstrapService;


    public void bootstrap(GalModel model) throws Exception{
        categoriesBootststrapService.bootstrap(model.getCategoryTypesList());
        deviceTypesBootstrapService.bootstrap(model.getDeviceTypes().getNormalDeviceTypes()); // Bootstrap normal device types. Abstract device types aren't bootstrapped, rather their possible feature types & mtr device types are appended to the devide type which extends them
        diverseGroupTypesBootstrapService.bootstrap(model.getDiverseGroups());
    }
}
