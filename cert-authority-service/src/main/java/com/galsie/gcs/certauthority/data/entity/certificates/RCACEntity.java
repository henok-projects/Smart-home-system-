package com.galsie.gcs.certauthority.data.entity.certificates;

import com.galsie.gcs.certauthority.data.discrete.RootCertificateAuthorityType;
import com.galsie.lib.certificates.certificate.SomeX509v3CertificateManager;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class RCACEntity extends CertificateEntity{

    @Builder
    public RCACEntity(String serialNumber, String base64EncodedPrivateKey, byte[] derEncodedCertificate) {
        super(serialNumber, derEncodedCertificate);
        this.base64EncodedPrivateKey = base64EncodedPrivateKey;
    }

    @Column(name="root_certificate_type", unique = false)
    RootCertificateAuthorityType rootCertificateAuthorityType;

    /**
     * The private key of the certificate encoded in base64
     * // TODO: Store encrypted
     */
    @Column(name="private_key")
    String base64EncodedPrivateKey;


    public SomeX509v3CertificateManager toCertificateManager() throws Exception {
        return SomeX509v3CertificateManager.fromPrivateKeyAndDERData(this.base64EncodedPrivateKey, this.derEncodedCertificate);
    }
}
