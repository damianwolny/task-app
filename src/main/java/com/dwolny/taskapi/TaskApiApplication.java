package com.dwolny.taskapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class TaskApiApplication {

	public static void main(String[] args) {

		SpringApplication.run(TaskApiApplication.class, args);
	}

}
