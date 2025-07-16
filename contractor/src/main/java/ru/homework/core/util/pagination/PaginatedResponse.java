package ru.homework.core.util.pagination;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class PaginatedResponse<T> {
	private final List<T> content;
	private final int page;
	private final int size;
	private final int total;
}