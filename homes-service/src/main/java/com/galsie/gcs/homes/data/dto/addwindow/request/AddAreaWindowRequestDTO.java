package com.galsie.gcs.homes.data.dto.addwindow.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.homes.data.discrete.addarea.addwindow.AddAreaWindowResponseErrorType;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.galsie.lib.utils.lang.NotNull;
import lombok.*;

import java.util.Optional;
import java.util.regex.Pattern;

@GalDTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AddAreaWindowRequestDTO {

    @JsonProperty("area_id")
    @NotNull
    private Long areaId;

    @JsonProperty("window_name")
    @NotNull
    private String windowName;


    private static Pattern pattern = Pattern.compile("([\\p{L}\\p{N}]{1}+[\\p{L}\\p{N}_\\-.]*){1,19}");

    private boolean isAreaIdValid() {
        return this.areaId > 0;
    }

    private boolean isWindowNameValid() {
        return pattern.matcher(this.windowName).matches();
    }

    public Optional<AddAreaWindowResponseErrorType> validate() {
        if (!isAreaIdValid()) {
            return Optional.of(AddAreaWindowResponseErrorType.INVALID_AREA_ID);
        }
        if (!isWindowNameValid()) {
            return Optional.of(AddAreaWindowResponseErrorType.INVALID_WINDOW_NAME);
        }
        return Optional.empty();
    }

}
