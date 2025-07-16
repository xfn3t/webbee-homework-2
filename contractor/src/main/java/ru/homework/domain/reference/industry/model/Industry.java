package ru.homework.domain.reference.industry.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table("industry")
public class Industry {
	@Id
	private Integer id;
	private String name;
	private boolean isActive;
}