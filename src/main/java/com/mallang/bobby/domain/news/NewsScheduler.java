package com.mallang.bobby.domain.news;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mallang.bobby.domain.news.service.NewsService;
import com.mallang.bobby.domain.news.vo.NewsRequestQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NewsScheduler {
	private final NewsService newsService;

	@Scheduled(cron = "0 0 */1 * * *")
	public void test() {
		try {
			log.info("[뉴스] 뉴스 조회 및 저장 시작");
			newsService.saveFromApi(NewsRequestQuery.IT);
			newsService.saveFromApi(NewsRequestQuery.DEVELOPER);
			newsService.saveFromApi(NewsRequestQuery.SW);
			log.info("[뉴스] 뉴스 조회 및 저장 완료");
		} catch (Exception e) {
			log.error("[뉴스] 뉴스 조회 및 저장 실패", e);
		}
	}
}
