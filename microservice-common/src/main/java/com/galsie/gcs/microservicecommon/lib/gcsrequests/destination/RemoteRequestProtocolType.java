package com.galsie.gcs.microservicecommon.lib.gcsrequests.destination;

import com.galsie.lib.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RemoteRequestProtocolType {
    HTTP("http"),
    HTTPS("https");

    private final String name;

    public String constructRequestPath(String... paths){
        return StringUtils.joinPathsWithStart(this.getName() + "://", paths);
    }

    public static final RemoteRequestProtocolType DEFAULT_PROTOCOL = RemoteRequestProtocolType.HTTP;
}
