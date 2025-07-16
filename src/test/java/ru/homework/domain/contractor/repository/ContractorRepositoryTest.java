package ru.homework.domain.contractor.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.homework.core.config.BaseTestcontainers;
import ru.homework.domain.contractor.dto.ContractorFilter;
import ru.homework.domain.contractor.model.Contractor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class ContractorRepositoryTest extends BaseTestcontainers {

	@Autowired
	private ContractorRepository contractorRepository;

	@BeforeEach
	void clearDatabase() {
		contractorRepository.deleteAll();
	}

	@Test
	void shouldSaveAndFindContractor() {

		String uniqueId = "CTR-" + String.valueOf(System.currentTimeMillis()).substring(0, 4);

		Contractor c = new Contractor();
		c.setId(uniqueId);
		c.setName("Test Contractor " + uniqueId);
		c.setActive(true);

		contractorRepository.save(c);
		Optional<Contractor> found = contractorRepository.findById(uniqueId);
		assertTrue(found.isPresent());
		assertEquals("Test Contractor " + uniqueId, found.get().getName());
	}

	@Test
	void shouldDeactivateContractor() {
		String uniqueId = "CTR-" + String.valueOf(System.currentTimeMillis()).substring(0, 4);

		Contractor c = new Contractor();
		c.setId(uniqueId);
		c.setName("Test Contractor " + uniqueId);
		c.setActive(true);
		contractorRepository.save(c);

		contractorRepository.deactivateById(uniqueId);
		assertFalse(contractorRepository.findById(uniqueId).isPresent());
	}

	@Test
	void shouldSearchContractors() {
		// Создаем тестового контрагента
		Contractor testContractor = createTestContractor(null);

		// Ищем созданного контрагента
		List<Contractor> result = contractorRepository.search(
				new ContractorFilter(
					testContractor.getId(),
					null,
					"Test",
					null,
					null,
					null
				),
				10,
				0
		);

		// Проверяем результаты
		assertFalse(result.isEmpty());
		assertEquals(1, result.size(), "Должен быть найден ровно один контрагент");
		assertEquals(testContractor.getId(), result.getFirst().getId());
		assertEquals(testContractor.getName(), result.getFirst().getName());
	}

	private Contractor createTestContractor(String suffix) {
		String uniqueId = suffix != null ? suffix : "CTR-" + UUID.randomUUID().toString().substring(0, 8);
		String name = "Test Contractor " + uniqueId;

		Contractor c = new Contractor();
		c.setId(uniqueId);
		c.setName(name);
		c.setActive(true);
		contractorRepository.save(c);
		return c;
	}

}