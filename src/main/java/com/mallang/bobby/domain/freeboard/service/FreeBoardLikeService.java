package com.mallang.bobby.domain.freeboard.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mallang.bobby.domain.freeboard.entity.FreeBoardLike;
import com.mallang.bobby.domain.freeboard.repository.FreeBoardLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FreeBoardLikeService {
	private final FreeBoardLikeRepository freeBoardLikeRepository;

	public boolean isLike(long freeBoardId, long userId) {
		final FreeBoardLike freeBoardLike = freeBoardLikeRepository.findByFreeBoardIdAndUserId(freeBoardId, userId).orElse(null);

		return freeBoardLike != null;
	}

	public void like(long freeBoardId, long userId) {
		final FreeBoardLike freeBoardLike = freeBoardLikeRepository.findByFreeBoardIdAndUserId(freeBoardId, userId).orElse(new FreeBoardLike());
		freeBoardLike.setFreeBoardId(freeBoardId);
		freeBoardLike.setUserId(userId);

		freeBoardLikeRepository.save(freeBoardLike);
	}

	public void unLike(long freeBoardId, long userId) {
		final FreeBoardLike freeBoardLike = freeBoardLikeRepository.findByFreeBoardIdAndUserId(freeBoardId, userId).orElse(null);

		if (freeBoardLike == null) {
			return;
		}

		freeBoardLikeRepository.deleteById(freeBoardLike.getId());
	}

	public List<Long> getLikeFreeBoardIds(List<Long> freeBoardIds, long userId) {
		return freeBoardLikeRepository.findAllByFreeBoardIdInAndUserId(freeBoardIds, userId).stream()
			.map(FreeBoardLike::getFreeBoardId)
			.collect(Collectors.toList());
	}
}
