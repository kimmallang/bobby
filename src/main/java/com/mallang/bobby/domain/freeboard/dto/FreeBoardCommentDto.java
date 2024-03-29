package com.mallang.bobby.domain.freeboard.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mallang.bobby.dto.PagingCursorDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FreeBoardCommentDto {
	private Long id;
	private Long freeBoardId;
	private String contents;
	private Long writerId;
	private String writerNickname;
	private Integer likeCount;
	private Integer commentReplyCount;
	private Boolean isDeleted;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
	private LocalDateTime createdAt;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
	private LocalDateTime modifiedAt;

	private PagingCursorDto<FreeBoardReplyDto> commentReplyPage;

	private Boolean isMine;
	private Boolean isLike;
}
