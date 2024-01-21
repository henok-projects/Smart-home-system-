package com.galsie.gcs.microservicecommon.lib.deserialize.sms;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galsie.gcs.microservicecommon.lib.sms.data.discrete.SMSType;
import com.galsie.gcs.microservicecommon.lib.sms.data.dto.request.SMSBatchRequestDTO;
import com.galsie.gcs.microservicecommon.lib.sms.data.dto.request.SMSMultiMapAndPhoneNumbersBatchRequestDTO;
import com.galsie.gcs.microservicecommon.lib.sms.data.dto.request.SMSOneMapMultiplePhoneNumbersBatchRequestDTO;
import com.galsie.gcs.microservicecommon.lib.sms.data.dto.request.SMSRequestDTO;
import com.galsie.lib.utils.pair.Pair;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
public class DeserializerSMSBatchRequestDTO extends JsonDeserializer<SMSBatchRequestDTO> {

    @Override
    public SMSBatchRequestDTO deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode node = mapper.readTree(p);
        if(node.has("smsRequestDTOList")){
            var smsRequestDTOList = mapper.convertValue(node.get("smsRequestDTOList"), new TypeReference<List<SMSRequestDTO>>(){});
            return new SMSMultiMapAndPhoneNumbersBatchRequestDTO(smsRequestDTOList);
        }if(node.has("smsType")){
            var smsType = mapper.convertValue(node.get("smsType"), SMSType.class);
            var phoneNumberList = mapper.convertValue(node.get("phoneNumberList"), new TypeReference<List<Pair<Short, String>>>(){});
            var variableReplacementMap = mapper.convertValue(node.get("variableReplacementMap"), new TypeReference<Map<String, String>>(){});
            return new SMSOneMapMultiplePhoneNumbersBatchRequestDTO(smsType, phoneNumberList, variableReplacementMap);
        }
        log.error("Could not deserialize SMSBatchRequestDTO");
        throw new IOException("Could not deserialize SMSBatchRequestDTO");
    }
}
