package com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.activesessions;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ActiveSessionListDTO<idType> {
    @NotNull
    idType id;

    @NotNull
    List<ActiveSessionDTO> userAccountActiveSessions;

    public Optional<ActiveSessionDTO> getWithTokenMatching(String token){
        return userAccountActiveSessions.stream().filter((a) -> a.getToken().equals(token)).findFirst();
    }
}
