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
import com.mallang.bobby.domain.freeboard.repository.FreeBoardReplyLikeRepository;
import com.mallang.bobby.domain.freeboard.service.FreeBoardReplyLikeService;

@Import(TestConfig.class)
@DataJpaTest
@Transactional
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class FreeBoardReplyLikeServiceTest {
	private FreeBoardReplyLikeService freeBoardReplyLikeService;

	@Autowired
	private FreeBoardReplyLikeRepository freeBoardReplyLikeRepository;

	private final long freeBoardCommentId = 1234L;
	private final long userId = 1111L;

	@BeforeEach
	public void init() {
		freeBoardReplyLikeService = new FreeBoardReplyLikeService(freeBoardReplyLikeRepository);
	}

	@Test
	public void isLike() {
		assertFalse(freeBoardReplyLikeService.isLike(freeBoardCommentId, userId));
	}

	@Test
	public void like() {
		freeBoardReplyLikeService.like(freeBoardCommentId, userId);
		assertTrue(freeBoardReplyLikeService.isLike(freeBoardCommentId, userId));
	}

	@Test
	public void unLike() {
		freeBoardReplyLikeService.like(freeBoardCommentId, userId);
		assertTrue(freeBoardReplyLikeService.isLike(freeBoardCommentId, userId));

		freeBoardReplyLikeService.unLike(freeBoardCommentId, userId);
		assertFalse(freeBoardReplyLikeService.isLike(freeBoardCommentId, userId));
	}
}
