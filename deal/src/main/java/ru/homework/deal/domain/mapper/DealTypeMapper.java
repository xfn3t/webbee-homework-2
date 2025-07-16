package ru.homework.deal.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.homework.deal.domain.model.DealType;
import ru.homework.deal.domain.dto.DealTypeDto;

@Mapper(componentModel = "spring")
public interface DealTypeMapper {
	DealTypeDto toDto(DealType entity);

	default DealType toEntity(DealTypeDto dto) {
		if (dto == null) return null;
		DealType type = new DealType();
		type.setId(dto.getId());
		type.setName(dto.getName());
		return type;
	}
}