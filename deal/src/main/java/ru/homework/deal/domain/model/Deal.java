package ru.homework.deal.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;
import java.util.*;

@Entity
@Table(name="deal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Deal {
	@Id
	@GeneratedValue
	private UUID id;

	private String description;

	@Column(name = "agreement_number")
	private String agreementNumber;

	@Column(name = "agreement_date")
	private LocalDate agreementDate;

	@Column(name = "agreement_start_dt")
	private LocalDateTime agreementStartDt;

	@Column(name = "availability_date")
	private LocalDate availabilityDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type_id")
	private DealType type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = false)
	private DealStatus status;

	@Column(name = "close_dt")
	private LocalDateTime closeDt;

	@OneToMany(mappedBy = "deal", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DealSum> sums = new ArrayList<>();

	@OneToMany(mappedBy = "deal", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DealContractor> contractors = new ArrayList<>();

	@Column(name = "create_date", nullable = false, updatable = false)
	private LocalDateTime createDate = LocalDateTime.now();

	@Column(name = "modify_date")
	private LocalDateTime modifyDate;

	@Column(name = "is_active", nullable = false)
	private boolean isActive = true;
}