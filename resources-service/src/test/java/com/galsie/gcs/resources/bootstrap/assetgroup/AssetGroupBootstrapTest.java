package com.galsie.gcs.resources.bootstrap.assetgroup;

//@SpringBootTest
class AssetGroupBootstrapTest {
//
//
//    @Autowired
//    GCSRemoteRequests gcsRemoteRequests;
//
//    @Autowired
//    AssetGroupBootstrap assetGroupBootstrap;
//
//    @Autowired
//    MicroserviceProvidableAssetDTOsService providableAssetDTOsService;
//
//    @Autowired
//    MicroserviceProvidableAssetDTOsSubscriptionService subscriptionService;
//
//    @Autowired
//    MicroserviceSubscribedProvidableAssetDTOEntityRepository microserviceSubscribedProvidableAssetDTOEntityRepository;
//
//
//
//    @Test
//    void bootstrapTestCheckingThatIfFileVersionChangedUpdateDTOIsBroadcast() throws Exception {
//
//        setUp(".500");
//
//        var returned = editFileAndBootStrap("GalAssets/app_content/area/area_config.json");
//
//        assertEquals(1, returned.size());
//
//    }
//
//    /**
//     * To make run this test make sure homes, test and resource service are running
//     * @throws Exception
//     */
//    @Test
//    void assetBootstrapTestToCheckIfSubscribedServicesGetNotifiedWhenDTOHasNewVersion() throws Exception {
//        setUp(".0");
//
//        //Test that test-service does not have this item in cache
//        var gcsResponse = gcsRemoteRequests.initiateRequest(boolean.class)
//                .destination(GCSMicroservice.TEST, "checkMicroserviceCache", "dto")
//                .httpMethod(HttpMethod.GET)
//                .setRequestPayload(ProvidableAssetDTOType.COUNTRY_CODE_MODEL)
//                .performRequestWithGCSResponse()
//                .toGCSResponse();
//        assertFalse(gcsResponse.hasError());
//        var responseData = gcsResponse.getResponseData();
//        assertFalse(responseData);
//
//        //Test that homes-service does not have this item in cache
//        gcsResponse = gcsRemoteRequests.initiateRequest(boolean.class)
//                .destination(GCSMicroservice.HOMES, "checkMicroserviceCache", "dto")
//                .httpMethod(HttpMethod.GET)
//                .setRequestPayload(ProvidableAssetDTOType.COUNTRY_CODE_MODEL)
//                .performRequestWithGCSResponse()
//                .toGCSResponse();
//        assertFalse(gcsResponse.hasError());
//        responseData = gcsResponse.getResponseData();
//        assertFalse(responseData);
//
//        editFileAndBootStrap("GalAssets/app_content/signup/countryCodesListModel.json");
//
//        //Test that test-service does not have this item in cache
//        gcsResponse = gcsRemoteRequests.initiateRequest(boolean.class)
//                .destination(GCSMicroservice.TEST, "checkMicroserviceCache", "dto")
//                .httpMethod(HttpMethod.GET)
//                .setRequestPayload(ProvidableAssetDTOType.COUNTRY_CODE_MODEL)
//                .performRequestWithGCSResponse()
//                .toGCSResponse();
//        assertFalse(gcsResponse.hasError());
//        responseData = gcsResponse.getResponseData();
//        assertTrue(responseData);
//
//        //Test that homes-service does not have this item in cache
//        gcsResponse = gcsRemoteRequests.initiateRequest(boolean.class)
//                .destination(GCSMicroservice.HOMES, "checkMicroserviceCache", "dto")
//                .httpMethod(HttpMethod.GET)
//                .setRequestPayload(ProvidableAssetDTOType.COUNTRY_CODE_MODEL)
//                .performRequestWithGCSResponse()
//                .toGCSResponse();
//        assertFalse(gcsResponse.hasError());
//        responseData = gcsResponse.getResponseData();
//        assertTrue(responseData);
//    }
//
//    private void setUp(String instanceId){
//        //since this test uses an in memory database we need to fake the subscription request
//        var providableAssetDTOTypes = (Set.of(ProvidableAssetDTOType.AREA_CONFIGURATION_CONTENT, ProvidableAssetDTOType.COUNTRY_CODE_MODEL));
//        var service1 = GCSMicroservice.HOMES;
//        var service2 = GCSMicroservice.TEST;
//        var request = MicroserviceProvidableAssetDTOsSubscriptionRequestDTO.builder().gcsMicroservice(service1).uniqueInstanceId(instanceId)
//                .providableAssetDTOTypeSet( providableAssetDTOTypes).build();
//        var request2 = MicroserviceProvidableAssetDTOsSubscriptionRequestDTO.builder().gcsMicroservice(service2).uniqueInstanceId(instanceId)
//                .providableAssetDTOTypeSet( providableAssetDTOTypes).build();
//
//        subscriptionService.receiveSubscriptionRequest(request);
//        subscriptionService.receiveSubscriptionRequest(request2);
//
//    }
//
//    public List<ProvidedAssetDTO> editFileAndBootStrap(String pathToFile) throws Exception {
//        //Read asset from resource folder and change the version
//        var resourceStream = new InputStreamReader(ResourcesApplication.class.getClassLoader().getResourceAsStream(pathToFile));
//        BufferedReader br = new BufferedReader(resourceStream);
//        StringBuilder contentBuilder = new StringBuilder();
//
//        String line;
//        while ((line = br.readLine()) != null) {
//            contentBuilder.append(line).append("\n");
//        }
//        br.close();
//        String originalContent = contentBuilder.toString();
//        String modifiedContent = originalContent.replace("0.0", "0.1");
//        Path path = Path.of(ResourcesApplication.class.getClassLoader().getResource(pathToFile).getPath().substring(1));
//        try {
//            Files.writeString(path, modifiedContent);
//        }finally {
//
//        }
//
//        //run assetboostrap after version has been changed
//        var returned  = assetGroupBootstrap.bootstrap(true);
//
//
//        try{
//            Files.writeString(path,originalContent);
//        }finally{
//
//        }
//        return  returned;
//    }
}