package com.galsie.gcs.microservicecommon.lib.galassets.core;

public enum AssetVersioningType {
    /**
     * VERSIONED_FILE_YML: The asset has yml files, or directories that have yml files. The yml files themselves are versioned by version: 'major.minor'
     * VERSIONED_DIRECTORY_FILES: asset has files. files are in directories. Directories are versioned by @major.minor
     * VERSIONED_FILE_XML: asset has xmls, or directories that have xmls. The xml files themselves are versioned
     */
    FILE_VERSIONED, DIRECTORY_VERSIONED;

    public static final String FILE_VERSION_KEY_NAME = "assets_file_version";
}
