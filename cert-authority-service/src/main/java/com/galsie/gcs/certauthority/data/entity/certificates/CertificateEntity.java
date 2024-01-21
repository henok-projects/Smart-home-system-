package com.galsie.gcs.certauthority.data.entity.certificates;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import com.galsie.lib.certificates.certificate.SomeX509v3CertificateHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.IOException;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class CertificateEntity implements GalEntity<Long> {

    public CertificateEntity(String serialNumber, byte[] derEncodedCertificate){
        this.serialNumber = serialNumber;
        this.derEncodedCertificate = derEncodedCertificate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="unique_id")
    protected Long uniqueId;

    @Column(name="serial_number", unique = true)
    protected String serialNumber;


    @Lob
    @Column(name="der_encoded")
    protected byte[] derEncodedCertificate;


    public SomeX509v3CertificateHolder toCertificateHolder() throws IOException {
        return SomeX509v3CertificateHolder.fromDERData(derEncodedCertificate);
    }
}
