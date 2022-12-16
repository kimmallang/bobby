package com.mallang.bobby.domain.freeboard.repository.freeBoardReplyRepository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mallang.bobby.domain.freeboard.entity.FreeBoardReply;
import com.mallang.bobby.domain.freeboard.entity.QFreeBoardReply;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomFreeBoardReplyRepositoryImpl implements CustomFreeBoardReplyRepository {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<FreeBoardReply> findAllByFreeBoardCommentIdOrderByIdDesc(Long freeBoardCommentId, Long cursor, int size) {
		final QFreeBoardReply freeBoardReply = QFreeBoardReply.freeBoardReply;
		return jpaQueryFactory
			.select(freeBoardReply)
			.from(freeBoardReply)
			.where(getWhereClause(freeBoardCommentId, cursor))
			.limit(size)
			.orderBy(freeBoardReply.id.desc())
			.fetch();
	}

	private BooleanExpression getWhereClause(Long freeBoardCommentId, Long cursor) {
		final boolean isFirstPage = cursor <= 0;
		final QFreeBoardReply freeBoardReply = QFreeBoardReply.freeBoardReply;
		if (isFirstPage) {
			return freeBoardReply.freeBoardCommentId.eq(freeBoardCommentId);
		}

		return freeBoardReply.freeBoardCommentId.eq(freeBoardCommentId).and(freeBoardReply.id.lt(cursor));
	}
}
