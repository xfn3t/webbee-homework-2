package ru.homework.deal.domain.mapper;

import lombok.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.homework.deal.domain.dto.DealContractorDto;
import ru.homework.deal.domain.model.DealContractor;

import java.util.List;

@Mapper(
		componentModel = "spring",
		uses = {ContractorRoleMapper.class}
)
public interface DealContractorMapper {

	@Mapping(target = "dealId", source = "deal.id")
	DealContractorDto toDto(DealContractor entity);

	List<DealContractorDto> toDtoList(List<DealContractor> entities);

	@Mapping(target = "deal", ignore = true)
	@Mapping(target = "roles", ignore = true)
	@Mapping(target = "isActive", ignore = true)
	DealContractor toEntity(DealContractorDto dto);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "deal", ignore = true)
	@Mapping(target = "roles", ignore = true)
	@Mapping(target = "createDate", ignore = true)
	@Mapping(target = "createUserId", ignore = true)
	@Mapping(target = "active", ignore = true)
	void updateFromDto(DealContractorDto dto, @MappingTarget DealContractor entity);
}