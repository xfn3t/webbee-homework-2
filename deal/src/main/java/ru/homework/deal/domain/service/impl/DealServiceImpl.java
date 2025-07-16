package ru.homework.deal.domain.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.homework.deal.domain.dto.*;
import ru.homework.deal.domain.mapper.*;
import ru.homework.deal.domain.model.*;
import ru.homework.deal.domain.repository.*;
import ru.homework.deal.domain.service.DealExcelExporter;
import ru.homework.deal.domain.service.DealService;
import ru.homework.deal.domain.service.DealSpecification;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {

	private final DealRepository dealRepo;
	private final DealMapper dealMapper;
	private final DealContractorRepository contrRepo;
	private final DealContractorMapper contrMapper;
	private final ContractorRoleRepository roleRepo;
	private final DealTypeRepository typeRepo;
	private final DealStatusRepository statusRepo;
	private final CurrencyRepository currencyRepo;
	private final SumMapper sumMapper;
	private final DealExcelExporter dealExcelExporter;

	@Transactional
	public DealDto save(DealDto dto) {
		Deal entity = dto.getId() != null
				? dealRepo.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("Deal not found"))
				: new Deal();

		// Обновление справочных объектов
		if (dto.getType() != null) {
			DealType type = typeRepo.findById(dto.getType().getId())
					.orElseThrow(() -> new EntityNotFoundException("Deal type not found"));
			entity.setType(type);
		}

		if (dto.getStatus() != null) {
			DealStatus status = statusRepo.findById(dto.getStatus().getId())
					.orElseThrow(() -> new EntityNotFoundException("Deal status not found"));
			entity.setStatus(status);
		}

		// Обновление суммы
		if (dto.getSum() != null) {
			updateDealSum(entity, dto.getSum());
		}

		// Обновление контрагентов
		if (dto.getContractors() != null) {
			updateContractors(entity, dto.getContractors());
		}

		// Обновление основных полей
		dealMapper.updateDealFromDto(entity, dto);

		if (dto.getId() == null) {
			entity.setStatus(statusRepo.findById("DRAFT")
					.orElseThrow(() -> new EntityNotFoundException("DRAFT status not found")));
		}

		entity.setModifyDate(LocalDateTime.now());
		Deal saved = dealRepo.save(entity);
		return dealMapper.toDto(saved);
	}

	private void updateDealSum(Deal deal, SumDto sumDto) {
		// Удаляем существующие главные суммы
		deal.getSums().removeIf(DealSum::isMain);

		// Создаем новую главную сумму
		DealSum newSum = sumMapper.toEntity(sumDto);
		newSum.setDeal(deal);
		newSum.setMain(true);
		deal.getSums().add(newSum);
	}

	private void updateContractors(Deal deal, List<DealContractorDto> contractorDtos) {
		// Сохраняем существующих активных контрагентов
		Map<UUID, DealContractor> existingContractors = deal.getContractors().stream()
				.filter(DealContractor::isActive)
				.collect(Collectors.toMap(DealContractor::getId, c -> c));

		List<DealContractor> updatedContractors = new ArrayList<>();

		for (DealContractorDto dto : contractorDtos) {
			DealContractor contractor;
			if (dto.getId() != null && existingContractors.containsKey(dto.getId())) {
				contractor = existingContractors.get(dto.getId());
				contrMapper.updateFromDto(dto, contractor);
			} else {
				contractor = contrMapper.toEntity(dto);
				contractor.setDeal(deal);
			}
			updatedContractors.add(contractor);
		}

		// Деактивируем отсутствующих контрагентов
		deal.getContractors().forEach(c -> {
			if (!updatedContractors.contains(c)) {
				c.setActive(false);
			}
		});

		// Добавляем новых
		deal.getContractors().addAll(updatedContractors.stream()
				.filter(c -> c.getId() == null)
				.toList());
	}

	@Override
	@Transactional
	public DealDto changeStatus(UUID id, String statusId) {
		Deal deal = dealRepo.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Deal not found"));

		DealStatus newStatus = statusRepo.findById(statusId)
				.orElseThrow(() -> new EntityNotFoundException("Status not found"));

		deal.setStatus(newStatus);
		deal.setModifyDate(LocalDateTime.now());

		return dealMapper.toDto(dealRepo.save(deal));
	}

	@Override
	@Transactional(readOnly = true)
	public DealDto getById(UUID id) {
		return dealRepo.findById(id)
				.map(dealMapper::toDto)
				.orElseThrow(() -> new EntityNotFoundException("Deal not found"));
	}

	@Override
	@Transactional(readOnly = true)
	public Page<DealDto> search(DealSearchRequest req, Pageable pageable) {
		return dealRepo.findAll(DealSpecification.byCriteria(req), pageable)
				.map(dealMapper::toDto);
	}

	@Override
	@Transactional
	public byte[] exportToExcel(DealSearchRequest req, Pageable pageable) {
		Page<Deal> page = dealRepo.findAll(DealSpecification.byCriteria(req), pageable);
		return dealExcelExporter.export(page);
	}


	@Override
	@Transactional
	public DealContractorDto saveContractor(DealContractorDto dto) {
		DealContractor entity;
		if (dto.getId() != null) {
			entity = contrRepo.findById(dto.getId())
					.orElseThrow(() -> new EntityNotFoundException("Contractor not found"));
			contrMapper.updateFromDto(dto, entity);
		} else {
			entity = contrMapper.toEntity(dto);
			Deal deal = dealRepo.findById(dto.getDealId())
					.orElseThrow(() -> new EntityNotFoundException("Deal not found"));
			entity.setDeal(deal);
		}

		// Обновление ролей
		if (dto.getRoles() != null) {
			updateContractorRoles(entity, dto.getRoles());
		}

		entity.setModifyDate(LocalDateTime.now());
		DealContractor saved = contrRepo.save(entity);
		return contrMapper.toDto(saved);
	}

	private void updateContractorRoles(DealContractor contractor, List<ContractorRoleDto> roleDtos) {
		Set<ContractorRole> newRoles = roleDtos.stream()
				.map(dto -> roleRepo.findById(dto.getId())
						.orElseThrow(() -> new EntityNotFoundException("Role not found: " + dto.getId())))
				.collect(Collectors.toSet());

		contractor.getRoles().retainAll(newRoles);
		contractor.getRoles().addAll(newRoles);
	}

	@Override
	@Transactional
	public void deleteContractor(UUID id) {
		DealContractor contractor = contrRepo.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Contractor not found"));
		contractor.setActive(false);
		contrRepo.save(contractor);
	}

	@Override
	@Transactional
	public void addRoleToContractor(UUID contractorId, String roleId) {
		DealContractor contractor = contrRepo.findById(contractorId)
				.orElseThrow(() -> new EntityNotFoundException("Contractor not found"));

		ContractorRole role = roleRepo.findById(roleId)
				.orElseThrow(() -> new EntityNotFoundException("Role not found"));

		contractor.getRoles().add(role);
		contrRepo.save(contractor);
	}

	@Override
	@Transactional
	public void removeRoleFromContractor(UUID contractorId, String roleId) {
		DealContractor contractor = contrRepo.findById(contractorId)
				.orElseThrow(() -> new EntityNotFoundException("Contractor not found"));

		contractor.getRoles().removeIf(role -> role.getId().equals(roleId));
		contrRepo.save(contractor);
	}
}