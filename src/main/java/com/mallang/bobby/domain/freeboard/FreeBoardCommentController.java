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
import com.mallang.bobby.domain.freeboard.dto.FreeBoardCommentDto;
import com.mallang.bobby.domain.freeboard.service.FreeBoardFacade;
import com.mallang.bobby.dto.ResponseDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FreeBoardCommentController {
	private final FreeBoardFacade freeBoardFacade;

	@GetMapping("/free-board-comment/{freeBoardId}")
	public ResponseDto get(@PathVariable long freeBoardId, long cursor, int size, HttpServletRequest request) {
		final UserDto user = (UserDto)request.getAttribute("user");
		return ResponseDto.builder()
			.data(freeBoardFacade.getComments(freeBoardId, cursor, size, user))
			.build();
	}

	@GetMapping("/free-board-comment/{freeBoardId}/{id}")
	public ResponseDto getWithReply(@PathVariable long freeBoardId, @PathVariable long id, HttpServletRequest request) {
		final UserDto user = (UserDto)request.getAttribute("user");
		return ResponseDto.builder()
			.data(freeBoardFacade.getComment(freeBoardId, id, user))
			.build();
	}

	@PostMapping("/free-board-comment")
	public ResponseDto save(@RequestBody FreeBoardCommentDto freeBoardCommentDto, HttpServletRequest request) {
		final UserDto user = (UserDto)request.getAttribute("user");
		freeBoardFacade.saveComment(freeBoardCommentDto, user);

		return ResponseDto.builder().build();
	}

	@DeleteMapping("/free-board-comment/{id}")
	public ResponseDto delete(@PathVariable long id, HttpServletRequest request) {
		final UserDto user = (UserDto)request.getAttribute("user");
		freeBoardFacade.removeComment(id, user);

		return ResponseDto.builder().build();
	}

	@PostMapping("/free-board-comment/like/{id}")
	public ResponseDto like(@PathVariable long id, HttpServletRequest request) {
		final UserDto user = (UserDto)request.getAttribute("user");
		freeBoardFacade.likeComment(id, user);

		return ResponseDto.builder().build();
	}

	@DeleteMapping("/free-board-comment/like/{id}")
	public ResponseDto unLike(@PathVariable long id, HttpServletRequest request) {
		final UserDto user = (UserDto)request.getAttribute("user");
		freeBoardFacade.unLikeComment(id, user);

		return ResponseDto.builder().build();
	}
}
