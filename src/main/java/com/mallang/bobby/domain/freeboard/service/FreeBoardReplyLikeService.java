package com.mallang.bobby.domain.freeboard.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mallang.bobby.domain.freeboard.entity.FreeBoardReplyLike;
import com.mallang.bobby.domain.freeboard.repository.FreeBoardReplyLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FreeBoardReplyLikeService {
	private final FreeBoardReplyLikeRepository freeBoardReplyLikeRepository;

	public boolean isLike(long freeBoardReplyId, long userId) {
		return freeBoardReplyLikeRepository.existsByFreeBoardReplyIdAndUserId(freeBoardReplyId, userId);
	}

	public void like(long freeBoardReplyId, long userId) {
		final FreeBoardReplyLike freeBoardCommentLike = freeBoardReplyLikeRepository.findByFreeBoardReplyIdAndUserId(freeBoardReplyId, userId).orElse(new FreeBoardReplyLike());
		freeBoardCommentLike.setFreeBoardReplyId(freeBoardReplyId);
		freeBoardCommentLike.setUserId(userId);

		freeBoardReplyLikeRepository.save(freeBoardCommentLike);
	}

	public void unLike(long freeBoardReplyId, long userId) {
		final FreeBoardReplyLike freeBoardCommentLike = freeBoardReplyLikeRepository.findByFreeBoardReplyIdAndUserId(freeBoardReplyId, userId).orElse(null);

		if (freeBoardCommentLike == null) {
			return;
		}

		freeBoardReplyLikeRepository.deleteById(freeBoardCommentLike.getId());
	}

	public List<Long> getLikeFreeBoardReplyIds(List<Long> freeBoardReplyIds, long userId) {
		return freeBoardReplyLikeRepository.findAllByFreeBoardReplyIdInAndUserId(freeBoardReplyIds, userId).stream()
			.map(FreeBoardReplyLike::getFreeBoardReplyId)
			.collect(Collectors.toList());
	}
}
