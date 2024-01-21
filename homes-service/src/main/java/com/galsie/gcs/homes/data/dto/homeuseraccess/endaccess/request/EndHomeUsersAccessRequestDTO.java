package com.galsie.gcs.homes.data.dto.homeuseraccess.endaccess.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;

import java.util.List;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EndHomeUsersAccessRequestDTO {

    @JsonProperty("home_id")
    @NotNull
    private Long homeId;

    @JsonProperty("home_user_ids")
    @NotNull
    private List<Long> homeUserIds;


    public boolean isValidHomeId() {
        return this.homeId != null && this.homeId >= 0;
    }

    public boolean isValidHomeUserId() {
        if (this.homeUserIds == null) {
            return false;
        }

        for (Long homeUserId : this.homeUserIds) {
            if (homeUserId == null || homeUserId < 0) {
                return false;
            }
        }

        return true;
    }

}

