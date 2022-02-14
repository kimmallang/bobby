package com.mallang.bobby.external.naver.response.news;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewsResponse {
	private String lastBuildDate;
	private Integer total;
	private Integer start;
	private Integer display;
	private List<NewsItemDto> items;
}
