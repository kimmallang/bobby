package com.mallang.bobby.domain.freeboard.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mallang.bobby.domain.freeboard.entity.FreeBoardComment;
import com.mallang.bobby.domain.freeboard.repository.freeBoardCommentRepository.CustomFreeBoardCommentRepository;

@Repository
public interface FreeBoardCommentRepository extends JpaRepository<FreeBoardComment, Long>, CustomFreeBoardCommentRepository {
	Page<FreeBoardComment> findAllByFreeBoardId(Long freeBoardId, Pageable pageable);
	Boolean existsByFreeBoardIdAndIdLessThan(Long freeBoardId, Long id);
	Integer countAllByFreeBoardIdAndIsDeletedFalse(Long freeBoardId);
}
