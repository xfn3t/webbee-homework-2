package ru.homework.domain.contractor.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.homework.domain.contractor.dto.ContractorDto;
import ru.homework.domain.contractor.dto.ContractorFullDto;
import ru.homework.domain.contractor.mapper.ContractorMapper;
import ru.homework.domain.contractor.service.ContractorService;
import ru.homework.domain.contractor.model.Contractor;
import ru.homework.domain.contractor.dto.ContractorFilter;
import ru.homework.core.util.pagination.PaginatedResponse;
import ru.homework.domain.contractor.repository.ContractorRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContractorServiceImpl implements ContractorService {

	private final ContractorRepository contractorRepository;
	private final ContractorMapper contractorMapper;

	@Transactional
	public ContractorFullDto saveContractor(ContractorDto dto) {

		Contractor contractor = contractorMapper.toEntity(dto);

		if (contractor.getId() == null || !contractorRepository.existsById(contractor.getId())) {
			contractor.setActive(true);
		}

		contractorRepository.save(contractor);
		return getContractorById(contractor.getId()).orElse(null);
	}

	@Transactional(readOnly = true)
	public Optional<ContractorFullDto> getContractorById(String id) {
		return contractorRepository.findById(id)
				.map(contractorMapper::toFullDto);
	}

	@Transactional
	public void deactivateContractor(String id) {
		contractorRepository.deactivateById(id);
	}

	@Transactional(readOnly = true)
	public PaginatedResponse<ContractorFullDto> searchContractors(
			ContractorFilter filter,
			int page,
			int size
	) {
		int offset = page * size;

		List<Contractor> contractors = contractorRepository.search(
				new ContractorFilter(
					filter.getContractorId(),
					filter.getParentId(),
					filter.getSearchTerm(),
					filter.getCountry(),
					filter.getIndustry(),
					filter.getOrgForm()
				),
				size,
				offset
		);

		List<ContractorFullDto> dtos = contractors.stream()
				.map(contractorMapper::toFullDto)
				.collect(Collectors.toList());

		int total = contractorRepository.countSearchResults(
				new ContractorFilter(
					filter.getContractorId(),
					filter.getParentId(),
					filter.getSearchTerm(),
					filter.getCountry(),
					filter.getIndustry(),
					filter.getOrgForm()
				)
		);

		return new PaginatedResponse<>(
				dtos,
				page,
				size,
				total
		);
	}
}