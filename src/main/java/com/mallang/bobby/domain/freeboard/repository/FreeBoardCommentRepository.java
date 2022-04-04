package com.mallang.bobby.domain.freeboard.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mallang.bobby.domain.freeboard.entity.FreeBoardComment;

@Repository
public interface FreeBoardCommentRepository extends JpaRepository<FreeBoardComment, Long> {
	Page<FreeBoardComment> findAllByFreeBoardId(Long longs, Pageable pageable);
}
