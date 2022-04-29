package com.mallang.bobby.domain.freeboard;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.mallang.bobby.config.TestConfig;
import com.mallang.bobby.domain.freeboard.repository.FreeBoardCommentLikeRepository;
import com.mallang.bobby.domain.freeboard.service.FreeBoardCommentLikeService;

@Import(TestConfig.class)
@DataJpaTest
@Transactional
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class FreeBoardCommentLikeServiceTest {
	private FreeBoardCommentLikeService freeBoardCommentLikeService;

	@Autowired
	private FreeBoardCommentLikeRepository freeBoardCommentLikeRepository;

	private final long freeBoardCommentId = 1234L;
	private final long userId = 1111L;

	@BeforeEach
	public void init() {
		freeBoardCommentLikeService = new FreeBoardCommentLikeService(freeBoardCommentLikeRepository);
	}

	@Test
	public void isLike() {
		assertFalse(freeBoardCommentLikeService.isLike(freeBoardCommentId, userId));
	}

	@Test
	public void like() {
		freeBoardCommentLikeService.like(freeBoardCommentId, userId);
		assertTrue(freeBoardCommentLikeService.isLike(freeBoardCommentId, userId));
	}

	@Test
	public void unLike() {
		freeBoardCommentLikeService.like(freeBoardCommentId, userId);
		assertTrue(freeBoardCommentLikeService.isLike(freeBoardCommentId, userId));

		freeBoardCommentLikeService.unLike(freeBoardCommentId, userId);
		assertFalse(freeBoardCommentLikeService.isLike(freeBoardCommentId, userId));
	}
}
