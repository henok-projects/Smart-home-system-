package com.galsie.gcs.certauthority.service.certificate;


import com.galsie.gcs.certauthority.data.discrete.RootCertificateAuthorityType;
import com.galsie.gcs.certauthority.data.entity.certificates.RCACEntity;
import com.galsie.gcs.certauthority.repository.RCACEntityRepository;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.lib.certificates.certificate.SomeX509v3CertificateManager;
import com.galsie.lib.certificates.certificate.builder.AnyManagedCertificateBuilder;
import com.galsie.lib.certificates.keypair.algo.ECDSA;
import com.galsie.lib.utils.StringUtils;
import com.sun.istack.NotNull;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.util.annotation.Nullable;

import java.util.Date;

@Service
public class RootCertificateAuthorityService {

    @Autowired
    RCACEntityRepository rcacEntityRepository;



    public SomeX509v3CertificateManager gcsInternalGetOrCreateRootCertificateAuthority(RootCertificateAuthorityType rootCertificateAuthorityType) throws Exception{
        var rootCertificateAuthorityOPT = rcacEntityRepository.findByRootCertificateAuthorityType(rootCertificateAuthorityType);
        if (rootCertificateAuthorityOPT.isPresent()){
            var rcacManager = rootCertificateAuthorityOPT.get().toCertificateManager();
            return rcacManager;
        }
        // Doesn't exist, so we need to create a root certificate authority for this type
        var serialNumber = StringUtils.randomAlphanumeric(10); // TODO: check max length allowed
        var newRCACManager = createNewRootCertificate(serialNumber, new Date(), null);

        var rcacEntity = RCACEntity.builder()
                .serialNumber(serialNumber)
                .base64EncodedPrivateKey(newRCACManager.getBase64EncodedPrivateKey())
                .derEncodedCertificate(newRCACManager.getEncodedCertificate())
                .build();
        GCSResponse.saveEntityThrows(rcacEntityRepository, rcacEntity);
        return newRCACManager;
    }


    // TODO: Comment as to why exactly this matches specs

    private SomeX509v3CertificateManager createNewRootCertificate(String serialNumber, @NotNull Date validFrom, @Nullable Date validTo) throws Exception {
        return AnyManagedCertificateBuilder.start()
                .setRCACIdRDN(0L)
                .keypair()
                    .setGenerationAlgorithm(ECDSA.SECP_256_R1)
                    .done()
                .extensions()
                    .setAsCertificateAuthority()
                    .setKeyUsage(KeyUsage.keyCertSign | KeyUsage.cRLSign)
                    .withAuthorityKeyIdentifier()
                    .withSubjectKeyIdentifier()
                    .done()
                .setSerialNumber(serialNumber)
                .setValidFrom(validFrom)
                .setValidTo(validTo)
                .buildAsSelfSigned();
    }
}
