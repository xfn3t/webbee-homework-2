package ru.homework.deal.domain.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ru.homework.deal.domain.dto.DealDto;
import ru.homework.deal.domain.dto.DealSearchRequest;
import ru.homework.deal.domain.dto.DealSearchResponse;
import ru.homework.deal.domain.service.DealService;


import java.util.UUID;

@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
@Tag(name="Deal API")
public class DealController {
	private final DealService service;

	@PutMapping("/save")
	public ResponseEntity<DealDto> save(@RequestBody DealDto dto) {
		return ResponseEntity.ok(service.save(dto));
	}

	@PatchMapping("/change/status")
	public ResponseEntity<DealDto> changeStatus(
			@RequestParam UUID id,
			@RequestParam String status
	) {
		return ResponseEntity.ok(service.changeStatus(id, status));
	}

	@GetMapping("/{id}")
	public ResponseEntity<DealDto> get(@PathVariable UUID id) {
		return ResponseEntity.ok(service.getById(id));
	}

	@PostMapping("/search")
	public ResponseEntity<DealSearchResponse> search(
			@RequestBody DealSearchRequest req,
			@ParameterObject Pageable pageable
	) {
		Page<DealDto> page = service.search(req, pageable);
		return ResponseEntity.ok(new DealSearchResponse(
				page.getTotalElements(),
				page.getTotalPages(),
				page.getContent()
		));
	}

	@PostMapping(value = "/search/export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> export(
			@RequestBody DealSearchRequest req,
			@ParameterObject Pageable pageable
	) {
		byte[] data = service.exportToExcel(req, pageable);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=deals.xlsx")
				.body(data);
	}
}
