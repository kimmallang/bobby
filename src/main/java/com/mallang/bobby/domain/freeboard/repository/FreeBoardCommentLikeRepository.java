package com.mallang.bobby.domain.freeboard.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mallang.bobby.domain.freeboard.entity.FreeBoardCommentLike;

@Repository
public interface FreeBoardCommentLikeRepository extends JpaRepository<FreeBoardCommentLike, Long> {
	Boolean existsByFreeBoardCommentIdAndUserId(Long freeBoardCommentId, Long userId);
	Optional<FreeBoardCommentLike> findByFreeBoardCommentIdAndUserId(Long freeBoardCommentId, Long userId);
	List<FreeBoardCommentLike> findAllByFreeBoardCommentIdInAndUserId(List<Long> freeBoardCommentIds, Long userId);
}
