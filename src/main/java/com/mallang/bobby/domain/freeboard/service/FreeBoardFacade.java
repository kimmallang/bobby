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

	public PagingCursorDto<FreeBoardDto> getBoards(long cursor, int size) {
		return freeBoardService.get(cursor, size);
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

	public PagingCursorDto<FreeBoardCommentDto> getComments(long freeBoardId, long cursor, int size) {
		return freeBoardCommentService.get(freeBoardId, cursor, size);
	}

	public FreeBoardCommentDto getComment(long freeBoardId, long id) {
		return freeBoardCommentService.get(freeBoardId, id);
	}

	public Long saveComment(FreeBoardCommentDto freeBoardCommentDto, UserDto userDto) {
		return freeBoardCommentService.save(freeBoardCommentDto, userDto);
	}

	public void removeComment(long id, UserDto userDto) {
		freeBoardCommentService.remove(id, userDto);
	}

	public PagingCursorDto<FreeBoardReplyDto> getReplies(long freeBoardCommentId, long cursor, int size) {
		return freeBoardReplyService.get(freeBoardCommentId, cursor, size);
	}

	public Long saveReply(FreeBoardReplyDto freeBoardReplyDto, UserDto userDto) {
		return freeBoardReplyService.save(freeBoardReplyDto, userDto);
	}

	public void removeReply(long id, UserDto userDto) {
		freeBoardReplyService.remove(id, userDto);
	}
}
