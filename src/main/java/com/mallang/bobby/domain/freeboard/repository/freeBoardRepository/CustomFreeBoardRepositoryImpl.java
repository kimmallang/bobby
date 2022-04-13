package com.mallang.bobby.domain.freeboard.repository.freeBoardRepository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mallang.bobby.domain.freeboard.entity.FreeBoard;
import com.mallang.bobby.domain.freeboard.entity.FreeBoardComment;
import com.mallang.bobby.domain.freeboard.entity.QFreeBoard;
import com.mallang.bobby.domain.freeboard.entity.QFreeBoardComment;
import com.mallang.bobby.domain.freeboard.repository.freeBoardCommentRepository.CustomFreeBoardCommentRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomFreeBoardRepositoryImpl implements CustomFreeBoardRepository {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<FreeBoard> findAllOrderByIdDesc(Long cursor, int size) {
		final QFreeBoard freeBoard = QFreeBoard.freeBoard;
		return jpaQueryFactory
			.select(freeBoard)
			.from(freeBoard)
			.where(getWhereClause(cursor))
			.limit(size)
			.orderBy(freeBoard.id.desc())
			.fetch();
	}

	private BooleanExpression getWhereClause(Long cursor) {
		final boolean isFirstPage = cursor <= 0;
		final QFreeBoard freeBoard = QFreeBoard.freeBoard;
		if (isFirstPage) {
			return freeBoard.isDeleted.eq(false);
		}

		return freeBoard.isDeleted.eq(false).and(freeBoard.id.lt(cursor));
	}
}
