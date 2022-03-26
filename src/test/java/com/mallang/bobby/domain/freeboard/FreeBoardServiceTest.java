package com.mallang.bobby.domain.freeboard;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.mallang.bobby.domain.auth.user.dto.UserDto;
import com.mallang.bobby.domain.freeboard.dto.FreeBoardDto;
import com.mallang.bobby.domain.freeboard.repository.FreeBoardRepository;
import com.mallang.bobby.domain.freeboard.service.FreeBoardService;

@DataJpaTest
@Transactional
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class FreeBoardServiceTest {
	private ModelMapper modelMapper;
	private FreeBoardService freeBoardService;

	@Autowired
	private FreeBoardRepository freeBoardRepository;

	@BeforeEach
	public void init() {
		modelMapper = new ModelMapper();
		freeBoardService = new FreeBoardService(modelMapper, freeBoardRepository);
	}

	@Test
	public void getList() {
		assertEquals(20, freeBoardService.get(1, 20).getItems().size());
	}

	@Test
	public void get() {
		final FreeBoardDto freeBoardDto = freeBoardService.get(1L, null);
		assertEquals(1L, freeBoardDto.getId());
	}

	@Test
	public void insert() {
		final FreeBoardDto freeBoardDto = FreeBoardDto.builder()
			.title("title")
			.contents("contents")
			.build();

		final UserDto userDto = UserDto.builder()
			.id(1L)
			.nickname("nickname")
			.build();

		freeBoardService.save(freeBoardDto, userDto);
		final FreeBoardDto savedFreeBoardDto = freeBoardService.get(1, 20).getItems().get(0);

		assertEquals("title", savedFreeBoardDto.getTitle());
		assertEquals("contents", savedFreeBoardDto.getContents());
	}

	@Test
	public void update() {
		final FreeBoardDto freeBoardDto = FreeBoardDto.builder()
			.id(1L)
			.title("title")
			.contents("contents")
			.build();

		final UserDto userDto = UserDto.builder()
			.id(1L)
			.nickname("nickname")
			.build();

		freeBoardService.save(freeBoardDto, userDto);
		final FreeBoardDto savedFreeBoardDto = freeBoardService.get(1L, null);

		assertEquals("title", savedFreeBoardDto.getTitle());
		assertEquals("contents", savedFreeBoardDto.getContents());
	}

	@Test
	public void delete() {
		final FreeBoardDto freeBoardBefore = freeBoardService.get(1L, null);
		assertFalse(freeBoardBefore.getDeleteYn());

		UserDto userDto = UserDto.builder()
			.id(freeBoardBefore.getWriterId())
			.build();
		freeBoardService.remove(1L, userDto);

		final FreeBoardDto freeBoardAfter = freeBoardService.get(1L, null);
		assertTrue(freeBoardAfter.getDeleteYn());
	}
}
