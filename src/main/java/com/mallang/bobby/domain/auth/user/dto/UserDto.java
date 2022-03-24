package com.mallang.bobby.domain.auth.user.dto;

import com.mallang.bobby.domain.auth.oauth2.dto.OAuth2Provider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	private Long id;
	private String userId;
	private OAuth2Provider authorizedBy;
	private String nickname;
	private String profileImageUrl;
	private String profileThumbnailUrl;
}
