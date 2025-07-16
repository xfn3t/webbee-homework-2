package ru.homework.deal.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;


@Entity
@Table(name="contractor_role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractorRole {
	@Id
	@Column(length = 30)
	private String id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, length = 30)
	private String category;

	@Column(name = "is_active", nullable = false)
	private boolean isActive = true;
}