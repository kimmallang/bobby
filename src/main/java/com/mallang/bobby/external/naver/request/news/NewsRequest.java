package com.mallang.bobby.external.naver.request.news;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewsRequest {
	private String query;
	private Integer display;
	private Integer start;
	private NewsRequestSort sort;
}
