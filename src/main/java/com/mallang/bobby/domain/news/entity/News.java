package com.mallang.bobby.domain.news.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mallang.bobby.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "news")
@NoArgsConstructor
@AllArgsConstructor
public class News extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "query")
	private String query;

	@Column(name = "title")
	private String title;

	@Column(name = "original_link")
	private String originalLink;

	@Column(name = "naver_link")
	private String naverLink;

	@Column(name = "description")
	private String description;
}
