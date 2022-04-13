package com.mallang.bobby.domain.freeboard.repository.freeBoardRepository;

import java.util.List;

import com.mallang.bobby.domain.freeboard.entity.FreeBoard;

public interface CustomFreeBoardRepository {
	List<FreeBoard> findAllOrderByIdDesc(Long cursor, int size);
}
