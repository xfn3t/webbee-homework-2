package ru.homework.deal.core.export.exel;

public interface ExcelExporter<T> {
	/**
	 * Генерирует Excel-файл из объекта произвольного типа.
	 *
	 * @param data исходные данные
	 * @return байтовый массив с содержимым .xlsx
	 */
	byte[] export(T data);
}
