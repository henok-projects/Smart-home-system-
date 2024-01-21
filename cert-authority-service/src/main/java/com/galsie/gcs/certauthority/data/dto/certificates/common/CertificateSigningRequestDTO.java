package com.galsie.gcs.certauthority.data.dto.certificates.common;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.lib.certificates.csr.SomeCSRHolder;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;

@GalDTO
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CertificateSigningRequestDTO {
    @NotNull
    String base64DerEncodedCSR;

    public SomeCSRHolder toCSRHolder() throws IOException {
        return SomeCSRHolder.fromBase64Encoded(base64DerEncodedCSR);
    }
}
