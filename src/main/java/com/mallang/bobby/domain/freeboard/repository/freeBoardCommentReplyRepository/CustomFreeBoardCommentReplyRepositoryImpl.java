package com.mallang.bobby.domain.freeboard.repository.freeBoardCommentReplyRepository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mallang.bobby.domain.freeboard.entity.FreeBoardCommentReply;
import com.mallang.bobby.domain.freeboard.entity.QFreeBoardComment;
import com.mallang.bobby.domain.freeboard.entity.QFreeBoardCommentReply;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomFreeBoardCommentReplyRepositoryImpl implements CustomFreeBoardCommentReplyRepository {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<FreeBoardCommentReply> findAllByFreeBoardCommentIdOrderByIdDesc(Long freeBoardCommentId, Long cursor, int size) {
		final QFreeBoardCommentReply freeBoardCommentReply = QFreeBoardCommentReply.freeBoardCommentReply;
		return jpaQueryFactory
			.select(freeBoardCommentReply)
			.from(freeBoardCommentReply)
			.where(getWhereClause(freeBoardCommentId, cursor))
			.limit(size)
			.orderBy(freeBoardCommentReply.id.desc())
			.fetch();
	}

	private BooleanExpression getWhereClause(Long freeBoardCommentId, Long cursor) {
		final boolean isFirstPage = cursor <= 0;
		final QFreeBoardCommentReply freeBoardCommentReply = QFreeBoardCommentReply.freeBoardCommentReply;
		if (isFirstPage) {
			return freeBoardCommentReply.freeBoardCommentId.eq(freeBoardCommentId);
		}

		return freeBoardCommentReply.freeBoardCommentId.eq(freeBoardCommentId).and(freeBoardCommentReply.id.lt(cursor));
	}
}
