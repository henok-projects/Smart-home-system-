package com.galsie.gcs.homes.data.discrete.homesystemroletype;

public enum HomeSystemRoleType {
    HOUSEMASTER("House Master"),
    ADMIN("Administrator"),
    CHILD("Child"),
    GUEST("Guest"),
    OTHER("Other");

    private final String displayName;

    HomeSystemRoleType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static HomeSystemRoleType fromString(String text) {
        for (HomeSystemRoleType b : HomeSystemRoleType.values()) {
            if (b.displayName.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
