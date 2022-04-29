package com.mallang.bobby.domain.freeboard.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mallang.bobby.domain.freeboard.entity.FreeBoardLike;

@Repository
public interface FreeBoardLikeRepository extends JpaRepository<FreeBoardLike, Long> {
	Boolean existsByFreeBoardIdAndUserId(Long freeBoardId, Long userId);
	Optional<FreeBoardLike> findByFreeBoardIdAndUserId(Long freeBoardId, Long userId);
	List<FreeBoardLike> findAllByFreeBoardIdInAndUserId(List<Long> freeBoardIds, Long userId);
}
