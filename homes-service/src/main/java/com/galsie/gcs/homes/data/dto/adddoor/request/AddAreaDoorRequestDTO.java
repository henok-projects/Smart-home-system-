package com.galsie.gcs.homes.data.dto.adddoor.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.addarea.adddoor.AddAreaDoorResponseErrorType;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.lib.utils.lang.NotNull;
import com.galsie.lib.utils.lang.Nullable;
import lombok.*;

import java.util.Optional;
import java.util.regex.Pattern;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AddAreaDoorRequestDTO {

    @JsonProperty("area_id")
    @NotNull
    private Long areaId;

    @JsonProperty("connect_to_area")
    @Nullable
    private Long connectToAreaId;

    @JsonProperty("door_name")
    @NotNull
    private String doorName;


    private static Pattern pattern = Pattern.compile("([\\p{L}\\p{N}]{1}+[\\p{L}\\p{N}_\\-.]*){1,19}");

    private boolean isAreaIdValid() {
        return this.areaId > 0;
    }

    public boolean isConnectToAreaIdValid() {
        return this.connectToAreaId == null || this.connectToAreaId > 0;
    }

    private boolean isDoorNameValid() {
        return pattern.matcher(this.doorName).matches();
    }

    public Optional<AddAreaDoorResponseErrorType> validate() {
        if (!isAreaIdValid()) {
            return Optional.of(AddAreaDoorResponseErrorType.INVALID_AREA_ID);
        }
        if (!isConnectToAreaIdValid()) {
            return Optional.of(AddAreaDoorResponseErrorType.INVALID_CONNECT_TO_AREA_ID);
        }
        if(connectToAreaId != null && connectToAreaId.equals(areaId)) {
            return Optional.of(AddAreaDoorResponseErrorType.AREA_ID_AND_CONNECT_TO_AREA_ID_ARE_THE_SAME);
        }
        if (!isDoorNameValid()) {
            return Optional.of(AddAreaDoorResponseErrorType.INVALID_DOOR_NAME);
        }
        return Optional.empty();
    }
}
