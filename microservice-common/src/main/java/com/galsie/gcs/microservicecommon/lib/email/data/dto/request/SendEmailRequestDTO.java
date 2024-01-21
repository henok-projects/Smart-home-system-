package com.galsie.gcs.microservicecommon.lib.email.data.dto.request;

import com.galsie.gcs.microservicecommon.lib.email.data.discrete.EmailType;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SendEmailRequestDTO {

    @NotNull
    private EmailType emailType;

    @NotNull
    private String toAddress;

    @NotNull
    private HashMap<String, String> variableReplacementMap;
}
