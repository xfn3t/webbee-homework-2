package ru.homework.deal.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.homework.deal.domain.model.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, String> {}