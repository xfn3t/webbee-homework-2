package ru.homework.deal.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.homework.deal.domain.model.Currency;
import ru.homework.deal.domain.dto.CurrencyDto;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {
	CurrencyDto toDto(Currency entity);

	default Currency toEntity(CurrencyDto dto) {
		if (dto == null) return null;
		Currency currency = new Currency();
		currency.setId(dto.getId());
		currency.setName(dto.getName());
		return currency;
	}
}