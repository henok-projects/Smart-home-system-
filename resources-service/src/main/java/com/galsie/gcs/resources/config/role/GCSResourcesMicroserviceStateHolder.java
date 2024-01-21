package com.galsie.gcs.resources.config.role;

import com.galsie.gcs.resources.data.discrete.GCSResourceMicroserviceRole;
import com.galsie.gcs.resources.data.discrete.GCSResourceMicroserviceStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
public class GCSResourcesMicroserviceStateHolder {

    private  GCSResourceMicroserviceRole thisMicroserviceRole = GCSResourceMicroserviceRole.NORMAL;

    private GCSResourceMicroserviceStatus thisMicroserviceStatus = GCSResourceMicroserviceStatus.STARTING_UP;

}
