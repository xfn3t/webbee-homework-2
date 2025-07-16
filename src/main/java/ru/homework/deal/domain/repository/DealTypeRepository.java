package ru.homework.deal.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.homework.deal.domain.model.DealType;

@Repository
public interface DealTypeRepository extends JpaRepository<DealType, String> { }