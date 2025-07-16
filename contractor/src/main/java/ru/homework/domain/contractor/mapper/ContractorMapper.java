package ru.homework.domain.contractor.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.homework.domain.contractor.dto.ContractorDto;
import ru.homework.domain.contractor.dto.ContractorFullDto;
import ru.homework.domain.contractor.model.Contractor;

@Mapper(componentModel = "spring")
public interface ContractorMapper {

	@Mapping(target = "createDate", ignore = true)
	@Mapping(target = "modifyDate", ignore = true)
	@Mapping(target = "createUserId", ignore = true)
	@Mapping(target = "modifyUserId", ignore = true)
	@Mapping(source = "isActive", target = "active")
	Contractor toEntity(ContractorDto dto);

	@Mapping(source = "active", target = "isActive", qualifiedByName = "booleanToBooleanWrapper")
	ContractorFullDto toFullDto(Contractor entity);

	@Named("booleanToBooleanWrapper")
	default Boolean booleanToBooleanWrapper(boolean value) {
		return value;
	}
}