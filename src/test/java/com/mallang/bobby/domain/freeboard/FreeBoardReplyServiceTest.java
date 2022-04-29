package com.mallang.bobby.domain.freeboard;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.mallang.bobby.config.TestConfig;
import com.mallang.bobby.domain.auth.user.dto.UserDto;
import com.mallang.bobby.domain.freeboard.dto.FreeBoardReplyDto;
import com.mallang.bobby.domain.freeboard.entity.FreeBoardReply;
import com.mallang.bobby.domain.freeboard.repository.FreeBoardReplyLikeRepository;
import com.mallang.bobby.domain.freeboard.repository.FreeBoardReplyRepository;
import com.mallang.bobby.domain.freeboard.service.FreeBoardReplyLikeService;
import com.mallang.bobby.domain.freeboard.service.FreeBoardReplyService;

@Import(TestConfig.class)
@DataJpaTest
@Transactional
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class FreeBoardReplyServiceTest {
	private ModelMapper modelMapper;
	private FreeBoardReplyService freeBoardReplyService;

	@Autowired
	private FreeBoardReplyRepository freeBoardReplyRepository;

	@Autowired
	private FreeBoardReplyLikeRepository freeBoardReplyLikeRepository;

	@BeforeEach
	public void init() {
		modelMapper = new ModelMapper();
		final FreeBoardReplyLikeService freeBoardReplyLikeService = new FreeBoardReplyLikeService(freeBoardReplyLikeRepository);
		freeBoardReplyService = new FreeBoardReplyService(modelMapper, freeBoardReplyRepository, freeBoardReplyLikeService);
	}

	@Test
	public void getList() {
		assertTrue(freeBoardReplyService.get(1L, 0, 1).getItems().size() > 0);
	}

	@Test
	public void insert() {
		final FreeBoardReplyDto freeBoardReplyDto = FreeBoardReplyDto.builder()
			.freeBoardCommentId(1L)
			.contents("contents")
			.build();

		final UserDto userDto = UserDto.builder()
			.id(1L)
			.nickname("nickname")
			.build();

		freeBoardReplyService.save(freeBoardReplyDto, userDto);
		final FreeBoardReplyDto savedFreeBoardReplyDto = freeBoardReplyService.get(1L, 0, 20).getItems().get(0);

		assertEquals("contents", savedFreeBoardReplyDto.getContents());
	}

	@Test
	public void update() {
		final FreeBoardReplyDto freeBoardReplyDto = FreeBoardReplyDto.builder()
			.id(1L)
			.freeBoardCommentId(1L)
			.contents("contents")
			.build();

		final UserDto userDto = UserDto.builder()
			.id(1L)
			.nickname("nickname")
			.build();

		freeBoardReplyService.save(freeBoardReplyDto, userDto);

		final FreeBoardReplyDto savedFreeBoardReplyDto = findById(1L);

		assertNotNull(savedFreeBoardReplyDto);
		assertEquals("contents", savedFreeBoardReplyDto.getContents());
	}

	@Test
	public void delete() {
		final FreeBoardReplyDto freeBoardCommentReplyBefore = findById(1L);
		assertNotNull(freeBoardCommentReplyBefore);
		assertFalse(freeBoardCommentReplyBefore.getIsDeleted());

		UserDto userDto = UserDto.builder()
			.id(freeBoardCommentReplyBefore.getWriterId())
			.build();
		freeBoardReplyService.remove(1L, userDto);

		final FreeBoardReplyDto freeBoardCommentReplyAfter = findById(1L);
		assertNull(freeBoardCommentReplyAfter);
	}

	private FreeBoardReplyDto findById(long id) {
		FreeBoardReply freeBoardReply = freeBoardReplyRepository.findById(id).orElse(null);
		if (freeBoardReply == null || freeBoardReply.getIsDeleted()) {
			return null;
		}

		return modelMapper.map(freeBoardReply, FreeBoardReplyDto.class);
	}
}
