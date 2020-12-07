package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.example.demo.controller", "com.example.demo.utilities","com.example.demo.model","com.example.demo.controllerService","com.example.demo.seleniumScriptModules", "com.example.demo.StepDefinition"})
public class demoApplication {

	public static void main(String[] args) {
		SpringApplication.run(demoApplication.class, args);
		
		//System.out.println("Hello");
	}

}
