package ru.homework.domain.reference.orgform.controller;

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
import ru.homework.domain.reference.orgform.dto.OrgFormDto;
import ru.homework.domain.reference.orgform.service.OrgFormService;

import java.util.List;

@RestController
@RequestMapping("/org_form")
@RequiredArgsConstructor
@Tag(name = "Управление организационными формами", description = "API для работы с организационными формами")
public class OrgFormController {

	private final OrgFormService orgFormService;

	@GetMapping
	@Operation(
			summary = "Получение всех активных организационных форм",
			description = "Возвращает список всех активных организационных форм",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Список организационных форм получен",
							content = @Content(
									mediaType = MediaType.APPLICATION_JSON_VALUE,
									schema = @Schema(implementation = OrgFormDto.class)
							)
					)
			}
	)
	public ResponseEntity<List<OrgFormDto>> getAllActive() {
		List<OrgFormDto> list = orgFormService.getAllActiveOrgForms();
		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(list);
	}

	@GetMapping("/{id}")
	@Operation(
			summary = "Получение организационной формы по ID",
			description = "Возвращает организационную форму по её идентификатору",
			parameters = {
					@Parameter(
							name = "id",
							description = "Идентификатор организационной формы",
							example = "2",
							required = true
					)
			},
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Организационная форма найдена",
							content = @Content(
									mediaType = MediaType.APPLICATION_JSON_VALUE,
									schema = @Schema(implementation = OrgFormDto.class),
									examples = {
											@ExampleObject(
													name = "Пример ответа",
													value = """
                                {
                                    "id": 2,
                                    "name": "ООО",
                                    "isActive": true
                                }
                                """
											)
									}
							)
					),
					@ApiResponse(responseCode = "404", description = "Организационная форма не найдена")
			}
	)
	public ResponseEntity<OrgFormDto> getById(@PathVariable Integer id) {
		return orgFormService.getOrgFormById(id)
				.map(dto -> ResponseEntity.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(dto))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PutMapping("/save")
	@Operation(
			summary = "Создание или обновление организационной формы",
			description = "Создает новую или обновляет существующую организационную форму",
			requestBody = @RequestBody(
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = OrgFormDto.class),
							examples = {
									@ExampleObject(
											name = "Пример запроса",
											value = """
                            {
                                "id": 2,
                                "name": "ООО",
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
							description = "Организационная форма успешно сохранена",
							content = @Content(
									mediaType = MediaType.APPLICATION_JSON_VALUE,
									schema = @Schema(implementation = OrgFormDto.class),
									examples = {
											@ExampleObject(
													name = "Пример ответа",
													value = """
                                {
                                    "id": 2,
                                    "name": "ООО",
                                    "isActive": true
                                }
                                """
											)
									}
							)
					)
			}
	)
	public ResponseEntity<OrgFormDto> saveOrgForm(@RequestBody OrgFormDto dto) {
		OrgFormDto saved = orgFormService.saveOrgForm(dto);
		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(saved);
	}

	@DeleteMapping("/delete/{id}")
	@Operation(
			summary = "Деактивация организационной формы",
			description = "Логическое удаление (деактивация) организационной формы по идентификатору",
			parameters = {
					@Parameter(
							name = "id",
							description = "Идентификатор организационной формы",
							example = "2",
							required = true
					)
			},
			responses = {
					@ApiResponse(responseCode = "200", description = "Организационная форма деактивирована")
			}
	)
	public ResponseEntity<Void> deactivate(@PathVariable Integer id) {
		orgFormService.deactivateOrgForm(id);
		return ResponseEntity.ok().build();
	}
}
