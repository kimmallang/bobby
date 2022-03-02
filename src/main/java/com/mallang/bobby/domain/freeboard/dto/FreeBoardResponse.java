package com.mallang.bobby.domain.freeboard.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FreeBoardResponse {
	private Integer page;
	private Boolean isLast;
	private List<FreeBoardDto> items;
}
