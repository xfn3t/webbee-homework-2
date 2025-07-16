package ru.homework.deal.domain.mapper;

import org.mapstruct.*;
import ru.homework.deal.domain.model.Currency;
import ru.homework.deal.domain.dto.DealDto;
import ru.homework.deal.domain.dto.SumDto;
import ru.homework.deal.domain.model.Deal;
import ru.homework.deal.domain.model.DealSum;

import java.util.Collections;
import java.util.List;

@Mapper(
		componentModel = "spring",
		uses = {DealTypeMapper.class, DealStatusMapper.class, SumMapper.class, DealContractorMapper.class}
)
public interface DealMapper {

	@Mapping(target = "type", source = "type")
	@Mapping(target = "status", source = "status")
	@Mapping(target = "sum", source = "sums")
	@Mapping(target = "contractors", source = "contractors")
	DealDto toDto(Deal entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createDate", ignore = true)
	@Mapping(target = "modifyDate", ignore = true)
	@Mapping(target = "active", ignore = true)
	@Mapping(target = "sums", source = "dto.sum")
	@Mapping(target = "contractors", source = "dto.contractors")
	void updateDealFromDto(@MappingTarget Deal entity, DealDto dto);

	default List<DealSum> mapSum(SumDto dto) {
		if (dto == null) return Collections.emptyList();

		DealSum sum = new DealSum();
		sum.setSum(dto.getValue());

		Currency currency = new Currency();
		currency.setId(dto.getCurrency().getId());
		currency.setName(dto.getCurrency().getName());
		sum.setCurrency(currency);

		sum.setMain(true);
		return Collections.singletonList(sum);
	}
}