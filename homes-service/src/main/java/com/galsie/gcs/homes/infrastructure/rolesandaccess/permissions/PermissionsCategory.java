package com.galsie.gcs.homes.infrastructure.rolesandaccess.permissions;

public enum PermissionsCategory {
    MANAGEMENT("management",
            new PermissionsSubCategory("general_info", true),
            new PermissionsSubCategory("delete_home", true),
            new PermissionsSubCategory("areas", true, Long.class),
            new PermissionsSubCategory("floors", true, Long.class)
            ),
    DEVICES("devices", new PermissionsSubCategory("devives", true , Integer.class)),
    CATEGORIES("categories", new PermissionsSubCategory("categories", true)),
    OPERATORS("operators", new PermissionsSubCategory("automations", true, Long.class));

    private final String key;
    private final PermissionsSubCategory[] subCategories;

    PermissionsCategory(String key, PermissionsSubCategory... subCats) {
        this.key = key;
        this.subCategories = subCats;
    }

    public String getKey() {
        return key;
    }

    public PermissionsSubCategory[] getSubCategories() {
        return subCategories;
    }

    public void isValidPermissions(String permissionToCheck){
        
    }

    public static boolean isValidPermission(String permissionToCheck) {
        String[] parts = permissionToCheck.split("\\.");

        if (!(parts.length >= 4 && parts.length <= 7)){
            return false;
        }

        String categoryKey;
        String subCategoryKey;
        String permissionLevel;

        if (parts.length == 4) {
            categoryKey = parts[0] + "." + parts[1];
            subCategoryKey = parts[2];
            permissionLevel = parts[3];
        } else {
            categoryKey = parts[0] + "." + parts[1];
            subCategoryKey = parts[2] + "." + parts[3];
            String customizationType = parts[4];
            if (!"custom".equalsIgnoreCase(customizationType)) {
                return false;
            }
            permissionLevel = (parts.length == 6) ? parts[5] : parts[6];
        }

        for (PermissionsCategory category : PermissionsCategory.values()) {
            if (category.getKey().equalsIgnoreCase(categoryKey)) {
                for (PermissionsSubCategory subCategory : category.getSubCategories()) {
                    if (subCategory.getSubCategoryKey().equalsIgnoreCase(subCategoryKey)) {
                        return subCategory.isPermissionValid(permissionLevel);
                    }
                }
            }
        }
        return false;
    }

}


