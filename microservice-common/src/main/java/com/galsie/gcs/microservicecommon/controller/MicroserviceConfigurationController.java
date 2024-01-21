package com.galsie.gcs.microservicecommon.controller;

import com.galsie.gcs.microservicecommon.service.MicroserviceConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/serviceConfig")
public class MicroserviceConfigurationController {

    @Autowired
    MicroserviceConfigurationService microserviceConfigurationService;

    /**
     * Gets the local version info for this microservice. Local version is the same as that in application.yml
     * @return
     */
    @RequestMapping("/getLocalVersionInfo")
    public ResponseEntity<?> getLocalVersionInfo(){
        return microserviceConfigurationService.getLocalServiceVersionInfo().toResponseEntity();
    }


    /**
     * Gets the global version info for this microservice type. Global version is the latest version for this microservice type
     *
     * Note: When a microservice starts, it checks its version against the Global version and accordingly:
     *  - Updates the global version (if the microservice has a greater version than the one stored globally, or if there exists no global version)
     *  - Stops the microservice (if the the microservice has a lower version than the one stored globally, and the version is required).
     *      - If the version isn't required, the microservice doesn't stop, rather shows a warning
     * @return
     */
    @RequestMapping("/getGlobalVersionInfo")
    public ResponseEntity<?> getGlobalVersionInfo(){
        return microserviceConfigurationService.getGlobalServiceVersionInfo().toResponseEntity();
    }

}
