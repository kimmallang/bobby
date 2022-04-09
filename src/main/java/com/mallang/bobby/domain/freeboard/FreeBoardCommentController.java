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
import com.mallang.bobby.domain.freeboard.dto.FreeBoardDto;
import com.mallang.bobby.domain.freeboard.service.FreeBoardCommentService;
import com.mallang.bobby.dto.ResponseDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FreeBoardCommentController {
	private final FreeBoardCommentService freeBoardCommentService;

	@GetMapping("/free-board-comment/{freeBoardId}")
	public ResponseDto get(@PathVariable long freeBoardId, int page, int size) {
		return ResponseDto.builder()
			.data(freeBoardCommentService.get(freeBoardId, page, size))
			.build();
	}

	@PostMapping("/free-board-comment")
	public ResponseDto save(@RequestBody FreeBoardCommentDto freeBoardCommentDto, HttpServletRequest request) {
		final UserDto user = (UserDto)request.getAttribute("user");
		freeBoardCommentService.save(freeBoardCommentDto, user);

		return ResponseDto.builder().build();
	}

	@DeleteMapping("/free-board-comment/{id}")
	public ResponseDto delete(@PathVariable long id, HttpServletRequest request) {
		final UserDto user = (UserDto)request.getAttribute("user");
		freeBoardCommentService.remove(id, user);

		return ResponseDto.builder().build();
	}
}
