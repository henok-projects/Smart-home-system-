package com.galsie.gcs.resources.service.providableentities;

import com.galsie.gcs.microservicecommon.lib.galassets.data.discrete.providableentities.ProvidableEntityResponseErrorType;
import com.galsie.gcs.resources.data.entity.countries.CountryEntity;
import com.galsie.gcs.resources.repository.countries.CityRepository;
import com.galsie.gcs.resources.repository.countries.CountryRepository;
import com.galsie.gcs.resources.repository.countries.ZoneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class ProvidableCountryEntitiesServiceTest {

    @Autowired
    CountryRepository countryRepo;

    @Autowired
    ZoneRepository zoneRepo;

    @Autowired
    CityRepository cityRepository;

    @Autowired
    ProvidableCountryEntitiesService entityProviderService;

    List<CountryEntity> countries = new ArrayList<>();
    @BeforeEach
    void setUp() {
        countries = countryRepo.findAll();
    }

    @Test
    void getCountriesList() {
        var response =  entityProviderService.requestGetCountriesList();
        var responseData = response.getResponseData();
        assertNull(responseData.getProvidableEntityError());
        var countryDTOsList = responseData.getCountriesEntityDTO();
        assertEquals(countries.size(), countryDTOsList.getCountries().size());
    }

    @Test
    void getCountryWithGoodRequest() {
        var index = (int) (Math.random() * countries.size()-1);
        var country = countries.get(index);
        var response = entityProviderService.requestGetCountry(country.getId());
        var responseData = response.getResponseData();
        assertNull(responseData.getProvidableEntityError());
        var countryDTO = responseData.getCountry();
        assertEquals(country.getName(), countryDTO.getName());
    }

    @Test
    void getCountryWithBadRequest() {
        var id = countries.get(countries.size() - 1).getId() + 1;
        var response = entityProviderService.requestGetCountry(id);
        var responseData = response.getResponseData();
        assertNotNull(responseData.getProvidableEntityError());
        assertEquals(ProvidableEntityResponseErrorType.ENTITY_NOT_FOUND, responseData.getProvidableEntityError());
    }

    @Test
    void getZoneWithGoodRequest() {
        CountryEntity country = null;
        System.out.println(countries.size());
        for(int i=0; i <countries.size(); i++) {
            System.out.println(countries.get(i).getZones().size());
            if (!countries.get(i).getZones().isEmpty()) {
                country = countries.get(i);
                System.out.println(country != null);
                break;
            }
        }

        var zoneId = (int) (Math.random() * country.getZones().size()-1);
        var zone = country.getZones().get(zoneId);
        var response = entityProviderService.requestGetZone(zone.getId());
        var responseData = response.getResponseData();
        assertNull(responseData.getProvidableEntityError());
        var zoneDTO = responseData.getZone();
        assertEquals(zone.getName(), zoneDTO.getName());
        assertEquals(zone.getCities().size(), zoneDTO.getCities().size());
    }
}