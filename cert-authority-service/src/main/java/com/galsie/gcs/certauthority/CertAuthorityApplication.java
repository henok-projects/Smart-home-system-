package com.galsie.gcs.certauthority;

import com.galsie.gcs.microservicecommon.GCSMicroserviceApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CertAuthorityApplication extends GCSMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CertAuthorityApplication.class, args);
	}

}
