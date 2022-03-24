package com.mallang.bobby.domain.news;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mallang.bobby.domain.news.service.NewsService;
import com.mallang.bobby.domain.news.dto.NewsRequestQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NewsScheduler {
	private final NewsService newsService;

	@Scheduled(cron = "0 0 */1 * * *")
	public void refreshFromApi() {
		try {
			log.info("[뉴스] 뉴스 조회 및 저장 시작");
			newsService.refreshFromApi(NewsRequestQuery.IT);
			newsService.refreshFromApi(NewsRequestQuery.DUNAMU);
			newsService.refreshFromApi(NewsRequestQuery.NAVER);
			newsService.refreshFromApi(NewsRequestQuery.KAKAO);
			newsService.refreshFromApi(NewsRequestQuery.LINE);
			newsService.refreshFromApi(NewsRequestQuery.COUPANG);
			newsService.refreshFromApi(NewsRequestQuery.BAEMIN);
			newsService.refreshFromApi(NewsRequestQuery.DANGKGEUN);
			newsService.refreshFromApi(NewsRequestQuery.TOSS);
			log.info("[뉴스] 뉴스 조회 및 저장 완료");
		} catch (Exception e) {
			log.error("[뉴스] 뉴스 조회 및 저장 실패", e);
		}
	}
}
