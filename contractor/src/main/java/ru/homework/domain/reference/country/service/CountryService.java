package ru.homework.domain.reference.country.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.homework.domain.reference.country.dto.CountryDto;
import ru.homework.domain.reference.country.mapper.CountryMapper;
import ru.homework.domain.reference.country.model.Country;
import ru.homework.domain.reference.country.repository.CountryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryService {

	private final CountryRepository countryRepository;
	private final CountryMapper countryMapper;

	public List<CountryDto> getAllActiveCountries() {
		return countryRepository.findByIsActiveTrue().stream()
				.map(countryMapper::toDto)
				.collect(Collectors.toList());
	}

	public Optional<CountryDto> getCountryById(String id) {
		return countryRepository.findById(id)
				.map(countryMapper::toDto);
	}

	public CountryDto saveCountry(CountryDto dto) {
		Country country = countryMapper.toEntity(dto);
		country.setActive(true);
		Country saved = countryRepository.save(country);
		return countryMapper.toDto(saved);
	}

	public void deactivateCountry(String id) {
		countryRepository.updateActiveStatus(id, false);
	}
}