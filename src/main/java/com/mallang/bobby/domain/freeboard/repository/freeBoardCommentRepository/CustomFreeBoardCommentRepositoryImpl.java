package com.mallang.bobby.domain.freeboard.repository.freeBoardCommentRepository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.mallang.bobby.domain.freeboard.entity.FreeBoardComment;
import com.mallang.bobby.domain.freeboard.entity.QFreeBoardComment;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomFreeBoardCommentRepositoryImpl implements CustomFreeBoardCommentRepository {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<FreeBoardComment> findAllByFreeBoardIdWithReply(Long freeBoardId, Pageable pageable) {
		final QFreeBoardComment freeBoardComment = QFreeBoardComment.freeBoardComment;
		final List<FreeBoardComment> freeBoardComments = jpaQueryFactory
			.select(freeBoardComment)
			.from(freeBoardComment)
			.where(freeBoardComment.id.eq(freeBoardId))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		final JPAQuery<FreeBoardComment> countQuery = jpaQueryFactory
			.select(freeBoardComment)
			.from(freeBoardComment)
			.where(freeBoardComment.id.eq(freeBoardId));

		return PageableExecutionUtils.getPage(freeBoardComments, pageable, countQuery::fetchCount);
	}
}
