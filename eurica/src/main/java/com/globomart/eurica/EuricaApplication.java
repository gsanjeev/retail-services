package com.globomart.eurica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EuricaApplication {

	public static void main(String[] args) {

		SpringApplication.run(EuricaApplication.class, args);
	}
}
