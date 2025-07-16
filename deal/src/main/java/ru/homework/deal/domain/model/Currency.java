package ru.homework.deal.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;


@Entity
@Table(name = "currency")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Currency {
	@Id
	@Column(name = "id", length = 3)
	private String id;

	@Column(nullable = false)
	private String name;

	@Column(name = "is_active", nullable = false)
	private boolean isActive = true;

	public Currency(String id, String name) {
		this.id = id;
		this.name = name;
	}
}