package com.galsie.gcs.users.data.dto.getcontactinfo.request.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class UserReferenceDTODeserializer extends StdDeserializer<UserReferenceDTO> {

    public UserReferenceDTODeserializer() {
        super(UserReferenceDTO.class);
    }

    @Override
    public UserReferenceDTO deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);

        if (node.has("username")) {
            return parser.getCodec().treeToValue(node, UserReferenceByUsernameDTO.class);
        } else if (node.has("email")) {
            return parser.getCodec().treeToValue(node, UserReferenceByEmailDTO.class);
        } else if (node.has("phone")) {
            return parser.getCodec().treeToValue(node, UserReferenceByPhoneDTO.class);
        } else if (node.has("user_id")) {
            return parser.getCodec().treeToValue(node, UserReferenceByIdDTO.class);
        }

        return null; // Handle the case where no subtype matches
    }
}

