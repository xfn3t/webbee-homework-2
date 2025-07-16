package ru.homework.deal.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="deal_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DealStatus {
	@Id
	@Column(name = "id", length = 30)
	private String id;

	@Column(nullable = false)
	private String name;

	@Column(name = "is_active", nullable = false)
	private boolean isActive = true;
}