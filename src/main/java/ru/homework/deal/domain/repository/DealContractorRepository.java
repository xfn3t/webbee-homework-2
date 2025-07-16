package ru.homework.deal.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.homework.deal.domain.model.DealContractor;

import java.util.UUID;
import java.util.List;

@Repository
public interface DealContractorRepository extends JpaRepository<DealContractor, UUID> {
	List<DealContractor> findByDealIdAndIsActiveTrue(UUID dealId);
}
