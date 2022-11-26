package com.adrianlesniak.redditclone;

import com.adrianlesniak.redditclone.config.SwaggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.validation.Validation;
import javax.validation.Validator;

@SpringBootApplication
@EnableAsync
@Import(SwaggerConfig.class)
public class RedditCloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedditCloneApplication.class, args);
	}

	@Bean
	public Validator getValidator() {
		return Validation.buildDefaultValidatorFactory().getValidator();
	}

}
