package ru.homework.domain.reference.orgform.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.homework.domain.reference.orgform.model.OrgForm;

import java.util.List;

@Repository
public interface OrgFormRepository extends CrudRepository<OrgForm, Integer> {

	List<OrgForm> findByIsActiveTrue();

	@Modifying
	@Query("UPDATE org_form SET is_active = :active WHERE id = :id")
	void updateActiveStatus(Integer id, boolean active);
}