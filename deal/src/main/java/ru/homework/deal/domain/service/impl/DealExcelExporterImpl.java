package ru.homework.deal.domain.service.impl;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.homework.deal.domain.model.Deal;
import ru.homework.deal.domain.model.DealSum;
import ru.homework.deal.domain.service.DealExcelExporter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

@Service
public class DealExcelExporterImpl implements DealExcelExporter {

	@Override
	public byte[] export(Page<Deal> page) {
		try (Workbook wb = new XSSFWorkbook();
			 ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = wb.createSheet("Deals");
			createHeaderRow(sheet);
			int rowIdx = 1;
			for (Deal deal : page.getContent()) {
				writeDealRow(sheet.createRow(rowIdx++), deal);
			}

			wb.write(out);
			return out.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException("Excel export failed", e);
		}
	}

	private void createHeaderRow(Sheet sheet) {
		Row header = sheet.createRow(0);
		String[] cols = {"ID", "Description", "Agreement №", "Agreement Date", "Type", "Status", "Sum", "Currency"};
		for (int i = 0; i < cols.length; i++) {
			header.createCell(i).setCellValue(cols[i]);
		}
	}

	private void writeDealRow(Row row, Deal deal) {
		row.createCell(0).setCellValue(deal.getId().toString());
		row.createCell(1).setCellValue(Optional.ofNullable(deal.getDescription()).orElse(""));
		row.createCell(2).setCellValue(Optional.ofNullable(deal.getAgreementNumber()).orElse(""));
		row.createCell(3).setCellValue(
				deal.getAgreementDate() != null
						? deal.getAgreementDate().toString()
						: ""
		);
		row.createCell(4).setCellValue(
				deal.getType() != null
						? deal.getType().getName()
						: ""
		);
		row.createCell(5).setCellValue(
				deal.getStatus() != null
						? deal.getStatus().getName()
						: ""
		);
		deal.getSums().stream()
				.filter(DealSum::isMain)
				.findFirst()
				.ifPresentOrElse(sum -> {
					row.createCell(6).setCellValue(sum.getSum().toString());
					row.createCell(7).setCellValue(sum.getCurrency().getId());
				}, () -> {
					row.createCell(6).setCellValue("");
					row.createCell(7).setCellValue("");
				});
	}
}
