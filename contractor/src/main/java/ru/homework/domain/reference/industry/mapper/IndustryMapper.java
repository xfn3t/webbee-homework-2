package ru.homework.domain.reference.industry.mapper;

import org.mapstruct.Mapper;
import ru.homework.domain.reference.industry.dto.IndustryDto;
import ru.homework.domain.reference.industry.model.Industry;

@Mapper(componentModel = "spring")
public interface IndustryMapper {
	IndustryDto toDto(Industry entity);
	Industry toEntity(IndustryDto dto);
}