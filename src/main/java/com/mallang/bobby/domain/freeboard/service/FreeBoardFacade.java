package com.mallang.bobby.domain.freeboard.service;

import org.springframework.stereotype.Service;

import com.mallang.bobby.domain.auth.user.dto.UserDto;
import com.mallang.bobby.domain.freeboard.dto.FreeBoardCommentDto;
import com.mallang.bobby.domain.freeboard.dto.FreeBoardReplyDto;
import com.mallang.bobby.domain.freeboard.dto.FreeBoardDto;
import com.mallang.bobby.dto.PagingCursorDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FreeBoardFacade {
	private final FreeBoardService freeBoardService;
	private final FreeBoardCommentService freeBoardCommentService;
	private final FreeBoardReplyService freeBoardReplyService;

	public PagingCursorDto<FreeBoardDto> getBoards(long cursor, int size, UserDto userDto) {
		return freeBoardService.get(cursor, size, userDto);
	}

	public FreeBoardDto getBoard(long id, UserDto userDto) {
		return freeBoardService.get(id, userDto);
	}

	public void saveBoard(FreeBoardDto freeBoardDto, UserDto userDto) {
		freeBoardService.save(freeBoardDto, userDto);
	}

	public void removeBoard(long id, UserDto userDto) {
		freeBoardService.remove(id, userDto);
	}

	public void likeBoard(long freeBoardId, UserDto userDto) {
		freeBoardService.like(freeBoardId, userDto);
	}

	public void unLikeBoard(long freeBoardId, UserDto userDto) {
		freeBoardService.unLike(freeBoardId, userDto);
	}

	public PagingCursorDto<FreeBoardCommentDto> getComments(long freeBoardId, long cursor, int size, UserDto userDto) {
		return freeBoardCommentService.get(freeBoardId, cursor, size, userDto);
	}

	public FreeBoardCommentDto getComment(long freeBoardId, long id, UserDto userDto) {
		return freeBoardCommentService.get(freeBoardId, id, userDto);
	}

	public Long saveComment(FreeBoardCommentDto freeBoardCommentDto, UserDto userDto) {
		final Long commentId = freeBoardCommentService.save(freeBoardCommentDto, userDto);
		final Integer commentCount = freeBoardCommentService.countCommentCountByFreeBoardId(freeBoardCommentDto.getFreeBoardId());
		freeBoardService.updateCommentCount(freeBoardCommentDto.getFreeBoardId(), commentCount);
		return commentId;
	}

	public void removeComment(long id, UserDto userDto) {
		freeBoardCommentService.remove(id, userDto);
	}

	public PagingCursorDto<FreeBoardReplyDto> getReplies(long freeBoardCommentId, long cursor, int size, UserDto userDto) {
		return freeBoardReplyService.get(freeBoardCommentId, cursor, size, userDto);
	}

	public void likeComment(long freeBoardCommentId, UserDto userDto) {
		freeBoardCommentService.like(freeBoardCommentId, userDto);
	}

	public void unLikeComment(long freeBoardCommentId, UserDto userDto) {
		freeBoardCommentService.unLike(freeBoardCommentId, userDto);
	}

	public Long saveReply(FreeBoardReplyDto freeBoardReplyDto, UserDto userDto) {
		final Long replyId = freeBoardReplyService.save(freeBoardReplyDto, userDto);
		final Integer replyCount = freeBoardReplyService.countReplyCountByFreeBoardCommentId(freeBoardReplyDto.getFreeBoardCommentId());
		freeBoardCommentService.updateReplyCount(freeBoardReplyDto.getFreeBoardCommentId(), replyCount);
		return replyId;
	}

	public void removeReply(long id, UserDto userDto) {
		freeBoardReplyService.remove(id, userDto);
	}

	public void likeReply(long freeBoardReplyId, UserDto userDto) {
		freeBoardReplyService.like(freeBoardReplyId, userDto);
	}

	public void unLikeReply(long freeBoardReplyId, UserDto userDto) {
		freeBoardReplyService.unLike(freeBoardReplyId, userDto);
	}
}
