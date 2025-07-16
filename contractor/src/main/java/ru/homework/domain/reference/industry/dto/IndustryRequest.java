package ru.homework.domain.reference.industry.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndustryRequest {
	private String name;
	private Boolean isActive;
}