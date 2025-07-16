package ru.homework;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.suite.api.*;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.junit.jupiter.TestcontainersExtension;
import ru.homework.core.config.BaseTestcontainers;
import ru.homework.domain.contractor.controller.ContractorControllerTest;
import ru.homework.domain.contractor.repository.ContractorRepositoryTest;
import ru.homework.domain.contractor.service.ContractorServiceTest;
import ru.homework.domain.reference.country.repository.CountryRepositoryTest;

@Suite
@SelectClasses({
		ContractorRepositoryTest.class,
		ContractorControllerTest.class,
		ContractorServiceTest.class,
		CountryRepositoryTest.class
})
@ActiveProfiles("test")
@Testcontainers
@ExtendWith(TestcontainersExtension.class)
public class ContractorApplicationTests extends BaseTestcontainers {
}