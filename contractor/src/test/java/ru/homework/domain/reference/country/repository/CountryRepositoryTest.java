package ru.homework.domain.reference.country.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.TestcontainersExtension;
import ru.homework.core.config.BaseTestcontainers;
import ru.homework.domain.reference.country.model.Country;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({ TestcontainersExtension.class, SpringExtension.class })
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CountryRepositoryTest extends BaseTestcontainers {

	@Autowired
	private CountryRepository countryRepository;

	@BeforeEach
	void initTestData() {
		countryRepository.deleteAll();
	}


	@Test
	void shouldSaveAndFindCountryById() {
		String countryId = "US-" + UUID.randomUUID().toString().substring(0, 4);

		Country country = new Country();
		country.setId(countryId);
		country.setName("United States");
		country.setActive(true);

		Country saved = countryRepository.insertCountry(country);
		assertEquals(countryId, saved.getId());

		Optional<Country> found = countryRepository.findById(countryId);
		assertTrue(found.isPresent());
		assertEquals("United States", found.get().getName());
		assertTrue(found.get().isActive());
	}

	@Test
	void shouldFindActiveCountries() {

		countryRepository.insertCountry(new Country("US", "United States", true));
		countryRepository.insertCountry(new Country("CA", "Canada", true));
		countryRepository.insertCountry(new Country("RU", "Russia", false));

		List<Country> activeCountries = countryRepository.findByIsActiveTrue();

		assertEquals(2, activeCountries.size());
		assertTrue(activeCountries.stream().anyMatch(c -> c.getId().equals("US")));
		assertTrue(activeCountries.stream().anyMatch(c -> c.getId().equals("CA")));
		assertFalse(activeCountries.stream().anyMatch(c -> c.getId().equals("RU")));
	}

	@Test
	void shouldUpdateCountry() {
		// 1) Генерируем ID и сразу вставляем новую запись через insertCountry()
		String countryId = "US-" + UUID.randomUUID().toString().substring(0, 4);
		countryRepository.insertCountry(new Country(countryId, "United States", true));

		// 2) Вызываем наш custom-UPDATE
		int updatedRows = countryRepository
				.updateNameAndActiveById(countryId, "USA", false);
		assertEquals(1, updatedRows, "Должна быть обновлена ровно одна строка");

		// 3) Проверяем результат
		Country updated = countryRepository.findById(countryId)
				.orElseThrow(() -> new AssertionError("Country not found after update"));
		assertEquals("USA", updated.getName());
		assertFalse(updated.isActive());
	}

	@Test
	void shouldDeleteCountry() {
		String countryId = "US-" + UUID.randomUUID().toString().substring(0, 4);
		countryRepository.insertCountry(new Country(countryId, "United States", true));

		countryRepository.deleteById(countryId);

		assertFalse(countryRepository.findById(countryId).isPresent());
	}
}
