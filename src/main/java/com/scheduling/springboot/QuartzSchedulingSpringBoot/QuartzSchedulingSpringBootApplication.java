package com.scheduling.springboot.QuartzSchedulingSpringBoot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"controller", "jobs"})
public class QuartzSchedulingSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuartzSchedulingSpringBootApplication.class, args);
	}

}
