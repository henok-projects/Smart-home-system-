package com.galsie.gcs.resources;

import com.galsie.gcs.microservicecommon.GCSMicroserviceApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ResourcesApplication extends GCSMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResourcesApplication.class, args);
	}

}
