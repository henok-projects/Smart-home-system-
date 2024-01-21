package com.galsie.gcs.certauthority.service.certificate.ica;

import com.galsie.gcs.certauthority.data.dto.certificates.ica.request.ControllerHomeICACSigningRequest;
import com.galsie.gcs.certauthority.data.dto.certificates.ica.response.ControllerHomeICACSigningResponseDTO;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.GCSResponse;
import com.galsie.lib.certificates.certificate.SomeX509v3CertificateManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * We have:
 * - ICAC per controller (which is allowed to commission) that sign NOCs for itself and nodes it commissions
 * - ICAC held by GCS that signs NOCs for controllers not allowed to commission, and potentially for devices
 */
@Service
@Slf4j
public class HomeICACService {

    @Autowired
    ControllerHomeICACService controllerHomeICACService;

    @Autowired
    GCSHomeICACService homeICACCommonService;

    public GCSResponse<ControllerHomeICACSigningResponseDTO> processControllerICACertificateSigningRequest(ControllerHomeICACSigningRequest certificateSigningRequestDTO) {
        return controllerHomeICACService.processControllerICACertificateSigningRequest(certificateSigningRequestDTO);
    }

    public SomeX509v3CertificateManager gcsInternalGetOrCreateGCSHomeICAC(Long homeId) throws Exception {
        return homeICACCommonService.gcsInternalGetOrCreateGCSHomeICAC(homeId);
    }

}
