package ru.homework.deal.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.homework.deal.domain.model.DealSum;

@Repository
public interface DealSumRepository extends JpaRepository<DealSum, Long> {}