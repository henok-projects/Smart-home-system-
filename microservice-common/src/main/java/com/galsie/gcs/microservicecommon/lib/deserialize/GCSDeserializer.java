package com.galsie.gcs.microservicecommon.lib.deserialize;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

@Slf4j
public class GCSDeserializer extends JsonDeserializer<Object> {

    private static final HashMap<Set<String>, String> classToFieldsMap = new HashMap<>();

//    @Override
//    public DeserializeBasePackage deserialize(JsonParser jP, DeserializationContext ctxt) throws IOException, JacksonException {
//        ObjectMapper mapper = (ObjectMapper) jP.getCodec();
//        JsonNode node = mapper.readTree(jP);
//        System.out.println(node);
//        if(node.has("providableAssetDTOType")){
//            return mapper.treeToValue(node, ProvidableAssetDTOWithType.class);
//        }
//        if(node.has("providableAssetResponseError")){
//            if(node.has("providedAssetDTO")){
//                return mapper.treeToValue(node, GetProvidableAssetDTOResponseDTO.class);
//            }
//            return mapper.treeToValue(node, GetProvidableAssetDTOListResponseDTO.class);
//        }
//        if(node.has("smsRequestDTOList")){
//            return mapper.treeToValue(node, SMSMultiMapAndPhoneNumbersBatchRequestDTO.class);
//        }if(node.has("smsType")){
//            return mapper.treeToValue(node, SMSOneMapMultiplePhoneNumbersBatchRequestDTO.class);
//        }
//        log.error("Couldn't deserialize json node: " + node);
//        throw new IOException("Couldn't deserialize json node");
//    }

    @Override
    public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
        log.info("Deserializing json node");
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode node = mapper.readTree(jp);
        System.out.println(node);
        for(var entry: classToFieldsMap.entrySet()){
            boolean hasAllKeys = true;
            for(var key: entry.getKey()){
                if(!node.has(key)){
                    hasAllKeys = false;
                    break;
                }
            }
            if(!hasAllKeys){
                continue;
            }
            log.info("about to map");
            try {
                var obj = mapper.readValue(node.toString(), Class.forName(entry.getValue()));
                log.info("mapped");
                log.info("obj: " + obj.getClass().getName());
                return obj;
            }catch (Exception e){
                log.error("Error mapping: " + node, e);
                return null;
            }
        }
        log.error("Couldn't deserialize json node: " + node);
        throw new IOException("Couldn't deserialize json node");
    }

//    public Object process(ObjectMapper mapper, JsonNode node, Set<String> keys, String className) throws JsonProcessingException {
//        boolean hasAllKeys = true;
//        for(var key: keys){
//            if(!node.has(key)){
//                hasAllKeys = false;
//                break;
//            }
//        }
//        log.info("hasAllKeys: " + hasAllKeys +" for keys: " + keys+" "+className);
//        if(!hasAllKeys){
//            return null;
//        }
//        log.info("about to map");
//        try {
//            var obj = mapper.treeToValue(node, Class.forName(className).getConstructor().newInstance().getClass());
//            log.info("mapped");
//            log.info("obj: " + obj.getClass().getName());
//            return obj;
//        }catch (Exception e){
//            log.error("Error mapping: " + node, e);
//            return null;
//        }
//    }

    public static void addClassToFields(String className, String[] fields){
        var fieldsSet = Set.of(fields);
        classToFieldsMap.put(fieldsSet, className);
        System.out.println("Added class to fields: " + className+ " " + fieldsSet);
        System.out.println("classToFieldsMap: " + classToFieldsMap);
    }
}
