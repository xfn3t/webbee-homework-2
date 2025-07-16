package ru.homework.deal.domain.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.homework.deal.domain.dto.ContractorRoleDto;
import ru.homework.deal.domain.model.ContractorRole;

@Mapper(componentModel = "spring")
public interface ContractorRoleMapper {
	ContractorRoleDto toDto(ContractorRole entity);

	default ContractorRole toEntity(ContractorRoleDto dto) {
		if (dto == null) return null;
		ContractorRole role = new ContractorRole();
		role.setId(dto.getId());
		role.setName(dto.getName());
		role.setCategory(dto.getCategory());
		return role;
	}
}