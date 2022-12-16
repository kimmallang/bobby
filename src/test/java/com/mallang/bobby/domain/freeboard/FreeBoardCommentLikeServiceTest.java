package com.mallang.bobby.domain.freeboard;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FreeBoardCommentLikeServiceTest {
	private FreeBoardCommentLikeService freeBoardCommentLikeService;

	@Autowired
	private FreeBoardCommentLikeRepository freeBoardCommentLikeRepository;

	private final long FREE_BOARD_COMMENT_ID = 1234L;
	private final long FREE_BOARD_COMMENT_ID_2 = 2468L;
	private final long FREE_BOARD_COMMENT_ID_3 = 1357L;
	private final long USER_ID = 1111L;

	@BeforeEach
	public void init() {
		freeBoardCommentLikeService = new FreeBoardCommentLikeService(freeBoardCommentLikeRepository);
	}

	@Test
	public void isLike() {
		assertFalse(freeBoardCommentLikeService.isLike(FREE_BOARD_COMMENT_ID, USER_ID));
	}

	@Test
	public void like() {
		freeBoardCommentLikeService.like(FREE_BOARD_COMMENT_ID, USER_ID);
		assertTrue(freeBoardCommentLikeService.isLike(FREE_BOARD_COMMENT_ID, USER_ID));
	}

	@Test
	public void unLike() {
		freeBoardCommentLikeService.like(FREE_BOARD_COMMENT_ID, USER_ID);
		assertTrue(freeBoardCommentLikeService.isLike(FREE_BOARD_COMMENT_ID, USER_ID));

		freeBoardCommentLikeService.unLike(FREE_BOARD_COMMENT_ID, USER_ID);
		assertFalse(freeBoardCommentLikeService.isLike(FREE_BOARD_COMMENT_ID, USER_ID));
	}

	@Test
	public void getLikeFreeBoardCommentIds() {
		freeBoardCommentLikeService.like(FREE_BOARD_COMMENT_ID, USER_ID);
		freeBoardCommentLikeService.like(FREE_BOARD_COMMENT_ID_2, USER_ID);

		List<Long> likeFreeBoardIds = freeBoardCommentLikeService.getLikeFreeBoardCommentIds(
			Arrays.asList(FREE_BOARD_COMMENT_ID, FREE_BOARD_COMMENT_ID_2, FREE_BOARD_COMMENT_ID_3), USER_ID);
		assertEquals(2, likeFreeBoardIds.size());
		assertEquals(FREE_BOARD_COMMENT_ID, likeFreeBoardIds.get(0));
		assertEquals(FREE_BOARD_COMMENT_ID_2, likeFreeBoardIds.get(1));
	}
}
