package com.galsie.gcs.users.data.dto.getcontactinfo.request.many;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.galsie.gcs.users.data.dto.getcontactinfo.request.common.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetManyUserContactInfoRequestDTODeserializer extends StdDeserializer<GetManyUserContactInfoRequestDTO> {

    public GetManyUserContactInfoRequestDTODeserializer() {
        super(GetManyUserContactInfoRequestDTO.class);
    }

    @Override
    public GetManyUserContactInfoRequestDTO deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);

        GetManyUserContactInfoRequestDTO request = new GetManyUserContactInfoRequestDTO();

        if (node.has("for_users")) {
            JsonNode usersNode = node.get("for_users");
            if (usersNode.isArray()) {
                List<UserReferenceDTO> userReferences = new ArrayList<>();
                for (JsonNode userNode : usersNode) {
                    UserReferenceDTO userReference = null;
                    if (userNode.has("username")) {
                        userReference = parser.getCodec().treeToValue(userNode, UserReferenceByUsernameDTO.class);
                    } else if (userNode.has("email")) {
                        userReference = parser.getCodec().treeToValue(userNode, UserReferenceByEmailDTO.class);
                    } else if (userNode.has("phone")) {
                        userReference = parser.getCodec().treeToValue(userNode, UserReferenceByPhoneDTO.class);
                    } else if (userNode.has("user_id")) {
                        userReference = parser.getCodec().treeToValue(userNode, UserReferenceByIdDTO.class);
                    }
                    if (userReference != null) {
                        userReferences.add(userReference);
                    }
                }
                request.setForUsers(userReferences);
            }
        }

        if (node.has("include_profile_photos")) {
            request.setIncludeProfilePhotos(node.get("include_profile_photos").asBoolean());
        }

        return request;
    }
}


