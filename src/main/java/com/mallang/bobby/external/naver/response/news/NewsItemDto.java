package com.mallang.bobby.external.naver.response.news;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewsItemDto {
	private String title;
	private String originallink;
	private String link;
	private String description;
	private String pubDate;
}
