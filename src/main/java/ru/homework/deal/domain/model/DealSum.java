package ru.homework.deal.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "deal_sum")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DealSum {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "deal_id", nullable = false)
	private Deal deal;

	@Column(name = "sum", nullable = false, precision = 100, scale = 2)
	private BigDecimal sum;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "currency_id", nullable = false)
	private Currency currency;

	@Column(name = "is_main", nullable = false)
	private boolean isMain = false;

	@Column(name = "is_active", nullable = false)
	private boolean isActive = true;
}