package ru.homework.core.config;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.junit.jupiter.TestcontainersExtension;

@Testcontainers
@ExtendWith(TestcontainersExtension.class)
public abstract class BaseTestcontainers {
	@Container
	public static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
			new PostgreSQLContainer<>("postgres:16-alpine")
					.withDatabaseName("testdb")
					.withUsername("postgres")
					.withPassword("postgres");

	static {
		POSTGRESQL_CONTAINER.start();
	}

	@DynamicPropertySource
	static void registerPgProperties(DynamicPropertyRegistry registry) {

		registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
		registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
		registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);

		// Активация тестового профиля
//		registry.add("spring.profiles.active", () -> "test");
	}
}
