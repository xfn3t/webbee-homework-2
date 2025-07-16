package ru.homework.domain.reference.country.mapper;

import org.mapstruct.Mapper;
import ru.homework.domain.reference.country.dto.CountryDto;
import ru.homework.domain.reference.country.model.Country;

@Mapper(componentModel = "spring")
public interface CountryMapper {
	CountryDto toDto(Country entity);
	Country toEntity(CountryDto dto);
}