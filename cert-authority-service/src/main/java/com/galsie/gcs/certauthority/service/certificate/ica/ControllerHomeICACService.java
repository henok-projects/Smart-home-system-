package com.galsie.gcs.certauthority.service.certificate.ica;

import com.galsie.gcs.certauthority.data.discrete.RootCertificateAuthorityType;
import com.galsie.gcs.certauthority.data.dto.certificates.ica.request.ControllerHomeICACSigningRequest;
import com.galsie.gcs.certauthority.data.dto.certificates.ica.response.ControllerHomeICACSigningResponseDTO;
import com.galsie.gcs.certauthority.data.dto.certificates.ica.response.HomeICACSingingErrorType;
import com.galsie.gcs.certauthority.data.entity.certificates.home.ica.ControllerHomeICACEntity;
import com.galsie.gcs.certauthority.repository.home.ica.ControllerHomeICACEntityRepository;
import com.galsie.gcs.certauthority.service.certificate.RootCertificateAuthorityService;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.exception.GCSResponseException;
import com.galsie.lib.certificates.asn1.object.MatterASN1ObjectIdentifier;
import com.galsie.lib.certificates.certificate.SomeX509v3CertificateHolder;
import com.galsie.lib.certificates.certificate.SomeX509v3CertificateManager;
import com.galsie.lib.certificates.certificate.builder.AnyUnmanagedCertificateBuilder;
import com.galsie.lib.certificates.csr.SomeCSRHolder;
import com.galsie.lib.utils.NumericUtils;
import com.galsie.lib.utils.pair.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

/**
 * Package private because {@link HomeICACService} should be used
 */
@Service
@Slf4j
class ControllerHomeICACService {

    @Autowired
    HomeICACCommonService homeICACCommonService;

    @Autowired
    RootCertificateAuthorityService rootCertificateAuthorityService;

    @Autowired
    ControllerHomeICACEntityRepository controllerHomeICACEntityRepository;


    public GCSResponse<ControllerHomeICACSigningResponseDTO> processControllerICACertificateSigningRequest(ControllerHomeICACSigningRequest certificateSigningRequestDTO) {
        try {
            return this.gcsInternalProcessControllerICACertificateSigningRequest(certificateSigningRequestDTO);
        } catch (GCSResponseException exception) {
            return exception.getGcsResponse(ControllerHomeICACSigningResponseDTO.class);
        }
    }

    /**
     * TODO: Perhaps include front-end device id so that we track which devices have which icacs (so we'd set the icac id to the device id)
     *
     * @param certificateSigningRequestDTO
     * @return
     */
    public GCSResponse<ControllerHomeICACSigningResponseDTO> gcsInternalProcessControllerICACertificateSigningRequest(ControllerHomeICACSigningRequest certificateSigningRequestDTO) throws GCSResponseException {
        // Parse to a cst holder
        SomeCSRHolder someCSRHolder;
        try {
            someCSRHolder = certificateSigningRequestDTO.toCSRHolder();
        }catch (Exception ex){
            return ControllerHomeICACSigningResponseDTO.responseError(HomeICACSingingErrorType.CSR_DECODING_FAILED);
        }

        // get the home id from the request
        long homeId = certificateSigningRequestDTO.getHomeId();
        // set the icac id to match the home id
        long icacId = homeId;
        // generate the serial number
        var serialNumber = homeICACCommonService.generateCertificateSerialNumberFor(homeId);

        // Sign the controllers icac
        var signedControllerICACPair = signControllerICAC(homeId, icacId, homeICACCommonService.getFabricIdFor(homeId), serialNumber, someCSRHolder);
        if (signedControllerICACPair.hasSecond()){ // return the error if there was one
            return ControllerHomeICACSigningResponseDTO.responseError(signedControllerICACPair.getSecond());
        }
        // if there wasn't one an error, this should be non-nil holds the signed certificate
        var signedControllerICAC = signedControllerICACPair.getFirst();

        // encoded in the different formats we need, return an encoding error if failed
        String base64DEREncoded;
        byte[] derEncoded;
        try {
            base64DEREncoded = signedControllerICAC.getBase64DEREncoded();
            derEncoded = signedControllerICAC.getEncoded();
        }catch (Exception ex){
            return ControllerHomeICACSigningResponseDTO.responseError(HomeICACSingingErrorType.CERTIFICATE_ENCODING_FAILED);
        }
        // create the entity
        var controllerHomeICACEntity = ControllerHomeICACEntity.builder()
                .homeId(homeId)
                .icacId(icacId)
                .serialNumber(serialNumber)
                .derEncodedCertificate(derEncoded).build();
        GCSResponse.saveEntityThrows(controllerHomeICACEntityRepository, controllerHomeICACEntity);
        return ControllerHomeICACSigningResponseDTO.responseSuccess(base64DEREncoded);
    }

    /**
     * Controllers with commissioning capabilities request the signing of an ICAC.
     * - They then internally use the icac keypair and info to:
     * - Sign an operational certificate for themselves, as they would be managing the certificate
     * - Sign operational certificates for devices being commissioned
     *
     * @param someCSRHolder
     * @return
     */
    private Pair<SomeX509v3CertificateHolder, HomeICACSingingErrorType> signControllerICAC(Long homeId, Long icacId, Long fabricId, String serialNumber, SomeCSRHolder someCSRHolder) {

        var extractedIcacId = extractICACId(someCSRHolder);
        // only fails if exceptions were thrown, not if it wasn't found
        if (extractedIcacId.hasSecond()) {
            return Pair.ofSecond(extractedIcacId.getSecond());
        }
        // ICAC should not be included in the subject DN by choice
        // If it were, ignore it and log a warning
        if (extractedIcacId.getFirst() != -1) {
            log.warn("ICAC id was included in a home controller ICAC request, it will be ignored. ");
        }

        SomeX509v3CertificateHolder createdControllerICAC;
        try {
            var rootCertificate = rootCertificateAuthorityService.gcsInternalGetOrCreateRootCertificateAuthority(RootCertificateAuthorityType.GALHOMES);
            createdControllerICAC = this.createControllerHomeICAC(homeId, icacId, fabricId, serialNumber, someCSRHolder, rootCertificate);
        } catch (Exception ex) {
            return Pair.ofSecond(HomeICACSingingErrorType.CERTIFICATE_CREATION_AND_SIGNING_FAILED);
        }
        return Pair.ofFirst(createdControllerICAC);
    }

    private SomeX509v3CertificateHolder createControllerHomeICAC(Long homeId, Long icacId, Long fabricId, String serialNumber, SomeCSRHolder controllerICACCSR, SomeX509v3CertificateManager rootCertificateManager) throws Exception {
        var controllerICACBuilder = AnyUnmanagedCertificateBuilder.start()
                .forCertificateSigningRequest(controllerICACCSR, true);
        homeICACCommonService.setupAsHomeICAC(controllerICACBuilder, homeId, icacId, fabricId, serialNumber, new Date(), null);
        return  controllerICACBuilder.buildSignedBy(rootCertificateManager);
    }


    /**
     * CSR shouldn't include icac, its set by GCS
     * - If it did though, we log an error and ignore it
     * @param someCSRHolder
     * @return
     */
    private Pair<Long, HomeICACSingingErrorType> extractICACId(SomeCSRHolder someCSRHolder){
        Optional<String> icacIdStringOpt;
        try {
            icacIdStringOpt = someCSRHolder.getSubjectRDNValueFor(MatterASN1ObjectIdentifier.MATTER_ICAC_ID);
        }catch (Exception ex){
            return Pair.ofSecond(HomeICACSingingErrorType.SUBJECT_DN_RDN_EXTRACTION_FAILED);
        }
        if (icacIdStringOpt.isEmpty()) {
            return Pair.of(-1L, null);
        }
        var icacIdOpt = NumericUtils.parseLong(icacIdStringOpt.get());
        if (icacIdOpt.isEmpty()){
            return Pair.ofSecond(HomeICACSingingErrorType.SUBJECT_DN_MATTER_ICAC_ID_PARSING_FAILED);
        }
        return Pair.ofFirst(icacIdOpt.get());
    }



    /*
    Removed since a matter certificate can encode a max of 5 rdns
    private Pair<Long, HomeICACertificateSingingErrorType> extractHomeId(SomeCSRHolder someCSRHolder){
        Optional<String> homeIdStringOpt;
        try {
            homeIdStringOpt = someCSRHolder.getSubjectRDNValueFor(GalsieASN1ObjectIdentifier.GALSIE_HOME_ID);
        }catch (Exception ex){
            return Pair.ofSecond(HomeICACertificateSingingErrorType.SUBJECT_DN_RDN_EXTRACTION_FAILED);
        }
        if (homeIdStringOpt.isEmpty()) {
            return Pair.of(-1L, null);
        }
        var homeIdOpt = NumericUtils.parseLong(homeIdStringOpt.get());
        if (homeIdOpt.isEmpty()){
            return Pair.ofSecond(HomeICACertificateSingingErrorType.SUBJECT_DN_GALSIE_HOME_ID_PARSING_FAILED);
        }
        return Pair.ofFirst(homeIdOpt.get());
    }*/
}
