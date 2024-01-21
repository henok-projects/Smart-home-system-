package com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.activesessions.user;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserActiveSessionListRequestDTO {

    @NotNull
    Long userId;

    @Nullable
    LocalDateTime accessedAfter;
}
