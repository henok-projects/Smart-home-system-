package com.galsie.gcs.resources.service.assetsync;

import com.galsie.gcs.resources.data.entity.sync.GalAssetsSynchronizedFileEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.dto.GalDTO;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@GalDTO
@NoArgsConstructor
@Getter
@Setter
public class GalAssetsSynchronizedFileEntityCollection {

    @NotNull
    private final List<GalAssetsSynchronizedFileEntity> galAssetsSynchronizedFileEntityList = new LinkedList<>();

    public void addFilesPageSyncDTO(GalAssetsSynchronizedFileEntity galAssetsSynchronizedFileEntity){
        galAssetsSynchronizedFileEntityList.add(galAssetsSynchronizedFileEntity);
    }

}
