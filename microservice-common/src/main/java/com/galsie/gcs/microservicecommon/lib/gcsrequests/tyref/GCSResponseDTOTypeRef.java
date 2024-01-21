package com.galsie.gcs.microservicecommon.lib.gcsrequests.tyref;

import com.galsie.gcs.microservicecommon.lib.gcsresponse.data.dto.GCSResponseDTO;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.Type;
// This is a helper class that extends Spring's ParameterizedTypeReference,
// which is a way to carry generic type information at runtime.
public class GCSResponseDTOTypeRef<V> extends ParameterizedTypeReference<GCSResponseDTO<V>> {

    // This stores the type for the generic parameter V
    private final Class<V> type;

    // Constructor that accepts a Class object representing the type for V
    public GCSResponseDTOTypeRef(Class<V> type) {
        this.type = type;
    }

    // This method is overridden to return a specific Type object that
    // represents GCSResponseDTO with a specific type argument.
    // Here, we're using Apache's TypeUtils to create this Type object,
    // but similar functionality can be achieved with other libraries or custom code.
    @Override
    public Type getType() {
        // parameterize() creates a new ParameterizedType instance based on
        // the given raw type (GCSResponseDTO) and array of type arguments (type).
        return TypeUtils.parameterize(GCSResponseDTO.class, type);
    }
}
