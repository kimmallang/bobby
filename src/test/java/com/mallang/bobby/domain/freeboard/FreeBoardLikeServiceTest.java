package com.mallang.bobby.domain.freeboard;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.mallang.bobby.domain.freeboard.repository.FreeBoardLikeRepository;
import com.mallang.bobby.domain.freeboard.service.FreeBoardLikeService;

@DataJpaTest
@Transactional
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class FreeBoardLikeServiceTest {
	private FreeBoardLikeService freeBoardLikeService;

	@Autowired
	private FreeBoardLikeRepository freeBoardLikeRepository;

	private final long freeBoardId = 1234L;
	private final long userId = 1111L;

	@BeforeEach
	public void init() {
		freeBoardLikeService = new FreeBoardLikeService(freeBoardLikeRepository);
	}

	@Test
	public void isLike() {
		assertFalse(freeBoardLikeService.isLike(freeBoardId, userId));
	}

	@Test
	public void like() {
		freeBoardLikeService.like(freeBoardId, userId);
		assertTrue(freeBoardLikeService.isLike(freeBoardId, userId));
	}

	@Test
	public void unLike() {
		freeBoardLikeService.like(freeBoardId, userId);
		assertTrue(freeBoardLikeService.isLike(freeBoardId, userId));

		freeBoardLikeService.unLike(freeBoardId, userId);
		assertFalse(freeBoardLikeService.isLike(freeBoardId, userId));
	}
}
