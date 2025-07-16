package ru.homework.core.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
		return builder -> {
			builder.serializationInclusion(JsonInclude.Include.NON_NULL);
			builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			builder.simpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		};
	}
}