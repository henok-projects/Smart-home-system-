package com.galsie.gcs.homes.data.discrete;


import java.util.Arrays;
import java.util.Optional;

public enum HomeAreaType {

    ENTRYWAY, KITCHEN, LIVING_ROOM, BEDROOM, MUDROOM, CORRIDOR, SALON, GARAGE, STORAGE, GARDEN,
    BATHROOM, DINING_ROOM, BASEMENT, ATTIC, HOME_OFFICE, LAUNDRY_ROOM, BALCONY, PATIO, WALK_IN_CLOSET, OTHER;

    public static Optional<HomeAreaType> fromString(String areaTypeString){
        return Arrays.stream(HomeAreaType.values()).filter(s -> s.name().equalsIgnoreCase(areaTypeString)).findFirst();
    }
}
