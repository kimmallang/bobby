package com.mallang.bobby.domain.auth.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mallang.bobby.domain.auth.user.entity.User;
import com.mallang.bobby.domain.auth.oauth2.dto.OAuth2Provider;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUserId(Long userId);
	Optional<User> findByAuthorizedByAndUserId(OAuth2Provider provider, Long userId);
}
