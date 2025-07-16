package ru.homework.deal.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import ru.homework.deal.domain.model.DealStatus;
import ru.homework.deal.domain.model.DealType;
import ru.homework.deal.domain.dto.EnumValueDto;

@Mapper(componentModel = "spring")
public interface EnumValueMapper {

	@Named("statusToDto")
	default EnumValueDto statusToDto(DealStatus s) {
		if (s == null) return null;
		return new EnumValueDto(s.getId(), s.getName());
	}

	@Named("typeToDto")
	default EnumValueDto typeToDto(DealType t) {
		if (t == null) return null;
		return new EnumValueDto(t.getId(), t.getName());
	}
}