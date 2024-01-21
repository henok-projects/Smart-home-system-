package com.galsie.gcs.certauthority.service.certificate.ica;

import com.galsie.lib.certificates.certificate.builder.CertificateBuilder;
import com.galsie.lib.certificates.certificate.builder.CertificateBuilderCommonImpl;
import com.galsie.lib.certificates.exception.FabricIdNotSupportedException;
import com.galsie.lib.certificates.exception.MaxSupportedRDNCountExceededException;
import com.galsie.lib.utils.StringUtils;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.springframework.stereotype.Service;
import reactor.util.annotation.Nullable;

import java.io.IOException;
import java.util.Date;

/**
 * Package private because {@link HomeICACService} should be used
 */
@Service
class HomeICACCommonService {


    /**
     * Currently, fabric id 0 is reserved by the matter protocol
     * - So the fabric id of a home is the homeId+1
     *
     * Note: This may change in the future
     *
     * @param homeId The id of the home to get the fabric id for
     * @return
     */
    public Long getFabricIdFor(Long homeId){
        return homeId+1;
    }

    /**
     * Generates a Certificate Serial number for a certificate belonging to this home id
     * - The serial number is random except for its first part which is the home id
     * Format: homeId_randomString
     *
     * NOTE: The serial number length must NOT exceed <b>20 octets</b> according to Matter section 6.5.4. Serial Number
     * @param homeId the home id to generate the certificate serial number for
     * @return
     */
    public String generateCertificateSerialNumberFor(Long homeId){
        // NOTE: since we are using alphanumeric numbers, which are all in ASCII range, only 1 octet is occupied per character
        var homeIdStr = String.valueOf(homeId);
        var remainingSize = 20 - homeIdStr.length() - 1; // subtract 1 for the underscore
        return homeId + "_" + StringUtils.randomAlphanumeric(remainingSize);
    }

    /**
     *
     * @param certificateBuilder The certificate builder which needs to be setup as a home icac
     * @param homeId
     * @param icacId
     * @param serialNumber
     * @param validFrom
     * @param validTo
     * @param <T>
     * @throws MaxSupportedRDNCountExceededException
     * @throws IOException
     */
    public <T extends CertificateBuilder> void setupAsHomeICAC(CertificateBuilderCommonImpl<T> certificateBuilder, Long homeId, Long icacId, Long fabricId, String serialNumber, Date validFrom, @Nullable Date validTo) throws MaxSupportedRDNCountExceededException, IOException, FabricIdNotSupportedException {
        certificateBuilder
                .setFabricIdRDN(fabricId);  // fabric id 0 is reserved by the protocol
        certificateBuilder.setIcacIdRDN(icacId); // from 6.5.6.2. Matter Certificate Types:The value of matter-icac-id and matter-rcac-id DN attribute types MAY be any 64-bit identifier desired by the certificate’s issuer. It's used for debugging purposes or whatsonot.
        certificateBuilder.extensions()
                .setAsCertificateAuthority()
                .setKeyUsage(KeyUsage.keyCertSign | KeyUsage.cRLSign)
                .withAuthorityKeyIdentifier()
                .withSubjectKeyIdentifier();
        certificateBuilder.setSerialNumber(serialNumber);
        certificateBuilder.setValidFrom(validFrom);
        certificateBuilder.setValidTo(validTo);
    }



}
