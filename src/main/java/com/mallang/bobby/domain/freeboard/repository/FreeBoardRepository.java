package com.mallang.bobby.domain.freeboard.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mallang.bobby.domain.freeboard.entity.FreeBoard;
import com.mallang.bobby.domain.freeboard.repository.freeBoardRepository.CustomFreeBoardRepository;

@Repository
public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long>, CustomFreeBoardRepository {
	Optional<FreeBoard> findByIdAndIsDeleted(Long id, Boolean isDeleted);
	Boolean existsByIsDeletedFalseAndIdLessThan(Long id);
}
