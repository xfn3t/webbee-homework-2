package ru.homework.domain.contractor.service;

import ru.homework.domain.contractor.dto.ContractorDto;
import ru.homework.domain.contractor.dto.ContractorFullDto;
import ru.homework.domain.contractor.dto.ContractorFilter;
import ru.homework.core.util.pagination.PaginatedResponse;

import java.util.Optional;

public interface ContractorService {

	ContractorFullDto saveContractor(ContractorDto dto);

	Optional<ContractorFullDto> getContractorById(String id);

	void deactivateContractor(String id);

	PaginatedResponse<ContractorFullDto> searchContractors(
			ContractorFilter filter,
			int page,
			int size
	);
}