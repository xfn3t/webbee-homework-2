package ru.homework.deal.domain.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.homework.deal.domain.dto.DealContractorDto;
import ru.homework.deal.domain.service.DealService;

import java.util.UUID;

@RestController
@RequestMapping("/deal-contractor")
@RequiredArgsConstructor
@Tag(name="Deal Contractor API")
public class DealContractorController {

	private final DealService service;

	@PutMapping("/save")
	public ResponseEntity<DealContractorDto> save(@RequestBody DealContractorDto dto) {
		return ResponseEntity.ok(service.saveContractor(dto));
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Void> delete(@RequestParam UUID id) {
		service.deleteContractor(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/contractor-to-role/add")
	public ResponseEntity<Void> addRole(
			@RequestParam UUID contractorId,
			@RequestParam String roleId
	) {
		service.addRoleToContractor(contractorId, roleId);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/contractor-to-role/delete")
	public ResponseEntity<Void> removeRole(
			@RequestParam UUID contractorId,
			@RequestParam String roleId
	) {
		service.removeRoleFromContractor(contractorId, roleId);
		return ResponseEntity.noContent().build();
	}
}
