package com.mallang.bobby.domain.news.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NewsRequestQuery {
	IT("IT"), DEVELOPER("개발자 채용"), SW("SW");

	private String query;
}
