package ru.homework.domain.reference.industry.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.homework.domain.reference.industry.model.Industry;

import java.util.List;

@Repository
public interface IndustryRepository extends CrudRepository<Industry, Integer> {
	List<Industry> findByIsActiveTrue();

	@Modifying
	@Query("UPDATE industry SET is_active = false WHERE id = :id")
	void deactivateById(Integer id);

	@Modifying
	@Query("UPDATE industry SET is_active = :active WHERE id = :id")
	void updateActiveStatus(Integer id, boolean active);

}