package com.galsie.gcs.certauthority.data.entity.certificates.home.ica;


import com.galsie.gcs.certauthority.data.entity.certificates.CertificateEntity;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor
public abstract class HomeICACEntity extends CertificateEntity {

    public HomeICACEntity(Long id, Long homeId, long icacId, String serialNumber, byte[] derEncodedCertificate) {
        super(id, serialNumber, derEncodedCertificate);
        this.homeId = homeId;
        this.icacId = icacId;
    }

    @Column(name = "home_id")
    protected long homeId;

    @Column(name = "icac_id")
    protected long icacId;
}
