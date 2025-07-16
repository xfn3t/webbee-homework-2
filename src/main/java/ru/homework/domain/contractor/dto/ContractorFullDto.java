package ru.homework.domain.contractor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ContractorFullDto {
	private String id;
	private String parentId;
	private String name;
	private String nameFull;
	private String inn;
	private String ogrn;
	private String country;
	private Integer industry;
	private Integer orgForm;

	@JsonProperty("createDate")
	private LocalDateTime createDate;

	@JsonProperty("modifyDate")
	private LocalDateTime modifyDate;

	private String createUserId;
	private String modifyUserId;

	@JsonProperty("isActive")
	private Boolean isActive;
}