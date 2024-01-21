package com.galsie.gcs.certauthority.controller;

import com.galsie.gcs.certauthority.data.dto.certificates.ica.request.ControllerHomeICACSigningRequest;
import com.galsie.gcs.certauthority.service.certificate.ica.HomeICACService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/certificates")
public class TestCertificateController {

    @Autowired
    HomeICACService homeICACService;

    /**
     * A front-end device running a controller that can commission would request the signing of an intermediate certificate
     * - The intermediate certificate is associated with the fabric, and is used by the controller to sign its operational certificate, and operational certificates for NOCs
     * @param certificateSigningRequestDTO
     * @return
    */
    @PostMapping("/signControllerICAC")
    public ResponseEntity<?> signControllerHomeICAC(@RequestBody ControllerHomeICACSigningRequest certificateSigningRequestDTO){
        return homeICACService.processControllerICACertificateSigningRequest(certificateSigningRequestDTO).toResponseEntity();
    }

    /*
    @PostMapping
    public ResponseEntity<?> signNOC(@RequestBody CertificateSigningRequestDTO certificateSigningRequestDTO){

    }
     */
}
