package ru.homework.deal.domain.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SumDto {
	private BigDecimal value;
	private CurrencyDto currency;
}