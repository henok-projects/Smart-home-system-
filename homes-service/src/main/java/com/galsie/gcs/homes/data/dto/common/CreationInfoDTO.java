package com.galsie.gcs.homes.data.dto.common;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreationInfoDTO {


    @JsonProperty("created_on")
    @NotNull
    private LocalDateTime createdOn;

//    @JsonProperty("created_by")
//    @NotNull
//    UserContactInfoDTO createdBy;
}