package com.mallang.bobby.domain.freeboard.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mallang.bobby.domain.freeboard.entity.FreeBoardCommentReply;

@Repository
public interface FreeBoardCommentReplyRepository extends JpaRepository<FreeBoardCommentReply, Long> {
	Page<FreeBoardCommentReply> findAllByFreeBoardCommentId(Long longs, Pageable pageable);
}
