package com.mallang.bobby.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mallang.bobby.domain.dto.ResponseDto;

@RestController
public class HelloController {
	@GetMapping("/hello")
	public ResponseDto getHello() {
		return ResponseDto.builder().data("hello").build();
	}
}
