package com.mallang.bobby.domain.user.dto;

import com.mallang.bobby.oauth2.dto.OAuth2Provider;
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
	private Long userId;
	private OAuth2Provider authorizedBy;
	private String nickname;
	private String profileImageUrl;
	private String profileThumbnailUrl;
}
