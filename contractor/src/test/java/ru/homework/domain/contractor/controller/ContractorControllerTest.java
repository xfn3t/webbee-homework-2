package ru.homework.domain.contractor.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import ru.homework.core.config.BaseTestcontainers;
import ru.homework.domain.contractor.dto.ContractorDto;
import ru.homework.domain.contractor.model.Contractor;
import ru.homework.domain.contractor.dto.ContractorFilter;
import ru.homework.core.util.pagination.PaginatedResponse;
import ru.homework.domain.contractor.repository.ContractorRepository;

import java.time.LocalDateTime;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ContractorControllerTest extends BaseTestcontainers {

	@LocalServerPort
	private int port;

	@Autowired
	private ContractorRepository contractorRepository;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
		contractorRepository.deleteAll();
	}

	@Test
	void saveContractorShouldCreateNewContractor() {
		String uniqueId = "CTR-" + UUID.randomUUID().toString().substring(0, 8);

		ContractorDto request = new ContractorDto();
		request.setId(uniqueId);
		request.setName("Test Contractor");

		// Добавляем подробное логирование
		Response response = given()
				.contentType(ContentType.JSON)
				.body(request)
				.log().all()  // Логируем запрос
				.when()
				.put("/contractor/save");

		response.then()
				.log().all()  // Логируем ответ
				.statusCode(200)
				.contentType(ContentType.JSON)
				.body("id", equalTo(uniqueId))
				.body("name", equalTo("Test Contractor"));
	}

	@Test
	void getContractorByIdShouldReturnContractor() {
		// Создаем контрагента напрямую через репозиторий
		String uniqueId = "CTR-" + UUID.randomUUID().toString().substring(0, 8);
		createTestContractor(uniqueId);

		// Запрос и проверка
		given()
				.pathParam("id", uniqueId)
				.when()
				.get("/contractor/{id}")
				.then()
				.statusCode(200)
				.body("id", equalTo(uniqueId));
	}

	@Test
	void getContractorByIdShouldReturn404WhenNotFound() {
		given()
				.pathParam("id", "NON_EXISTING_ID")
				.when()
				.get("/contractor/{id}")
				.then()
				.statusCode(404);
	}

	@Test
	void deactivateContractorShouldDeactivateContractor() {
		// Создаем контрагента
		String uniqueId = "CTR-" + UUID.randomUUID().toString().substring(0, 8);
		createTestContractor(uniqueId);

		// Деактивируем
		given()
				.pathParam("id", uniqueId)
				.when()
				.delete("/contractor/delete/{id}")
				.then()
				.statusCode(200);

		// Проверяем, что контрагент больше не доступен
		given()
				.pathParam("id", uniqueId)
				.when()
				.get("/contractor/{id}")
				.then()
				.statusCode(404);
	}

	@Test
	void searchContractorsShouldFindContractor() {
		// Создаем контрагента
		String uniqueId = "CTR-" + UUID.randomUUID().toString().substring(0, 8);
		createTestContractor(uniqueId);

		ContractorFilter filter = new ContractorFilter();
		filter.setSearchTerm("Test");

		// Отправка запроса и проверка
		var response = given()
				.contentType(ContentType.JSON)
				.body(filter)
				.queryParam("page", 0)
				.queryParam("size", 10)
				.when()
				.post("/contractor/search")
				.then()
				.statusCode(200)
				.extract()
				.as(PaginatedResponse.class);

		assertFalse(response.getContent().isEmpty());
		assertEquals(1, response.getTotal());
	}

	private void createTestContractor(String id) {
		Contractor contractor = new Contractor();
		contractor.setId(id);
		contractor.setName("Test Contractor " + id);
		contractor.setActive(true);
		contractor.setCreateDate(LocalDateTime.now());
		contractor.setModifyDate(LocalDateTime.now());
		contractor.setCreateUserId("system");
		contractor.setModifyUserId("system");
		contractorRepository.save(contractor);
	}
}