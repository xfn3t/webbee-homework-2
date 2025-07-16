package ru.homework.domain.contractor.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractorDto {
	private String id;
	private String parentId;
	private String name;
	private String nameFull;
	private String inn;
	private String ogrn;
	private String country;
	private Integer industry;
	private Integer orgForm;
	private Boolean isActive;
}