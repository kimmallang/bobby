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
@Table(name = "free_board_comment")
@NoArgsConstructor
@AllArgsConstructor
public class FreeBoardComment extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "free_board_id")
	private Long freeBoardId;

	@Column(name = "contents")
	private String contents;

	@Column(name = "writer_id")
	private Long writerId;

	@Column(name = "writer_nickname")
	private String writerNickname;

	@Column(name = "like_count")
	private Integer likeCount;

	@Column(name = "comment_reply_count")
	private Integer commentReplyCount;

	@Column(name = "delete_yn")
	private Boolean isDeleted;

	public LocalDateTime getCreatedAt() {
		return super.createdAt;
	}

	public LocalDateTime getModifiedAt() {
		return super.modifiedAt;
	}
}
