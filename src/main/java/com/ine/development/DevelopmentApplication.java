package com.ine.development;

import com.ine.development.config.EnvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DevelopmentApplication {

	public static void main(String[] args) {
		EnvConfig.loadEnv();
		SpringApplication.run(DevelopmentApplication.class, args);
	}

}
