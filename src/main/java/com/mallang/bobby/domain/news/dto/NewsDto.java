package com.mallang.bobby.domain.news.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsDto {
	private Long id;
	private NewsRequestQuery query;
	private String title;
	private String originalLink;
	private String naverLink;
	private String description;
}
