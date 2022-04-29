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
import com.mallang.bobby.domain.freeboard.dto.FreeBoardReplyDto;
import com.mallang.bobby.domain.freeboard.service.FreeBoardFacade;
import com.mallang.bobby.dto.ResponseDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FreeBoardReplyController {
	private final FreeBoardFacade freeBoardFacade;

	@GetMapping("/free-board-reply/{freeBoardCommentId}")
	public ResponseDto get(@PathVariable long freeBoardCommentId, long cursor, int size) {
		return ResponseDto.builder()
			.data(freeBoardFacade.getReplies(freeBoardCommentId, cursor, size))
			.build();
	}

	@PostMapping("/free-board-reply")
	public ResponseDto save(@RequestBody FreeBoardReplyDto freeBoardReplyDto, HttpServletRequest request) {
		final UserDto user = (UserDto)request.getAttribute("user");
		freeBoardFacade.saveReply(freeBoardReplyDto, user);

		return ResponseDto.builder().build();
	}

	@DeleteMapping("/free-board-reply/{id}")
	public ResponseDto delete(@PathVariable long id, HttpServletRequest request) {
		final UserDto user = (UserDto)request.getAttribute("user");
		freeBoardFacade.removeReply(id, user);

		return ResponseDto.builder().build();
	}

	@PostMapping("/free-board-reply/like/{id}")
	public ResponseDto like(@PathVariable long id, HttpServletRequest request) {
		final UserDto user = (UserDto)request.getAttribute("user");
		freeBoardFacade.likeReply(id, user);

		return ResponseDto.builder().build();
	}

	@DeleteMapping("/free-board-reply/like/{id}")
	public ResponseDto unLike(@PathVariable long id, HttpServletRequest request) {
		final UserDto user = (UserDto)request.getAttribute("user");
		freeBoardFacade.unLikeReply(id, user);

		return ResponseDto.builder().build();
	}
}
