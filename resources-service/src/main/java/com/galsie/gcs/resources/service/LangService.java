package com.galsie.gcs.resources.service;

import org.springframework.stereotype.Service;
@Service
public class LangService {
    /*
    private HashMap<Lang, LoadedLanguage> languages;
    public LangService(){
        languages = (HashMap<Lang, LoadedLanguage>) Arrays.stream(Lang.values()).collect(Collectors.toMap((key) -> key, LoadedLanguage::fromLang));
    }

    public LoadedLanguage getLanguage(Lang lang){
        return languages.get(lang);
    }

    public LoadedLangFile getLangFile(Lang lang, LangFile file){
        return languages.get(lang).getLangFile(file);
    }

    public String getLanguageVersion(Lang lang, LangFile file){
        return this.getLangFile(lang, file).getVersion();
    }


    public GCSResponse<SynchronizeLanguageResponseDTO> synchroniseLanguage(SynchronizeLanguageRequestDTO requestDTO){
        Lang lang = requestDTO.getLang();
        HashMap<LangFile, LoadedLangFile> updatedFiles = new HashMap<>();
        requestDTO.getVersions().forEach((langFile, version) -> {
            LoadedLangFile loadedLangFile =  this.getLangFile(lang, langFile);
            if (loadedLangFile.getVersion().trim().equals(version.trim())){
                return; // if same version, skip
            }
            updatedFiles.put(langFile, loadedLangFile);
        });
        return GCSResponse.successResponse(new SynchronizeLanguageResponseDTO(lang, updatedFiles));
    }*/

}
