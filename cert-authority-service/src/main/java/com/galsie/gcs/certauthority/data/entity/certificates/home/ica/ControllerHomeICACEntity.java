package com.galsie.gcs.certauthority.data.entity.certificates.home.ica;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

/**
 * ICAC Entity held by a device controller
 */
@Entity
@NoArgsConstructor
public class ControllerHomeICACEntity extends HomeICACEntity{
    @Builder
    public ControllerHomeICACEntity(Long id, Long homeId, long icacId, String serialNumber, byte[] derEncodedCertificate) {
        super(id, homeId, icacId, serialNumber, derEncodedCertificate);
    }
}
