package com.mallang.bobby.domain.freeboard.entity;

import java.time.LocalDateTime;

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
@Table(name =  "free_board")
@NoArgsConstructor
@AllArgsConstructor
public class FreeBoard extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "title")
	private String title;

	@Column(name = "contents")
	private String contents;

	@Column(name = "writer_id")
	private String writerId;

	@Column(name = "like_count")
	private Integer likeCount;

	@Column(name = "comments_count")
	private Integer commentsCount;

	public LocalDateTime getCreatedAt() {
		return super.createdAt;
	}
}
