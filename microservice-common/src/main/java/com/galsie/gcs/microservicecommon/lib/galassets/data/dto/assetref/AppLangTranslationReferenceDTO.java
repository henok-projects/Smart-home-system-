package com.galsie.gcs.microservicecommon.lib.galassets.data.dto.assetref;

import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.Nullable;

/**
 * Translations are found in GalAssets
 * - The dot joined translation path is provided here can be null indicating there is no translation
 *   - Mainly this is the case for {@link com.galsie.gcs.microservicecommon.lib.gcsresponse.data.dto.GCSResponseErrorDTO}s
 * - A fallback value exists so that services that wouldn't be accessing the resources service (and so can't parse the dot joined path) can still log errors.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@GalDTO
public class AppLangTranslationReferenceDTO {
    static Logger logger = LogManager.getLogger(AppLangTranslationReferenceDTO.class);

    @Nullable
    String dotJoinedPath;

    @NotNull
    String fallbackMessage;
}
