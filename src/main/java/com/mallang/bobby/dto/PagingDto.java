package com.mallang.bobby.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PagingDto<T> {
	private Integer page;
	private Boolean isLast;
	private List<T> items;
}
