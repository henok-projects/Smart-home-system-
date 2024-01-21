package com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.lastaccess;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO used to send a notification that this session was last accessed at a certain time
 */
@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthSessionLastAccessUpdateDTO {
    @NotNull
    String sessionToken;

    @NotNull
    @JsonFormat(pattern = "yyyyMMddHH:mm:ss") // TODO: Have format somewhere global
    LocalDateTime lastAccess;
}
