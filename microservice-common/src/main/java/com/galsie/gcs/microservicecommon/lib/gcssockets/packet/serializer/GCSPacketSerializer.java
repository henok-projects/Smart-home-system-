package com.galsie.gcs.microservicecommon.lib.gcssockets.packet.serializer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galsie.gcs.microservicecommon.lib.gcssockets.packet.Packet;
import com.galsie.gcs.microservicecommon.lib.gcssockets.packet.AbstractPacketType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Getter
@Slf4j
public class GCSPacketSerializer {
    private ObjectMapper objectMapper;
    private Map<Long, AbstractPacketType> packetTypeList;

    public <T extends Packet> Optional<String> serialize(T packet){
        try {
            return Optional.of(objectMapper.writeValueAsString(packet));
        }catch (Exception ex){
            return Optional.empty();
        }
    }

    public Optional<Packet> deserialize(String json){
        try{
            return Optional.of(this.auxDeserialize(json));
        }catch (Exception ex){
            log.error("Failed to de-serialize a packet from the json: " + json + "reason: " + ex);
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    private Packet auxDeserialize(String json) throws Exception {
        var jsonData = objectMapper.readValue(json, new TypeReference<HashMap<String, String>>(){});
        var packetId = Long.valueOf(jsonData.get(Packet.PACKET_ID_JSON_KEY));
        if (!packetTypeList.containsKey(packetId)){
            throw new Exception("The packet id " + packetId + " was not found");
        }
        var packetClass = packetTypeList.get(packetId).getPacketClass();
        return objectMapper.readValue(json, packetClass);
    }

}
