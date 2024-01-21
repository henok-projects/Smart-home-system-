package com.galsie.gcs.certauthority.data.dto.certificates.ica.response;

import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.lib.certificates.certificate.SomeX509v3CertificateHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import reactor.util.annotation.Nullable;

import java.io.IOException;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ControllerHomeICACSigningResponseDTO {

    @Nullable
    HomeICACSingingErrorType homeICACertificateSingingError;

    @Nullable
    String base64DerEncodedCertificate;

    public static ControllerHomeICACSigningResponseDTO error(HomeICACSingingErrorType homeICACSingingErrorType){
        return new ControllerHomeICACSigningResponseDTO(homeICACSingingErrorType, null);
    }

    public static GCSResponse<ControllerHomeICACSigningResponseDTO> responseError(HomeICACSingingErrorType homeICACSingingErrorType){
        return GCSResponse.response(error(homeICACSingingErrorType));
    }

    public static ControllerHomeICACSigningResponseDTO success(String base64DerEncodedCertificate){
        var responseDTO =  new ControllerHomeICACSigningResponseDTO(null, base64DerEncodedCertificate);
        return new ControllerHomeICACSigningResponseDTO(null, base64DerEncodedCertificate);
    }
    public static GCSResponse<ControllerHomeICACSigningResponseDTO> responseSuccess(String base64DerEncodedCertificate){
        return GCSResponse.response(success(base64DerEncodedCertificate));
    }
    public static GCSResponse<ControllerHomeICACSigningResponseDTO> responseSuccess(SomeX509v3CertificateHolder someX509v3CertificateHolder) throws IOException {
        return responseSuccess(someX509v3CertificateHolder.getPEMEncoded());
    }

}
