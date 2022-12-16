package com.mallang.bobby.domain.freeboard.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mallang.bobby.domain.freeboard.entity.FreeBoardReplyLike;

@Repository
public interface FreeBoardReplyLikeRepository extends JpaRepository<FreeBoardReplyLike, Long> {
	Boolean existsByFreeBoardReplyIdAndUserId(Long freeBoardReplyId, Long userId);
	Optional<FreeBoardReplyLike> findByFreeBoardReplyIdAndUserId(Long freeBoardReplyId, Long userId);
	List<FreeBoardReplyLike> findAllByFreeBoardReplyIdInAndUserId(List<Long> freeBoardReplyIds, Long userId);
}
