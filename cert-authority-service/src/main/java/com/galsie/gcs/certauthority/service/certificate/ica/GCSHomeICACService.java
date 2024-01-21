package com.galsie.gcs.certauthority.service.certificate.ica;

import com.galsie.gcs.certauthority.data.discrete.RootCertificateAuthorityType;
import com.galsie.gcs.certauthority.data.entity.certificates.home.ica.GCSHomeICACEntity;
import com.galsie.gcs.certauthority.repository.home.ica.GCSHomeICACEntityRepository;
import com.galsie.gcs.certauthority.service.certificate.RootCertificateAuthorityService;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.lib.certificates.certificate.SomeX509v3CertificateManager;
import com.galsie.lib.certificates.certificate.builder.AnyManagedCertificateBuilder;
import com.galsie.lib.certificates.keypair.algo.ECDSA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Package private because {@link HomeICACService} should be used
 */
@Service
class GCSHomeICACService {

    @Autowired
    HomeICACCommonService homeICACCommonService;

    @Autowired
    GCSHomeICACEntityRepository gcsHomeICACEntityRepository;

    @Autowired
    RootCertificateAuthorityService rootCertificateAuthorityService;


    /**
     * TODO: Perhaps have a special id for the ICAC held by GCS for the home, ie - ICAC id 0 reserved for GCS
     *
     * A Home ICAC certificate is held by GCS for the sake of:
     * - Signing NOCs for controllers without commissioning capabilities
     *
     * Note:
     * - Currently, a SINGLE home ICAC certificate is held by GCS for each home
     *
     * @param homeId
     * @return
     * @throws Exception
     */
    public SomeX509v3CertificateManager gcsInternalGetOrCreateGCSHomeICAC(Long homeId) throws Exception {
        var gcsHomeICACOpt = gcsHomeICACEntityRepository.findByHomeId(homeId);
        if (gcsHomeICACOpt.isPresent()){
            return gcsHomeICACOpt.get().toCertificateManager();
        }
        var homeRCA = rootCertificateAuthorityService.gcsInternalGetOrCreateRootCertificateAuthority(RootCertificateAuthorityType.GALHOMES);
        String serialNumber = homeICACCommonService.generateCertificateSerialNumberFor(homeId);

        var homeICACManager = createGCSHomeICAC(homeId, homeId, homeICACCommonService.getFabricIdFor(homeId), serialNumber, homeRCA);
        var homeICACEntity = GCSHomeICACEntity.builder()
                .homeId(homeId)
                .icacId(homeId)
                .serialNumber(serialNumber)
                .derEncodedCertificate(homeICACManager.getEncodedCertificate())
                .base64EncodedPrivateKey(homeICACManager.getBase64EncodedPrivateKey())
                .build();
        GCSResponse.saveEntityThrows(gcsHomeICACEntityRepository, homeICACEntity);
        return homeICACManager;
    }

    /**
     * CREATE an intermediate certificate for a home, certificate is held by GCS for the sake of signing device NOCs
     *
     * Background:
     * For controllers which should not be able to commission, they should be issued an NOC
     * - This NOC can be signed by the root certificate or intermediate certificate.
     * - We chose that GCS manages an intermediate certificate for each home (we also have intermediate ceritifcates held by clients for the sake of commissioning)
     *
     * @param homeId
     * @param rootCertificateManager
     * @return
     * @throws Exception
     */
    private SomeX509v3CertificateManager createGCSHomeICAC(Long homeId, Long icacId, Long fabricId, String serialNumber, SomeX509v3CertificateManager rootCertificateManager) throws Exception {
        var validFrom = new Date();
        var icaCertificateBuilder = AnyManagedCertificateBuilder.start()
                .keypair()
                .setGenerationAlgorithm(ECDSA.SECP_256_R1)
                .done();
        homeICACCommonService.setupAsHomeICAC(icaCertificateBuilder, homeId, icacId, fabricId, serialNumber, new Date(), null); // TODO: Maybe set valid to
        return icaCertificateBuilder.buildSignedBy(rootCertificateManager);
    }
}
