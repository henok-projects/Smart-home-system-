package com.galsie.gcs.smartdevicesservice;

import com.galsie.gcs.microservicecommon.GCSMicroserviceApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SmartDevicesServiceApplication extends GCSMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartDevicesServiceApplication.class, args);
	}

}
