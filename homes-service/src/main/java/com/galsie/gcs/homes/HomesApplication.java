package com.galsie.gcs.homes;

import com.galsie.gcs.microservicecommon.GCSMicroserviceApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HomesApplication extends GCSMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomesApplication.class, args);
	}

}
