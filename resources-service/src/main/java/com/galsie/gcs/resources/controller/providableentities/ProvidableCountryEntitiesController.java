package com.galsie.gcs.resources.controller.providableentities;

import com.galsie.gcs.resources.service.providableentities.ProvidableCountryEntitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/providableEntities/request/countries")
public class ProvidableCountryEntitiesController {

    @Autowired
    ProvidableCountryEntitiesService entityProviderService;

    @GetMapping("/all")
    public ResponseEntity<?> getCountries(){
        return entityProviderService.requestGetCountriesList().toResponseEntity();
    }

    @GetMapping("/getCountry/{id}")
    public ResponseEntity<?> getCountry(@PathVariable Long id){
        return entityProviderService.requestGetCountry(id).toResponseEntity();
    }

    @GetMapping("/getZone/{id}")
    public ResponseEntity<?> getZone(@PathVariable Long id){
        return entityProviderService.requestGetZone(id).toResponseEntity();
    }
}
