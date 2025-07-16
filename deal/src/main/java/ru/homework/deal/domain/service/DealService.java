package ru.homework.deal.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.homework.deal.domain.dto.DealContractorDto;
import ru.homework.deal.domain.dto.DealDto;
import ru.homework.deal.domain.dto.DealSearchRequest;

import java.util.UUID;

public interface DealService {
	DealDto save(DealDto dto);
	DealDto changeStatus(UUID id, String status);
	DealDto getById(UUID id);
	Page<DealDto> search(DealSearchRequest req, Pageable pageable);
	byte[] exportToExcel(DealSearchRequest req, Pageable pageable);

	DealContractorDto saveContractor(DealContractorDto dto);
	void deleteContractor(UUID id);
	void addRoleToContractor(UUID contractorId, String roleId);
	void removeRoleFromContractor(UUID contractorId, String roleId);
}