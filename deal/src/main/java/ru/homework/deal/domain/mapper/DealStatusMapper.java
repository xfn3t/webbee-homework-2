package ru.homework.deal.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.homework.deal.domain.model.DealStatus;
import ru.homework.deal.domain.dto.DealStatusDto;

@Mapper(componentModel = "spring")
public interface DealStatusMapper {
	DealStatusDto toDto(DealStatus entity);

	default DealStatus toEntity(DealStatusDto dto) {
		if (dto == null) return null;
		DealStatus status = new DealStatus();
		status.setId(dto.getId());
		status.setName(dto.getName());
		return status;
	}
}