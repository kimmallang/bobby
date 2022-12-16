package com.mallang.bobby.domain.freeboard.repository.freeBoardReplyRepository;

import java.util.List;

import com.mallang.bobby.domain.freeboard.entity.FreeBoardReply;

public interface CustomFreeBoardReplyRepository {
	List<FreeBoardReply> findAllByFreeBoardCommentIdOrderByIdDesc(Long freeBoardCommentId, Long cursor, int size);
}
