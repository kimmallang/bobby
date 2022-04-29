package com.mallang.bobby.domain.freeboard.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "free_board_reply_like")
@NoArgsConstructor
@AllArgsConstructor
public class FreeBoardReplyLike {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "free_board_reply_id")
	private Long freeBoardReplyId;

	@Column(name = "user_id")
	private Long userId;
}
