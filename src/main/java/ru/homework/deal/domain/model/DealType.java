package ru.homework.deal.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "deal_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DealType {
	@Id
	@Column(name = "id", length = 30)
	private String id;

	@Column(nullable = false)
	private String name;

	@Column(name = "is_active", nullable = false)
	private boolean isActive = true;
}