package com.yithsopheakktra.co.springblogapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringBlogApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBlogApiApplication.class, args);
	}

}
