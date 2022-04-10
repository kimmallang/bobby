package com.mallang.bobby.domain.freeboard.dto;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mallang.bobby.dto.PagingDto;
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
}
