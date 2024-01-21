package com.galsie.gcs.certauthority.data.dto.certificates.ica.response;

public enum HomeICACSingingErrorType {
    CSR_DECODING_FAILED,

    SUBJECT_DN_RDN_EXTRACTION_FAILED,
    SUBJECT_DN_MATTER_ICAC_ID_PARSING_FAILED, // parsing to a long
    CERTIFICATE_CREATION_AND_SIGNING_FAILED,
    CERTIFICATE_ENCODING_FAILED,
    MISMATCHING_HOME_ID;
}
