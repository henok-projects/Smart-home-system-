package com.galsie.gcs.microservicecommon.lib.galassets.core.loaded.asset;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galsie.gcs.microservicecommon.lib.galassets.core.AssetVersioningType;
import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.util.Arrays;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.InputStream;
import java.util.Objects;

@Getter
@Setter
public class LoadedXmlAssetFile extends LoadedAssetFile {

    private ObjectMapper mapper = new ObjectMapper();

    private Document document;
    public LoadedXmlAssetFile(String version, Document document){
        super(version);
        this.document = document;
    }

    @Override
    public <T> T toDTO(Class<T> dtoType) throws Exception {
        try {
            return mapper.convertValue(this, dtoType);
        }catch (IllegalArgumentException e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public String getBase64EncodedData(boolean includeFileVersion){
        if(includeFileVersion) return Base64.encodeBase64String(Arrays.concatenate(getVersion().getBytes(),this.document.html().getBytes()));
        return Base64.encodeBase64String(this.document.html().getBytes());
    }

    public static LoadedXmlAssetFile fromRawStringData(String path, String data, AssetVersioningType versioningType) throws Exception{
        //var doc = Jsoup.parse(data, Parser.xmlParser());
        var doc = Jsoup.parse(data, "", Parser.xmlParser());
        String version = switch (versioningType) {
            case FILE_VERSIONED -> Objects.requireNonNull(doc.select("root > " + AssetVersioningType.FILE_VERSION_KEY_NAME).first()).text(); // throw null pointer exception if not found
            case DIRECTORY_VERSIONED -> getVersionFromPath(path);
        };
        if (version == null){
            throw new Exception("Couldn't create new instance: Failed to load version for file '" + path + "' with versioning type " + versioningType);
        }
        return new LoadedXmlAssetFile(version, doc);
    }
    public static LoadedXmlAssetFile fromInputStream(String path, InputStream data, AssetVersioningType versioningType) throws Exception {
        return  LoadedXmlAssetFile.fromRawStringData(path, new String(data.readAllBytes()), versioningType);
    }

}
