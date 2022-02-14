package com.mallang.bobby.external.naver.request.news;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsRequest {
	private String query;
	private Integer display;
	private Integer start;
	private NewsRequestSort sort;
}
