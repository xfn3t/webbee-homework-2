package ru.homework.domain.reference.country.controller;

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
import ru.homework.domain.reference.country.dto.CountryDto;
import ru.homework.domain.reference.country.service.CountryService;

import java.util.List;

@RestController
@RequestMapping("/country")
@RequiredArgsConstructor
@Tag(name = "Управление странами", description = "API для работы со странами")
public class CountryController {

	private final CountryService countryService;

	@GetMapping("/all")
	@Operation(
			summary = "Получение всех активных стран",
			description = "Возвращает список всех активных стран",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Список стран получен",
							content = @Content(
									mediaType = MediaType.APPLICATION_JSON_VALUE,
									schema = @Schema(implementation = CountryDto.class)
							)
					)
			}
	)
	public ResponseEntity<List<CountryDto>> getAllActive() {
		List<CountryDto> list = countryService.getAllActiveCountries();
		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(list);
	}

	@GetMapping("/{id}")
	@Operation(
			summary = "Получение страны по ID",
			description = "Возвращает страну по её идентификатору",
			parameters = {
					@Parameter(
							name = "id",
							description = "Идентификатор страны",
							example = "RU",
							required = true
					)
			},
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Страна найдена",
							content = @Content(
									mediaType = MediaType.APPLICATION_JSON_VALUE,
									schema = @Schema(implementation = CountryDto.class),
									examples = {
											@ExampleObject(
													name = "Пример ответа",
													value = """
                                {
                                    "id": "RU",
                                    "name": "Россия"
                                }
                                """
											)
									}
							)
					),
					@ApiResponse(responseCode = "404", description = "Страна не найдена")
			}
	)
	public ResponseEntity<CountryDto> getById(@PathVariable String id) {
		return countryService.getCountryById(id)
				.map(dto -> ResponseEntity.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(dto))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PutMapping("/save")
	@Operation(
			summary = "Создание или обновление страны",
			description = "Создает новую или обновляет существующую страну",
			requestBody = @RequestBody(
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = CountryDto.class),
							examples = {
									@ExampleObject(
											name = "Пример запроса",
											value = """
                            {
                                "id": "US",
                                "name": "United States"
                            }
                            """
									)
							}
					)
			),
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Страна успешно сохранена",
							content = @Content(
									mediaType = MediaType.APPLICATION_JSON_VALUE,
									schema = @Schema(implementation = CountryDto.class),
									examples = {
											@ExampleObject(
													name = "Пример ответа",
													value = """
                                {
                                    "id": "US",
                                    "name": "United States"
                                }
                                """
											)
									}
							)
					)
			}
	)
	public ResponseEntity<CountryDto> saveCountry(@RequestBody CountryDto dto) {
		CountryDto saved = countryService.saveCountry(dto);
		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(saved);
	}

	@DeleteMapping("/delete/{id}")
	@Operation(
			summary = "Деактивация страны",
			description = "Логическое удаление (деактивация) страны по идентификатору",
			parameters = {
					@Parameter(
							name = "id",
							description = "Идентификатор страны",
							example = "US",
							required = true
					)
			},
			responses = {
					@ApiResponse(responseCode = "200", description = "Страна деактивирована")
			}
	)
	public ResponseEntity<Void> deactivateCountry(@PathVariable String id) {
		countryService.deactivateCountry(id);
		return ResponseEntity.ok().build();
	}

}
