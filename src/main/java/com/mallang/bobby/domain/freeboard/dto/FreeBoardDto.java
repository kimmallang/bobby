package com.mallang.bobby.domain.freeboard.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FreeBoardDto {
	private Long id;
	private String title;
	private String contents;
	private Long writerId;
	private String writerNickname;
	private Integer likeCount;
	private Integer commentsCount;
	private Boolean deleteYn;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
	private LocalDateTime createdAt;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
	private LocalDateTime modifiedAt;

	private Boolean isMine;
	private Boolean isLike;
}
