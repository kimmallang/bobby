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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.mallang.bobby.config.TestConfig;
import com.mallang.bobby.domain.auth.user.dto.UserDto;
import com.mallang.bobby.domain.freeboard.dto.FreeBoardCommentDto;
import com.mallang.bobby.domain.freeboard.entity.FreeBoardComment;
import com.mallang.bobby.domain.freeboard.repository.FreeBoardCommentReplyRepository;
import com.mallang.bobby.domain.freeboard.repository.FreeBoardCommentRepository;
import com.mallang.bobby.domain.freeboard.service.FreeBoardCommentReplyService;
import com.mallang.bobby.domain.freeboard.service.FreeBoardCommentService;
import com.mallang.bobby.dto.PagingCursorDto;

@Import(TestConfig.class)
@DataJpaTest
@Transactional
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class FreeBoardCommentServiceTest {
	private ModelMapper modelMapper;
	private FreeBoardCommentService freeBoardCommentService;

	@Autowired
	private FreeBoardCommentRepository freeBoardCommentRepository;

	@Autowired
	private FreeBoardCommentReplyRepository freeBoardCommentReplyRepository;

	@BeforeEach
	public void init() {
		modelMapper = new ModelMapper();
		FreeBoardCommentReplyService freeBoardCommentReplyService = new FreeBoardCommentReplyService(modelMapper, freeBoardCommentReplyRepository);
		freeBoardCommentService = new FreeBoardCommentService(modelMapper, freeBoardCommentRepository, freeBoardCommentReplyService);
	}

	@Test
	public void getList() {
		PagingCursorDto<FreeBoardCommentDto> freeBoardCommentPage = freeBoardCommentService.get(1L, 0, 3);
		assertTrue(freeBoardCommentPage.getItems().size() > 0);
		assertFalse(freeBoardCommentPage.getIsLast());

		PagingCursorDto<FreeBoardCommentDto> freeBoardCommentPage2 = freeBoardCommentService.get(1L, freeBoardCommentPage.getCursor(), 20);
		assertTrue(freeBoardCommentPage2.getItems().size() > 0);
		assertTrue(freeBoardCommentPage2.getIsLast());
	}

	@Test
	public void insert() {
		final FreeBoardCommentDto freeBoardCommentDto = FreeBoardCommentDto.builder()
			.freeBoardId(1L)
			.contents("contents")
			.build();

		final UserDto userDto = UserDto.builder()
			.id(1L)
			.nickname("nickname")
			.build();

		final Sort sortByIdDesc = Sort.by(Sort.Direction.DESC, "id");
		final Pageable pageable = PageRequest.of(0, 20, sortByIdDesc);
		long beforeCount = freeBoardCommentRepository.findAllByFreeBoardId(1L, pageable).getTotalElements();

		freeBoardCommentService.save(freeBoardCommentDto, userDto);

		long afterCount = freeBoardCommentRepository.findAllByFreeBoardId(1L, pageable).getTotalElements();

		assertEquals(beforeCount + 1, afterCount);
	}

	@Test
	public void update() {
		final FreeBoardCommentDto freeBoardCommentDto = FreeBoardCommentDto.builder()
			.id(1L)
			.freeBoardId(1L)
			.contents("contents")
			.build();

		final UserDto userDto = UserDto.builder()
			.id(1L)
			.nickname("nickname")
			.build();

		freeBoardCommentService.save(freeBoardCommentDto, userDto);

		final FreeBoardCommentDto savedFreeBoardCommentDto = findById(1L);

		assertNotNull(savedFreeBoardCommentDto);
		assertEquals("contents", savedFreeBoardCommentDto.getContents());
	}

	@Test
	public void delete() {
		final FreeBoardCommentDto freeBoardCommentBefore = findById(1L);
		assertNotNull(freeBoardCommentBefore);
		assertFalse(freeBoardCommentBefore.getIsDeleted());

		UserDto userDto = UserDto.builder()
			.id(freeBoardCommentBefore.getWriterId())
			.build();
		freeBoardCommentService.remove(1L, userDto);

		final FreeBoardCommentDto freeBoardCommentAfter = findById(1L);
		assertNull(freeBoardCommentAfter);
	}

	private FreeBoardCommentDto findById(long id) {
		FreeBoardComment freeBoardComment = freeBoardCommentRepository.findById(id).orElse(null);
		if (freeBoardComment == null || freeBoardComment.getIsDeleted()) {
			return null;
		}

		return modelMapper.map(freeBoardComment, FreeBoardCommentDto.class);
	}
}
