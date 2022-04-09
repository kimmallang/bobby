package com.mallang.bobby.domain.freeboard.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mallang.bobby.domain.freeboard.entity.FreeBoardLike;

@Repository
public interface FreeBoardLikeRepository extends JpaRepository<FreeBoardLike, Long> {
	Optional<FreeBoardLike> findByFreeBoardIdAndUserId(Long freeBoard, Long userId);
}