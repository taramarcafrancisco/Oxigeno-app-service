package com.oxigeno.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.oxigeno.portal.services.storage.StorageService;

 
 

 

@SpringBootApplication (scanBasePackages = "com.oxigeno.portal")
@EnableTransactionManagement
public class ServletInitializer extends SpringBootServletInitializer {
	@Autowired
	private StorageService storageService;
	
	public static void main(String[] args) {
		SpringApplication.run(ServletInitializer.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ServletInitializer.class);
	}

/*
	@Bean
	CommandLineRunner init() {
		return (args) -> {
			storageService.deleteAll();
			storageService.init();
		};
	}*/
}
