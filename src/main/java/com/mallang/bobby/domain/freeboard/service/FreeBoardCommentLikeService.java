package com.mallang.bobby.domain.freeboard.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mallang.bobby.domain.auth.user.dto.UserDto;
import com.mallang.bobby.domain.freeboard.entity.FreeBoardCommentLike;
import com.mallang.bobby.domain.freeboard.repository.FreeBoardCommentLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FreeBoardCommentLikeService {
	private final FreeBoardCommentLikeRepository freeBoardCommentLikeRepository;

	public boolean isLike(long freeBoardCommentId, long userId) {
		return freeBoardCommentLikeRepository.existsByFreeBoardCommentIdAndUserId(freeBoardCommentId, userId);
	}

	public void like(long freeBoardCommentId, long userId) {
		final FreeBoardCommentLike freeBoardCommentLike = freeBoardCommentLikeRepository.findByFreeBoardCommentIdAndUserId(freeBoardCommentId, userId).orElse(new FreeBoardCommentLike());
		freeBoardCommentLike.setFreeBoardCommentId(freeBoardCommentId);
		freeBoardCommentLike.setUserId(userId);

		freeBoardCommentLikeRepository.save(freeBoardCommentLike);
	}

	public void unLike(long freeBoardCommentId, long userId) {
		final FreeBoardCommentLike freeBoardCommentLike = freeBoardCommentLikeRepository.findByFreeBoardCommentIdAndUserId(freeBoardCommentId, userId).orElse(null);

		if (freeBoardCommentLike == null) {
			return;
		}

		freeBoardCommentLikeRepository.deleteById(freeBoardCommentLike.getId());
	}

	public List<Long> getLikeFreeBoardCommentIds(List<Long> freeBoardCommentIds, long userId) {
		return freeBoardCommentLikeRepository.findAllByFreeBoardCommentIdInAndUserId(freeBoardCommentIds, userId).stream()
			.map(FreeBoardCommentLike::getFreeBoardCommentId)
			.collect(Collectors.toList());
	}
}
