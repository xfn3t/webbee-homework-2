package ru.homework.deal.domain.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SumFilter {
	private BigDecimal value;
	private String currency;
}