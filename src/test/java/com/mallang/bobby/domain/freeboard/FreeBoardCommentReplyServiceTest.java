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

import com.mallang.bobby.config.TestQuerydslConfig;
import com.mallang.bobby.domain.auth.user.dto.UserDto;
import com.mallang.bobby.domain.freeboard.dto.FreeBoardCommentReplyDto;
import com.mallang.bobby.domain.freeboard.entity.FreeBoardCommentReply;
import com.mallang.bobby.domain.freeboard.repository.FreeBoardCommentReplyRepository;
import com.mallang.bobby.domain.freeboard.service.FreeBoardCommentReplyService;

@Import(TestQuerydslConfig.class)
@DataJpaTest
@Transactional
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class FreeBoardCommentReplyServiceTest {
	private ModelMapper modelMapper;
	private FreeBoardCommentReplyService freeBoardCommentReplyService;

	@Autowired
	private FreeBoardCommentReplyRepository freeBoardCommentReplyRepository;

	@BeforeEach
	public void init() {
		modelMapper = new ModelMapper();
		freeBoardCommentReplyService = new FreeBoardCommentReplyService(modelMapper, freeBoardCommentReplyRepository);
	}

	@Test
	public void getList() {
		assertEquals(1, freeBoardCommentReplyService.get(1L, 1, 1).getItems().size());
	}

	@Test
	public void insert() {
		final FreeBoardCommentReplyDto freeBoardCommentReplyDto = FreeBoardCommentReplyDto.builder()
			.freeBoardCommentId(1L)
			.contents("contents")
			.build();

		final UserDto userDto = UserDto.builder()
			.id(1L)
			.nickname("nickname")
			.build();

		freeBoardCommentReplyService.save(freeBoardCommentReplyDto, userDto);
		final FreeBoardCommentReplyDto savedFreeBoardCommentReplyDto = (FreeBoardCommentReplyDto)freeBoardCommentReplyService.get(1L, 1, 20).getItems().get(0);

		assertEquals("contents", savedFreeBoardCommentReplyDto.getContents());
	}

	@Test
	public void update() {
		final FreeBoardCommentReplyDto freeBoardCommentReplyDto = FreeBoardCommentReplyDto.builder()
			.id(1L)
			.freeBoardCommentId(1L)
			.contents("contents")
			.build();

		final UserDto userDto = UserDto.builder()
			.id(1L)
			.nickname("nickname")
			.build();

		freeBoardCommentReplyService.save(freeBoardCommentReplyDto, userDto);

		final FreeBoardCommentReplyDto savedFreeBoardCommentReplyDto = findById(1L);

		assertNotNull(savedFreeBoardCommentReplyDto);
		assertEquals("contents", savedFreeBoardCommentReplyDto.getContents());
	}

	@Test
	public void delete() {
		final FreeBoardCommentReplyDto freeBoardCommentReplyBefore = findById(1L);
		assertNotNull(freeBoardCommentReplyBefore);
		assertFalse(freeBoardCommentReplyBefore.getIsDeleted());

		UserDto userDto = UserDto.builder()
			.id(freeBoardCommentReplyBefore.getWriterId())
			.build();
		freeBoardCommentReplyService.remove(1L, userDto);

		final FreeBoardCommentReplyDto freeBoardCommentReplyAfter = findById(1L);
		assertNull(freeBoardCommentReplyAfter);
	}

	private FreeBoardCommentReplyDto findById(long id) {
		FreeBoardCommentReply freeBoardCommentReply = freeBoardCommentReplyRepository.findById(id).orElse(null);
		if (freeBoardCommentReply == null || freeBoardCommentReply.getIsDeleted()) {
			return null;
		}

		return modelMapper.map(freeBoardCommentReply, FreeBoardCommentReplyDto.class);
	}
}
