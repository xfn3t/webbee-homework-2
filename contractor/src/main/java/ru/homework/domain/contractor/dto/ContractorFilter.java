package ru.homework.domain.contractor.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContractorFilter {
	private String contractorId;
	private String parentId;
	private String searchTerm;
	private String country;
	private Integer industry;
	private String orgForm;
}