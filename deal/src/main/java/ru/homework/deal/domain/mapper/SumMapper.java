package ru.homework.deal.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.homework.deal.domain.dto.SumDto;
import ru.homework.deal.domain.model.DealSum;

import java.util.List;

@Mapper(componentModel = "spring", uses = CurrencyMapper.class)
public interface SumMapper {

	@Mapping(target = "value", source = "sum")
	@Mapping(target = "currency", source = "currency")
	SumDto toDto(DealSum entity);

	default SumDto toDto(List<DealSum> sums) {
		return sums.stream()
				.filter(DealSum::isMain)
				.findFirst()
				.map(this::toDto)
				.orElse(null);
	}

	@Mapping(target = "sum", source = "value")
	@Mapping(target = "currency", source = "currency")
	DealSum toEntity(SumDto dto);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "deal", ignore = true)
	@Mapping(target = "main", ignore = true)
	@Mapping(target = "active", ignore = true)
	void updateFromDto(SumDto dto, @MappingTarget DealSum entity);
}