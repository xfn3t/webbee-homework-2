package ru.homework.deal.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.homework.deal.domain.model.ContractorRole;

@Repository
public interface ContractorRoleRepository extends JpaRepository<ContractorRole, String> { }
