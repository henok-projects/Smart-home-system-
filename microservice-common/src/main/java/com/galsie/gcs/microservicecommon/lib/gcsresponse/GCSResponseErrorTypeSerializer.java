package com.galsie.gcs.microservicecommon.lib.gcsresponse;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class GCSResponseErrorTypeSerializer extends StdSerializer<GCSResponseErrorType> {
    protected GCSResponseErrorTypeSerializer() {
        super(GCSResponseErrorType.class);
    }

    @Override
    public void serialize(GCSResponseErrorType gcsResponseErrorType, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        /**
         * NOTE: we don't serialize the message because it's the default message, which is included in {@link com.galsie.gcs.microservicecommon.lib.gcsresponse.data.dto.GCSResponseErrorDTO}
         */
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("error_definition", gcsResponseErrorType.name());
        jsonGenerator.writeObjectField("error_code", gcsResponseErrorType.getErrorCode());
        jsonGenerator.writeEndObject();
    }
}
