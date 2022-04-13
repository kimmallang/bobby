package com.mallang.bobby.domain.freeboard.repository.freeBoardCommentRepository;

import java.util.List;

import com.mallang.bobby.domain.freeboard.entity.FreeBoardComment;

public interface CustomFreeBoardCommentRepository {
	List<FreeBoardComment> findAllByFreeBoardIdOrderByIdDesc(Long freeBoardId, Long cursor, int size);
}
