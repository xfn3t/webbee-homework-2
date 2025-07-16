package ru.homework.deal.domain.service;

import org.springframework.data.domain.Page;
import ru.homework.deal.core.export.exel.ExcelExporter;
import ru.homework.deal.domain.model.Deal;

public interface DealExcelExporter extends ExcelExporter<Page<Deal>> {
}
