package com.mallang.bobby.domain.freeboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mallang.bobby.domain.freeboard.entity.FreeBoardReply;
import com.mallang.bobby.domain.freeboard.repository.freeBoardReplyRepository.CustomFreeBoardReplyRepository;

@Repository
public interface FreeBoardReplyRepository
	extends JpaRepository<FreeBoardReply, Long>, CustomFreeBoardReplyRepository {
	Boolean existsByFreeBoardCommentIdAndIdLessThan(Long freeBoardCommentId, Long id);
	Integer countAllByFreeBoardCommentIdAndIsDeletedFalse(Long freeBoardId);
}
