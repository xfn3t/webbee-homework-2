package ru.homework.deal.domain.dto;

import lombok.*;

import java.time.*;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DealDto {
	private UUID id;
	private String description;
	private String agreementNumber;
	private LocalDate agreementDate;
	private LocalDateTime agreementStartDt;
	private LocalDate availabilityDate;
	private DealTypeDto type;
	private DealStatusDto status;
	private SumDto sum;
	private LocalDateTime closeDt;
	private List<DealContractorDto> contractors;
}