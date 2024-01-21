package com.galsie.gcs.resources.data.entity.sync;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.galsie.gcs.microservicecommon.lib.galassets.core.AssetGroupType;
import com.galsie.gcs.microservicecommon.lib.galassets.core.loaded.asset.LoadedAssetFile;
import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.assetsync.response.AssetGroupFileSyncedDataDTO;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.time.LocalDateTime;

/**
 * This entity is used for storing String file data related to an asset synchronization request, keeping important information
 * like page number for easy parsing based on {@link com.galsie.gcs.microservicecommon.lib.galassets.data.dto.assetsync.request.GetSynchronizedPageRequestDTO}.
 * Each entity is linked to a {@link GalAssetsSynchronizationEntity} which acts like a common parent for all entities related to the same request.
 */
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class GalAssetsSynchronizedFileEntity implements GalEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="unique_id")
    Long id;

    @ManyToOne
    private GalAssetsSynchronizationEntity galAssetsSynchronizationEntity;

    @Column
    private Long page;

    @Column
    private AssetGroupType assetGroupType;

    @Column
    private String path;

    @Column
    private String version;

    @JsonProperty("file_data")
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private Blob fileData;

    @UpdateTimestamp
    @Column
    private LocalDateTime modificationDate;

    public static GalAssetsSynchronizedFileEntity fromLoadedAssetFile(LoadedAssetFile assetFile, AssetGroupType assetGroupType, GalAssetsSynchronizationEntity galAssetsSynchronizationEntity, long page, String cleanPath) {
        Blob blob = null;
        try {
            blob = new SerialBlob(assetFile.getBase64EncodedData(false).getBytes());
        } catch (Exception e) {
            log.info("Couldn't create blob from asset file data");
        }
        return GalAssetsSynchronizedFileEntity.builder().galAssetsSynchronizationEntity(galAssetsSynchronizationEntity).version(assetFile.getVersion())
                .assetGroupType(assetGroupType)
                .path(cleanPath)
                .page(page)
                .fileData(blob).build();
    }

    //This method is here because the DTO exists in the microservices-common library
    public static AssetGroupFileSyncedDataDTO toAssetFileSyncedDataDTO(GalAssetsSynchronizedFileEntity galAssetsSynchronizedFileEntity) {
        var string ="";
        try{
            string = new String(galAssetsSynchronizedFileEntity.getFileData().getBinaryStream().readAllBytes());
        } catch (Exception e) {
            log.error("Couldn't convert blob to string");
        }
        return AssetGroupFileSyncedDataDTO.builder().version(galAssetsSynchronizedFileEntity.getVersion())
                .assetGroupType(galAssetsSynchronizedFileEntity.getAssetGroupType()).path(galAssetsSynchronizedFileEntity.getPath())
                .fileData(string).build();
    }
}
