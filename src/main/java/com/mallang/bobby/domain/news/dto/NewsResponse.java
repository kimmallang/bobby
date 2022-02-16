package com.mallang.bobby.domain.news.dto;

import java.util.List;

import com.mallang.bobby.domain.news.vo.NewsVo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NewsResponse {
	private Integer page;
	private Boolean isLast;
	private List<NewsVo> items;
}
