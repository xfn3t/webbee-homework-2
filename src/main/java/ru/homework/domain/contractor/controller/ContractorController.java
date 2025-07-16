package ru.homework.domain.contractor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.homework.domain.contractor.dto.ContractorDto;
import ru.homework.domain.contractor.dto.ContractorFullDto;
import ru.homework.domain.contractor.dto.ContractorFilter;
import ru.homework.core.util.pagination.PaginatedResponse;
import ru.homework.domain.contractor.service.ContractorService;

@RestController
@RequestMapping("/contractor")
@RequiredArgsConstructor
@Tag(name = "Управление контрагентами", description = "API для работы с контрагентами")
public class ContractorController {

	private final ContractorService contractorService;

	@PutMapping("/save")
	@Operation(
			summary = "Создание или обновление контрагента",
			description = "Создает нового или обновляет существующего контрагента",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					content = @Content(
							mediaType = "application/json",
							examples = {
									@ExampleObject(
											name = "Пример запроса",
											value = """
                        {
                            "id": "CTR12345",
                            "parentId": "CTR00001",
                            "name": "ООО Ромашка",
                            "nameFull": "Общество с ограниченной ответственностью Ромашка",
                            "inn": "1234567890",
                            "ogrn": "1234567890123",
                            "country": "RU",
                            "industry": 1,
                            "orgForm": 2
                        }
                        """
									)
							}
					)
			),
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Успешное сохранение",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(implementation = ContractorFullDto.class)
							)
					)
			}
	)
	public ResponseEntity<ContractorFullDto> saveContractor(@RequestBody ContractorDto contractorDto) {
		ContractorFullDto result = contractorService.saveContractor(contractorDto);
		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(result);
	}

	@GetMapping("/{id}")
	@Operation(
			summary = "Получение контрагента по ID",
			description = "Возвращает контрагента по его идентификатору",
			parameters = {
					@Parameter(
							name = "id",
							description = "Идентификатор контрагента",
							example = "CTR12345",
							required = true
					)
			},
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Контрагент найден",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(implementation = ContractorFullDto.class),
									examples = {
											@ExampleObject(
													name = "Пример ответа",
													value = """
                            {
                                "id": "CTR12345",
                                "parentId": "CTR00001",
                                "name": "ООО Ромашка",
                                "nameFull": "Общество с ограниченной ответственностью Ромашка",
                                "inn": "1234567890",
                                "ogrn": "1234567890123",
                                "country": "RU",
                                "industry": 1,
                                "orgForm": 2,
                                "createDate": "2023-01-15T10:30:00",
                                "modifyDate": "2023-01-20T14:45:00",
                                "createUserId": "user123",
                                "modifyUserId": "user456",
                                "isActive": true
                            }
                            """
											)
									}
							)
					),
					@ApiResponse(responseCode = "404", description = "Контрагент не найден")
			}
	)
	public ResponseEntity<ContractorFullDto> getContractorById(@PathVariable String id) {
		return contractorService.getContractorById(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@DeleteMapping("/delete/{id}")
	@Operation(
			summary = "Деактивация контрагента",
			description = "Логическое удаление контрагента по идентификатору",
			parameters = {
					@Parameter(
							name = "id",
							description = "Идентификатор контрагента",
							example = "CTR12345",
							required = true
					)
			},
			responses = {
					@ApiResponse(responseCode = "200", description = "Контрагент деактивирован")
			}
	)
	public ResponseEntity<Void> deactivateContractor(@PathVariable String id) {
		contractorService.deactivateContractor(id);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/search")
	@Operation(
			summary = "Поиск контрагентов",
			description = "Поиск активных контрагентов с фильтрами и пагинацией",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					content = @Content(
							mediaType = "application/json",
							examples = {
									@ExampleObject(
											name = "Пример запроса",
											value = """
                        {
                            "contractorId": "CTR12345",
                            "parentId": "CTR00001",
                            "searchTerm": "Ромашка",
                            "country": "Россия",
                            "industry": 1,
                            "orgForm": "ООО"
                        }
                        """
									)
							}
					)
			),
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Результаты поиска",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(implementation = PaginatedResponse.class),
									examples = {
											@ExampleObject(
													name = "Пример ответа",
													value = """
                            {
                                "content": [
                                    {
                                        "id": "CTR12345",
                                        "name": "ООО Ромашка",
                                        "nameFull": "Общество с ограниченной ответственностью Ромашка",
                                        "inn": "1234567890",
                                        "ogrn": "1234567890123",
                                        "country": "RU",
                                        "industry": 1,
                                        "orgForm": 2,
                                        "isActive": true
                                    }
                                ],
                                "page": 0,
                                "size": 10,
                                "total": 1
                            }
                            """
											)
									}
							)
					)
			}
	)
	public ResponseEntity<PaginatedResponse<ContractorFullDto>> searchContractors(
			@RequestBody ContractorFilter filter,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
	) {
		return ResponseEntity.ok(contractorService.searchContractors(filter, page, size));
	}
}