package ru.homework.domain.contractor.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Contractor {
	private String id;
	private String parentId;
	private String name;
	private String nameFull;
	private String inn;
	private String ogrn;
	private String country;
	private Integer industry;
	private Integer orgForm;
	private LocalDateTime createDate;
	private LocalDateTime modifyDate;
	private String createUserId;
	private String modifyUserId;
	private boolean isActive;
}