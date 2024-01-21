package com.galsie.gcs.homes.data.dto.leavehome.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.HomeResponseErrorType;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LeaveHomeSetRequestDTO {

    @JsonProperty("homes_to_leave")
    @NotNull
    private List<LeaveSingleHomeDTO> homesToLeave;

    @JsonProperty("force_leave")
    @Nullable
    private Boolean forceLeave;

    public Optional<HomeResponseErrorType> validate() {
        if (homesToLeave == null || homesToLeave.isEmpty()) {
            return Optional.of(HomeResponseErrorType.HOME_DOESNT_EXIST);
        }

        // Validate each LeaveSingleHomeDTO in the list
        for (LeaveSingleHomeDTO leave : homesToLeave) {
            if (leave.getHomeId() == null || leave.getHomeId() < 0) {
                return Optional.of(HomeResponseErrorType.HOME_DOESNT_EXIST);
            }
        }

        return Optional.empty();
    }

}