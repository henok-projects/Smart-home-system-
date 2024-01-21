package com.galsie.gcs.microservicecommon.lib.galassets.providableassetdto.providedasset;

/**
 * A base class for all non-mock providable asset DTOs.
 */

public abstract class ProvidedAssetDTO implements AbstractProvidedAssetDTO{

    public abstract boolean valid();
}
