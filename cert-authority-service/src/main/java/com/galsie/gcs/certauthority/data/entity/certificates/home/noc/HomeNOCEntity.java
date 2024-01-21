package com.galsie.gcs.certauthority.data.entity.certificates.home.noc;

import com.galsie.gcs.certauthority.data.discrete.HomeNOCType;
import com.galsie.gcs.certauthority.data.entity.certificates.CertificateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * A Home Node Operational Certificate is:
 * - Either signed by the ICAC certificate held in {@link com.galsie.gcs.certauthority.data.entity.certificates.home.ica.ControllerHomeICACEntity}
 *  - In that case, the controller manages the ICAC (holds its keypair) and so it can commission devices. It use this ICAC to sign NOCs for itself and devices
 *  - the NOCs are sent back to GCS so that all generated NOCs can be tracked
 * - Or signed by the ICAC certificate held in {@link com.galsie.gcs.certauthority.data.entity.certificates.home.ica.GCSHomeICACEntity}
 *  - In that case,the controller CANT commission devices.
 *  - It requests the signing of an NOC, which is signed by the ICAC held by GCS
 */
@Entity
public class HomeNOCEntity extends CertificateEntity {

    @Column(name="noc_type")
    protected HomeNOCType nocType;

}
