package ru.homework.deal.domain.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DealSearchRequest {
	private String dealId;
	private String description;
	private String agreementNumber;
	private LocalDate agreementDateFrom;
	private LocalDate agreementDateTo;
	private LocalDate availabilityDateFrom;
	private LocalDate availabilityDateTo;
	private List<String> types;
	private List<String> statuses;
	private LocalDateTime closeDtFrom;
	private LocalDateTime closeDtTo;
	private String borrowerSearch;
	private String warrantySearch;
	private SumFilter sum;
}
