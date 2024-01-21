package com.galsie.gcs.homes.data.dto.homeuseraccess.editaccess.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.dto.common.UserHomeAccessInfoDTO;
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
public class EditHomeUsersAccessInfoRequestDTO {

    @JsonProperty("home_id")
    @NotNull
    private Long homeId;

    @JsonProperty("home_user_ids")
    @NotNull
    private List<Long> homeUserIds;

    @JsonProperty("access_info")
    @NotNull
    private UserHomeAccessInfoDTO accessInfo;


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
