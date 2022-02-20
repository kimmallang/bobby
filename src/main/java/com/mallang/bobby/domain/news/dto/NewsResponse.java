package com.mallang.bobby.domain.news.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NewsResponse {
	private Integer page;
	private Boolean isLast;
	private List<NewsDto> items;
}
