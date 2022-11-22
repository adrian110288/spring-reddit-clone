package com.adrianlesniak.redditclone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.validation.Validation;
import javax.validation.Validator;

@SpringBootApplication
public class RedditCloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedditCloneApplication.class, args);
	}

	@Bean
	public Validator getValidator() {
		return Validation.buildDefaultValidatorFactory().getValidator();
	}

}
