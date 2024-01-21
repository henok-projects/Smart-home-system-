package com.galsie.gcs.certauthority.data.dto.certificates.ica.request;

import com.galsie.gcs.certauthority.data.dto.certificates.common.CertificateSigningRequestDTO;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ControllerHomeICACSigningRequest extends CertificateSigningRequestDTO {

    @NotNull
    Long homeId;

    public ControllerHomeICACSigningRequest(Long homeId, String base64DerEncodedCSR) {
        super(base64DerEncodedCSR);
        this.homeId = homeId;
    }

}
