package com.mallang.bobby.domain.freeboard.repository.freeBoardCommentReplyRepository;

import java.util.List;

import com.mallang.bobby.domain.freeboard.entity.FreeBoardCommentReply;

public interface CustomFreeBoardCommentReplyRepository {
	List<FreeBoardCommentReply> findAllByFreeBoardCommentIdOrderByIdDesc(Long freeBoardCommentId, Long cursor, int size);
}
