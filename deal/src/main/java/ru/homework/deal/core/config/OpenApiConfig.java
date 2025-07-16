package ru.homework.deal.core.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import org.springframework.context.annotation.*;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI apiInfo() {
		return new OpenAPI()
				.info(new Info()
						.title("Deal Service API")
						.version("1.0")
						.description("REST API для управления сделками и контрагентами"));
	}
}
