package com.mallang.bobby.domain.freeboard;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mallang.bobby.domain.auth.user.dto.UserDto;
import com.mallang.bobby.domain.freeboard.dto.FreeBoardCommentReplyDto;
import com.mallang.bobby.domain.freeboard.service.FreeBoardCommentReplyService;
import com.mallang.bobby.dto.ResponseDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FreeBoardCommentReplyController {
	private final FreeBoardCommentReplyService freeBoardCommentReplyService;

	@GetMapping("/free-board-comment-reply/{freeBoardCommentId}")
	public ResponseDto get(@PathVariable long freeBoardCommentId, int page, int size) {
		return ResponseDto.builder()
			.data(freeBoardCommentReplyService.get(freeBoardCommentId, page, size))
			.build();
	}

	@PostMapping("/free-board-comment-reply")
	public ResponseDto save(@RequestBody FreeBoardCommentReplyDto freeBoardCommentReplyDto, HttpServletRequest request) {
		final UserDto user = (UserDto)request.getAttribute("user");
		freeBoardCommentReplyService.save(freeBoardCommentReplyDto, user);

		return ResponseDto.builder().build();
	}

	@DeleteMapping("/free-board-comment-reply/{id}")
	public ResponseDto delete(@PathVariable long id, HttpServletRequest request) {
		final UserDto user = (UserDto)request.getAttribute("user");
		freeBoardCommentReplyService.remove(id, user);

		return ResponseDto.builder().build();
	}
}
