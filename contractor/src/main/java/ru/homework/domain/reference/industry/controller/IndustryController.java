package ru.homework.domain.reference.industry.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.homework.domain.reference.industry.dto.IndustryDto;
import ru.homework.domain.reference.industry.model.Industry;
import ru.homework.domain.reference.industry.service.IndustryService;

import java.util.List;

@RestController
@RequestMapping("/industry")
@RequiredArgsConstructor
@Tag(name = "Управление отраслями", description = "API для работы с отраслями")
public class IndustryController {

	private final IndustryService industryService;

	@GetMapping
	@Operation(
			summary = "Получение всех активных отраслей",
			description = "Возвращает список всех активных отраслей",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Список отраслей получен",
							content = @Content(
									mediaType = MediaType.APPLICATION_JSON_VALUE,
									schema = @Schema(implementation = IndustryDto.class)
							)
					)
			}
	)
	public ResponseEntity<List<?>> getAllActive() {
		List<Industry> list = industryService.findByIsActiveTrue();
		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(list);
	}

	@GetMapping("/{id}")
	@Operation(
			summary = "Получение отрасли по ID",
			description = "Возвращает отрасль по её идентификатору",
			parameters = {
					@Parameter(
							name = "id",
							description = "Идентификатор отрасли",
							example = "1",
							required = true
					)
			},
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Отрасль найдена",
							content = @Content(
									mediaType = MediaType.APPLICATION_JSON_VALUE,
									schema = @Schema(implementation = IndustryDto.class),
									examples = {
											@ExampleObject(
													name = "Пример ответа",
													value = """
                                {
                                    "id": 1,
                                    "name": "Информационные технологии",
                                    "isActive": true
                                }
                                """
											)
									}
							)
					),
					@ApiResponse(responseCode = "404", description = "Отрасль не найдена")
			}
	)
	public ResponseEntity<IndustryDto> getById(@PathVariable Integer id) {
		return industryService.getIndustryById(id)
				.map(dto -> ResponseEntity.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(dto))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PutMapping("/save")
	@Operation(
			summary = "Создание или обновление отрасли",
			description = "Создает новую или обновляет существующую отрасль",
			requestBody = @RequestBody(
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = IndustryDto.class),
							examples = {
									@ExampleObject(
											name = "Пример запроса",
											value = """
                            {
                                "id": 1,
                                "name": "Биотехнологии",
                                "isActive": true
                            }
                            """
									)
							}
					)
			),
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Отрасль успешно сохранена",
							content = @Content(
									mediaType = MediaType.APPLICATION_JSON_VALUE,
									schema = @Schema(implementation = IndustryDto.class),
									examples = {
											@ExampleObject(
													name = "Пример ответа",
													value = """
                                {
                                    "id": 1,
                                    "name": "Биотехнологии",
                                    "isActive": true
                                }
                                """
											)
									}
							)
					)
			}
	)
	public ResponseEntity<IndustryDto> saveIndustry(@RequestBody IndustryDto industryDto) {
		IndustryDto saved = industryService.saveIndustry(industryDto);
		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(saved);
	}

	@DeleteMapping("/delete/{id}")
	@Operation(
			summary = "Деактивация отрасли",
			description = "Логическое удаление (деактивация) отрасли по идентификатору",
			parameters = {
					@Parameter(
							name = "id",
							description = "Идентификатор отрасли",
							example = "1",
							required = true
					)
			},
			responses = {
					@ApiResponse(responseCode = "200", description = "Отрасль деактивирована")
			}
	)
	public ResponseEntity<Void> deactivateIndustry(@PathVariable Integer id) {
		industryService.deactivateIndustry(id);
		return ResponseEntity.ok().build();
	}
}
