package com.mallang.bobby.oauth2.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AccessTokenResponse {
    private String tokenType;
	private String accessToken;
	private Integer expiresIn;
	private String refreshToken;
	private Integer refreshTokenExpiresIn;
	private String scope;
}
