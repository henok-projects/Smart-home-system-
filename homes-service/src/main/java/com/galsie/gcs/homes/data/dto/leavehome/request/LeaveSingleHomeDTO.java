package com.galsie.gcs.homes.data.dto.leavehome.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.*;
import reactor.util.annotation.Nullable;

import java.util.List;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LeaveSingleHomeDTO {


    @JsonProperty("home_id")
    @NotNull
    private Long homeId;

    @JsonProperty("leave_even_if_some_operators_wouldnt_work")
    @Nullable
    private Boolean leaveEvenIfSomeOperatorsWouldntWork;

    @JsonProperty("new_housemaster_home_user_ids")
    @Nullable
    private List<Long> newHousemasterHomeUserIds;

    @JsonProperty("allow_delete_if_last_user")
    @Nullable
    private Boolean allowDeleteIfLastUser;

    public boolean shouldLeaveEvenIfSomeOperatorsWouldntWork(){
        return leaveEvenIfSomeOperatorsWouldntWork != null && leaveEvenIfSomeOperatorsWouldntWork;
    }

    public boolean shouldDeleteHomeIfLastUser(){
        return allowDeleteIfLastUser != null && allowDeleteIfLastUser;
    }

}
