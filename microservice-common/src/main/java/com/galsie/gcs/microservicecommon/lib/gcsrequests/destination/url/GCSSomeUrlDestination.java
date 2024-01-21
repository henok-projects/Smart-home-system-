package com.galsie.gcs.microservicecommon.lib.gcsrequests.destination.url;

import com.galsie.gcs.microservicecommon.lib.gcsrequests.destination.RemoteRequestProtocolType;
import com.galsie.gcs.microservicecommon.lib.gcsrequests.destination.GCSRemoteRequestDestination;
import com.galsie.lib.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GCSSomeUrlDestination implements GCSRemoteRequestDestination {

    private RemoteRequestProtocolType remoteRequestProtocolType;
    private String domain;
    private String[] paths;

    public GCSSomeUrlDestination(RemoteRequestProtocolType remoteRequestProtocolType, String domain, String... paths){
        this.remoteRequestProtocolType = remoteRequestProtocolType;
        this.domain = domain;
        this.paths = paths;
    }


    @Override
    public String getDestinationUri() {
        return remoteRequestProtocolType.constructRequestPath(StringUtils.joinPathsWithStart(domain, this.paths));
    }
}
