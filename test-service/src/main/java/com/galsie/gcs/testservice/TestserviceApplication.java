package com.galsie.gcs.testservice;

import com.galsie.gcs.microservicecommon.GCSMicroserviceApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestserviceApplication extends GCSMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestserviceApplication.class, args);
	}
}
