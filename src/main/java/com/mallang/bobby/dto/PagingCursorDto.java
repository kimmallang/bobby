package com.mallang.bobby.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PagingCursorDto<T> {
	private Long cursor;
	private Boolean isLast;
	private List<T> items;
}
