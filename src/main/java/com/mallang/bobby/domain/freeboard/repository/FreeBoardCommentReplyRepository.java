package com.mallang.bobby.domain.freeboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mallang.bobby.domain.freeboard.entity.FreeBoardCommentReply;
import com.mallang.bobby.domain.freeboard.repository.freeBoardCommentReplyRepository.CustomFreeBoardCommentReplyRepository;

@Repository
public interface FreeBoardCommentReplyRepository extends JpaRepository<FreeBoardCommentReply, Long>, CustomFreeBoardCommentReplyRepository {
	Boolean existsByFreeBoardCommentIdAndIdLessThan(Long freeBoardCommentId, Long id);
}
