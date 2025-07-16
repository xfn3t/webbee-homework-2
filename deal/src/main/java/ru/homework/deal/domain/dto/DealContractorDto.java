package ru.homework.deal.domain.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DealContractorDto {
	private UUID id;
	private UUID dealId;
	private String contractorId;
	private String name;
	private String inn;
	private boolean main;
	private List<ContractorRoleDto> roles;
}