package ru.homework.domain.contractor.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.homework.domain.contractor.dto.ContractorDto;
import ru.homework.domain.contractor.dto.ContractorFullDto;
import ru.homework.domain.contractor.mapper.ContractorMapper;
import ru.homework.domain.contractor.model.Contractor;
import ru.homework.domain.contractor.dto.ContractorFilter;
import ru.homework.core.util.pagination.PaginatedResponse;
import ru.homework.domain.contractor.repository.ContractorRepository;
import ru.homework.domain.contractor.service.impl.ContractorServiceImpl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContractorServiceTest {

	@Mock
	private ContractorRepository contractorRepository;

	@Mock
	private ContractorMapper contractorMapper;

	@InjectMocks
	private ContractorServiceImpl contractorService;

	private final String testId = "CTR-12345";
	private final LocalDateTime testDate = LocalDateTime.now();

	@Test
	void saveContractorShouldCreateNewContractor() {
		// Подготовка
		ContractorDto dto = new ContractorDto();
		dto.setId(testId);

		Contractor entity = new Contractor();
		entity.setId(testId);

		ContractorFullDto fullDto = createFullDto(testId);

		when(contractorMapper.toEntity(dto)).thenReturn(entity);
		when(contractorRepository.findById(testId)).thenReturn(Optional.of(entity));
		when(contractorMapper.toFullDto(entity)).thenReturn(fullDto);

		// Действие
		ContractorFullDto result = contractorService.saveContractor(dto);

		// Проверка
		assertNotNull(result);
		assertEquals(testId, result.getId());
		verify(contractorRepository).save(entity);
	}

	@Test
	void getContractorByIdShouldReturnContractor() {
		// Подготовка
		Contractor entity = new Contractor();
		entity.setId(testId);

		ContractorFullDto fullDto = createFullDto(testId);

		when(contractorRepository.findById(testId)).thenReturn(Optional.of(entity));
		when(contractorMapper.toFullDto(entity)).thenReturn(fullDto);

		// Действие
		Optional<ContractorFullDto> result = contractorService.getContractorById(testId);

		// Проверка
		assertTrue(result.isPresent());
		assertEquals(testId, result.get().getId());
	}

	@Test
	void getContractorByIdShouldReturnEmptyWhenNotFound() {
		// Подготовка
		when(contractorRepository.findById(testId)).thenReturn(Optional.empty());

		// Действие
		Optional<ContractorFullDto> result = contractorService.getContractorById(testId);

		// Проверка
		assertTrue(result.isEmpty());
	}

	@Test
	void deactivateContractorShouldCallRepository() {
		// Действие
		contractorService.deactivateContractor(testId);

		// Проверка
		verify(contractorRepository).deactivateById(testId);
	}

	@Test
	void searchContractorsShouldReturnPaginatedResults() {
		// Подготовка
		ContractorFilter filter = new ContractorFilter();
		filter.setSearchTerm("test");

		int page = 0;
		int size = 10;

		Contractor entity = new Contractor();
		entity.setId(testId);

		ContractorFullDto fullDto = createFullDto(testId);

		when(contractorRepository.search(
				any(ContractorFilter.class),
				anyInt(),
				anyInt()
		)).thenReturn(Collections.singletonList(entity));

		when(contractorRepository.countSearchResults(any(ContractorFilter.class))).thenReturn(1);

		when(contractorMapper.toFullDto(entity)).thenReturn(fullDto);

		// Действие
		PaginatedResponse<ContractorFullDto> result =
				contractorService.searchContractors(filter, page, size);

		// Проверка
		assertNotNull(result);
		assertEquals(1, result.getTotal());
		assertEquals(testId, result.getContent().get(0).getId());
	}

	private ContractorFullDto createFullDto(String id) {
		ContractorFullDto dto = new ContractorFullDto();
		dto.setId(id);
		dto.setName("Test Contractor");
		dto.setCreateDate(testDate);
		dto.setModifyDate(testDate);
		dto.setCreateUserId("system");
		dto.setModifyUserId("system");
		dto.setIsActive(true);
		return dto;
	}
}