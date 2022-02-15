package com.mallang.bobby.domain.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mallang.bobby.domain.news.entity.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
	void deleteAllByQuery(String query);
}
