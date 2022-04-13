package com.mallang.bobby.domain.freeboard.repository.freeBoardCommentRepository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.mallang.bobby.domain.freeboard.entity.FreeBoardComment;
import com.mallang.bobby.domain.freeboard.entity.QFreeBoardComment;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomFreeBoardCommentRepositoryImpl implements CustomFreeBoardCommentRepository {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<FreeBoardComment> findAllByFreeBoardIdOrderByIdDesc(Long freeBoardId, Long cursor, int size) {
		final QFreeBoardComment freeBoardComment = QFreeBoardComment.freeBoardComment;
		return jpaQueryFactory
			.select(freeBoardComment)
			.from(freeBoardComment)
			.where(getWhereClause(freeBoardId, cursor))
			.limit(size)
			.orderBy(freeBoardComment.id.desc())
			.fetch();
	}

	private BooleanExpression getWhereClause(Long freeBoardId, Long cursor) {
		final boolean isFirstPage = cursor <= 0;
		final QFreeBoardComment freeBoardComment = QFreeBoardComment.freeBoardComment;
		if (isFirstPage) {
			return freeBoardComment.freeBoardId.eq(freeBoardId);
		}

		return freeBoardComment.freeBoardId.eq(freeBoardId).and(freeBoardComment.id.lt(cursor));
	}
}
