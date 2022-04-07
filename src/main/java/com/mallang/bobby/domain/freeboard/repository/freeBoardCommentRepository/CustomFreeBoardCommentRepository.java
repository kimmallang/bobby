package com.mallang.bobby.domain.freeboard.repository.freeBoardCommentRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mallang.bobby.domain.freeboard.entity.FreeBoardComment;

public interface CustomFreeBoardCommentRepository {
	Page<FreeBoardComment> findAllByFreeBoardIdWithReply(Long freeBoardId, Pageable pageable);
}
