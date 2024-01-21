package com.galsie.gcs.homes.infrastructure.rolesandaccess.permissions;

import com.galsie.lib.utils.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class PermissionsSubCategory {
    private String subCategoryKey; // TODO: Change to subcategoryKey (general_info, delete_home, ..)
    private boolean allowsFullBasicViewNone;

    //private boolean allowsCustomization;
    @Nullable
    private Class<?> customizedIdType; // LONG (for the area id or device id)

    public PermissionsSubCategory(String subCategoryKey, boolean allowsFullBasicViewNone, Class<?> customizedIdType){
        // INITIALIZES with allowsCustomization = true and customizedIdType matching the provided one
        this.subCategoryKey = subCategoryKey;
        this.allowsFullBasicViewNone = allowsFullBasicViewNone;
        this.customizedIdType = customizedIdType;
    }

    public PermissionsSubCategory(String subCategoryKey, boolean allowsFullBasicViewNone){
        // INITIALIZES with allowsCustomization = false and customizedIdType null
        this(subCategoryKey, allowsFullBasicViewNone, null);
    }

    public boolean allowsCustomization(){
        return this.customizedIdType != null;
    }

    public boolean isPermissionValid(String permissionLevel) {

        if (!allowsFullBasicViewNone) {
            return false;
        }

        return permissionLevel.equalsIgnoreCase("full") ||
                permissionLevel.equalsIgnoreCase("view") ||
                permissionLevel.equalsIgnoreCase("none");
    }

}