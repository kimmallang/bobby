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
import com.mallang.bobby.domain.freeboard.dto.FreeBoardDto;
import com.mallang.bobby.domain.freeboard.service.FreeBoardService;
import com.mallang.bobby.dto.ResponseDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FreeBoardController {
	private final FreeBoardService freeBoardService;

	@GetMapping("/free-board")
	public ResponseDto get(int page, int size) {
		return ResponseDto.builder()
			.data(freeBoardService.get(page, size))
			.build();
	}

	@GetMapping("/free-board/{id}")
	public ResponseDto get(@PathVariable long id, HttpServletRequest request) {
		final UserDto user = (UserDto)request.getAttribute("user");
		return ResponseDto.builder()
			.data(freeBoardService.get(id, user))
			.build();
	}

	@PostMapping("/free-board")
	public ResponseDto save(@RequestBody FreeBoardDto freeBoardDto, HttpServletRequest request) {
		final UserDto user = (UserDto)request.getAttribute("user");
		freeBoardService.save(freeBoardDto, user);

		return ResponseDto.builder().build();
	}

	@DeleteMapping("/free-board/{id}")
	public ResponseDto delete(@PathVariable long id, HttpServletRequest request) {
		final UserDto user = (UserDto)request.getAttribute("user");
		freeBoardService.remove(id, user);

		return ResponseDto.builder().build();
	}

	@PostMapping("/free-board/like/{id}")
	public ResponseDto like(@PathVariable long id, HttpServletRequest request) {
		final UserDto user = (UserDto)request.getAttribute("user");
		freeBoardService.like(id, user);

		return ResponseDto.builder().build();
	}

	@DeleteMapping("/free-board/like/{id}")
	public ResponseDto unLike(@PathVariable long id, HttpServletRequest request) {
		final UserDto user = (UserDto)request.getAttribute("user");
		freeBoardService.unLike(id, user);

		return ResponseDto.builder().build();
	}
}
