package com.galsie.gcs.resources.bootstrap.assetgroup.countries;

import com.galsie.gcs.microservicecommon.lib.galassets.core.AssetGroupType;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.galassets.core.loaded.asset.LoadedXmlAssetFile;
import com.galsie.gcs.resources.data.entity.countries.*;
import com.galsie.gcs.resources.repository.countries.*;
import com.galsie.gcs.resources.resources.assetgroup.loaded.LoadedAssetGroup;
import lombok.extern.java.Log;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Element;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;

@Component
@Log
public class CountriesBootstrap {

    @Autowired
    RegionRepository regionRepository;

    @Autowired
    CityRepository cityRepo;

    @Autowired
    CountryRepository countryRepo;

    @Autowired
    ZoneRepository zoneRepo;

    @Autowired
    PhoneCodeRepository phoneCodeRepository;

    @Transactional
    public void bootstrap() throws Exception {
        if (countryRepo.count() > 0){ // Only bootstrap if there are no countries
            log.info("Countries already bootstrapped");
            return;
        }
        LoadedAssetGroup assetGroup = LoadedAssetGroup.sharedInstance(AssetGroupType.COUNTRIES);
        var file = assetGroup.getLoadedFile("countries_states_cities");
        if (file.isEmpty()){
            throw new Exception("Couldn't bootstrap: find file countries_states_cities");
        }
        Document doc = ((LoadedXmlAssetFile) file.get()).getDocument();
        var countries = doc.getElementsByTag("country_state_city");
        createCountries(countries);
    }

    private Optional<String> getLoweredFromElement(Element node, String name){
        var els = node.getElementsByTagName(name);
        if (els.getLength() == 0){
            return Optional.empty();
        }
        return Optional.ofNullable(els.item(0).getTextContent().toLowerCase(Locale.ROOT));
    }

    private Optional<String> getLoweredFromElement(org.jsoup.nodes.Element node, String name){
        var els = node.getElementsByTag(name);
        if (els.isEmpty()){
            return Optional.empty();
        }
        return Optional.ofNullable(els.get(0)).map(el -> el.html().toLowerCase(Locale.ROOT));
    }

    private RegionEntity getOrCreateRegion(String regionName){
        Optional<RegionEntity> regionOpt = regionRepository.findByName(regionName);
        if(regionOpt.isPresent()){
            return regionOpt.get();
        }
        var region = RegionEntity.builder().name(regionName).build();
        var response = GCSResponse.saveEntity(regionRepository, region);
        return response.getResponseData();
    }

    @Transactional
    public void createCountries(List<org.jsoup.nodes.Element> countries) throws Exception {
        for (org.jsoup.nodes.Element country : countries) {
            if (country.html().isEmpty()) {
                continue;
            }
            var region = getLoweredFromElement(country, "region").get();
            var countryName = getLoweredFromElement(country, "name").get();
            var iso3 = getLoweredFromElement(country, "iso3").get();
            var iso2 = getLoweredFromElement(country, "iso2").get();
            var phoneCodes = getLoweredFromElement(country, "phone_code").get();
            if(phoneCodes.isEmpty()){
                throw new Exception(String.format("no phone codes present for country: %s", countryName));
            }
            var countryEntity = CountryEntity.builder().name(countryName).iso2Code(iso2).iso3Code(iso3)
                    .region(getOrCreateRegion(region)).build();
            var response = GCSResponse.saveEntityThrows(countryRepo, countryEntity);

            if(response.hasResponseData()) {
                createPhoneCodes(phoneCodes, response.getResponseData());
                var zones = country.getElementsByTag("states");
                createZones(response.getResponseData(), zones);
            }else{
                log.log(Level.WARNING, String.format("Country %s could not be persisted to database", countryName));
            }
        }
    }
    private void createZones(CountryEntity country, Elements nodes){
        for (org.jsoup.nodes.Element node : nodes) {
            if(node.html().isEmpty()) continue;
            var zoneName = getLoweredFromElement(node, "name").get();
            var zoneCode = getLoweredFromElement(node, "state_code").get();
            var zoneEntity = ZoneEntity.builder().country(country).zoneCode(zoneCode).name(zoneName).build();
            var response = GCSResponse.saveEntity(zoneRepo, zoneEntity);
            if (response.hasResponseData()) {
                var cities = node.getElementsByTag("cities");
                createCities(zoneEntity, cities);
            } else log.info(String.format("Cities for Country %s not persisted to database", country.getName()));
        }
    }

    private void createCities(ZoneEntity zone, Elements nodes){
        List<CityEntity> cityEntityList = new LinkedList<>();
        for (org.jsoup.nodes.Element node : nodes) {
            if (node.html().isEmpty()) continue;
//            if (node.childNodeSize() > 0) continue;
            var cityName = getLoweredFromElement(node, "name").get();
            var cityLong = getLoweredFromElement(node, "longitude").get();
            var cityLat = getLoweredFromElement(node, "latitude").get();
            var cityEntity = CityEntity.builder().zone(zone).name(cityName).longitude(Double.parseDouble(cityLong)).latitude(Double.parseDouble(cityLat)).build();
            cityEntityList.add(cityEntity);
        }
        var response = GCSResponse.saveEntities(cityRepo, cityEntityList);
        if(response.hasError())log.info(String.format("Cities for Zone %s not persisted to database", zone.getName()));
    }


    private void createPhoneCodes(String phoneCodes, CountryEntity countryEntity){
        String[] phoneCodesArray = phoneCodes.split("and");
        for (String phoneCode:phoneCodesArray){
            phoneCode = phoneCode.replaceAll("[-+\"*\\s]", "").toLowerCase(Locale.ROOT).trim();
            var phoneCodeEntity = PhoneCodeEntity.builder().code(Integer.parseInt(phoneCode)).country(countryEntity).build();
            var response = GCSResponse.saveEntity(phoneCodeRepository, phoneCodeEntity);
            if(response.hasError()) log.info(String.format("Phone codes for %s not persisted to database", countryEntity.getName()));
        }
    }


}
