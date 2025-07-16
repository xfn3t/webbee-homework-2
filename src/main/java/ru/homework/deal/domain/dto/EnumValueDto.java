package ru.homework.deal.domain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnumValueDto {
	private String id;
	private String name;
}