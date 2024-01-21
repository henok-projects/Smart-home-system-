package com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.activesessions;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.lib.utils.DateUtils;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ActiveSessionDTO {
    @NotNull
    String token;

    @NotNull
    boolean isForceExpired;

    @JsonFormat(pattern="yyyyMMddHH:mm:ss") // TODO: Have format somewhere global
    private LocalDateTime validUntil;

    @JsonIgnore
    public boolean isExpired(){
        if (isForceExpired){
            return true;
        }
        return DateUtils.secondsBetween(LocalDateTime.now(), validUntil) > 0;
    }
}
