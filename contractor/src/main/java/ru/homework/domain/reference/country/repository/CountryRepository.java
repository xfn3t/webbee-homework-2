package ru.homework.domain.reference.country.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.homework.domain.reference.country.model.Country;

import java.util.List;

@Repository
public interface CountryRepository extends CrudRepository<Country, String> {
	List<Country> findByIsActiveTrue();

	@Modifying
	@Query("UPDATE country " +
			"SET name = :name, is_active = :active " +
			"WHERE id = :id")
	@Transactional
	int updateNameAndActiveById(String id, String name, boolean active);

	@Modifying
	@Transactional
	@Query("UPDATE country SET is_active = :active WHERE id = :id")
	void updateActiveStatus(String id, boolean active);

	@Transactional
	@Query("""
       INSERT INTO country (id, name, is_active)
       VALUES ( :#{#country.id}, :#{#country.name}, :#{#country.active} )
       RETURNING id, name, is_active
       """)
	Country insertCountry(Country country);


}