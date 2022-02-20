package com.mallang.bobby.domain.news.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NewsRequestQuery {
	IT("IT"), DUNAMU("두나무"), NAVER("네이버"), KAKAO("카카오"), LINE("라인"), COUPANG("쿠팡"), BAEMIN("우한형제들"), DANGKGEUN("당근마켓"), TOSS("토스");

	private String query;
}
