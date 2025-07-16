package ru.homework.domain.reference.orgform.mapper;

import org.mapstruct.Mapper;
import ru.homework.domain.reference.orgform.dto.OrgFormDto;
import ru.homework.domain.reference.orgform.model.OrgForm;

@Mapper(componentModel = "spring")
public interface OrgFormMapper {
	OrgFormDto toDto(OrgForm entity);
	OrgForm toEntity(OrgFormDto dto);
}