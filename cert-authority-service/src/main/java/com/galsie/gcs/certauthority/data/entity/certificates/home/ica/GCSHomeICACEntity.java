package com.galsie.gcs.certauthority.data.entity.certificates.home.ica;

import com.galsie.lib.certificates.certificate.SomeX509v3CertificateManager;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@NoArgsConstructor
@Entity
@Getter
@Setter
public class GCSHomeICACEntity extends HomeICACEntity {

    @Builder
    public GCSHomeICACEntity(Long id, String base64EncodedPrivateKey, Long homeId, long icacId, String serialNumber, byte[] derEncodedCertificate) {
        super(id, homeId, icacId, serialNumber, derEncodedCertificate);
        this.base64EncodedPrivateKey = base64EncodedPrivateKey;
    }


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
