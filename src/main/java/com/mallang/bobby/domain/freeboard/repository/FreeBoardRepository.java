package com.mallang.bobby.domain.freeboard.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mallang.bobby.domain.freeboard.entity.FreeBoard;

@Repository
public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long> {
	Page<FreeBoard> findAllByIsDeleted(Boolean isDeleted, Pageable pageable);
	Optional<FreeBoard> findByIdAndIsDeleted(Long id, Boolean isDeleted);
}
