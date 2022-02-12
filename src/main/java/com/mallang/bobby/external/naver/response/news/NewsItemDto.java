package com.mallang.bobby.external.naver.response.news;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewsItemDto {
	private String title;
	private String originallink;
	private String link;
	private String description;
	private String pubDate;
}
