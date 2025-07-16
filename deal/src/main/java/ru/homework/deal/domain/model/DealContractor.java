package ru.homework.deal.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name="deal_contractor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DealContractor {

	@Id
	@GeneratedValue(generator="uuid2")
	private UUID id;

	@ManyToOne(optional=false)
	@JoinColumn(name="deal_id")
	private Deal deal;

	@Column(name="contractor_id", nullable=false)
	private String contractorId;

	private String name;
	private String inn;
	@Column(name="main", nullable=false)
	private boolean main = false;

	@Column(name="create_date", nullable=false, updatable=false)
	private LocalDateTime createDate = LocalDateTime.now();
	@Column(name="modify_date")
	private LocalDateTime modifyDate;
	@Column(name="create_user_id")
	private String createUserId;
	@Column(name="modify_user_id")
	private String modifyUserId;
	@Column(name="is_active", nullable=false)
	private boolean isActive = true;

	@ManyToMany
	@JoinTable(
			name="contractor_to_role",
			joinColumns=@JoinColumn(name="contractor_id"),
			inverseJoinColumns=@JoinColumn(name="role_id")
	)
	private Set<ContractorRole> roles = new HashSet<>();
}
