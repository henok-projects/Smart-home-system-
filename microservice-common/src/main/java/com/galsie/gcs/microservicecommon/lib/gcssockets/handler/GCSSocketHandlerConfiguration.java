package com.galsie.gcs.microservicecommon.lib.gcssockets.handler;

import com.sun.istack.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GCSSocketHandlerConfiguration {
    @NotNull
    GCSSocketHandler gcsSocketHandler;

    @NotNull
    String path;

}
