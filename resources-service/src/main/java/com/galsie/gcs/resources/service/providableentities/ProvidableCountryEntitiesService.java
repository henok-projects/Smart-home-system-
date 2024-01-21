package com.galsie.gcs.resources.service.providableentities;

import com.galsie.gcs.microservicecommon.lib.galassets.data.discrete.providableentities.ProvidableEntityResponseErrorType;
import com.galsie.gcs.microservicecommon.lib.galassets.providableentities.countries.CountriesProvidableEntityDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.providableentities.countries.response.GetCountriesListResponseDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.providableentities.countries.response.GetCountryResponseDTO;
import com.galsie.gcs.microservicecommon.lib.galassets.providableentities.countries.response.GetZoneResponseDTO;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.resources.data.dto.providiablecountriesdtos.CountryEntityBasicInfoExtDTO;
import com.galsie.gcs.resources.data.dto.providiablecountriesdtos.CountryEntityDetailedInfoExtDTO;
import com.galsie.gcs.resources.data.dto.providiablecountriesdtos.ZoneEntityDetailedInfoExtDTO;
import com.galsie.gcs.resources.repository.countries.CityRepository;
import com.galsie.gcs.resources.repository.countries.CountryRepository;
import com.galsie.gcs.resources.repository.countries.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProvidableCountryEntitiesService {

    @Autowired
    CountryRepository countryRepo;

    @Autowired
    ZoneRepository zoneRepo;

    @Autowired
    CityRepository cityRepo;


    public GCSResponse<GetCountriesListResponseDTO> requestGetCountriesList(){
        try {
            var mappedDBEntries = countryRepo.findAll().stream().map(CountryEntityBasicInfoExtDTO::toBasicCountryDTO).toList();
            var providableEntityDTO = new CountriesProvidableEntityDTO(mappedDBEntries);
            return GetCountriesListResponseDTO.responseSuccess(providableEntityDTO);
        }catch (Exception e) {
            return GetCountriesListResponseDTO.responseError(ProvidableEntityResponseErrorType.MAPPING_TO_DTO_FAILED);
        }
    }

    public GCSResponse<GetCountryResponseDTO> requestGetCountry(Long id){
        var countryOpt = countryRepo.findById(id);
        if(countryOpt.isEmpty()){
            return GetCountryResponseDTO.responseError(ProvidableEntityResponseErrorType.ENTITY_NOT_FOUND);
        }
        var country = countryOpt.get();
        return GetCountryResponseDTO.responseSuccess(CountryEntityDetailedInfoExtDTO.toDetailedCountryDTO(country, zoneRepo.findAllByCountry(country)));
    }

    public GCSResponse<GetZoneResponseDTO> requestGetZone(Long id) {
        var zoneOpt = zoneRepo.findById(id);
        if(zoneOpt.isEmpty()){
            return GetZoneResponseDTO.responseError(ProvidableEntityResponseErrorType.ENTITY_NOT_FOUND);
        }
        var zone = zoneOpt.get();
        return GetZoneResponseDTO.responseSuccess(ZoneEntityDetailedInfoExtDTO.toDetailedZoneDTO(zone, cityRepo.findAllByZone(zone)));
    }

}
