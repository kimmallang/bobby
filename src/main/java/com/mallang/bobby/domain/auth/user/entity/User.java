package com.mallang.bobby.domain.auth.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mallang.bobby.entity.BaseEntity;
import com.mallang.bobby.domain.auth.oauth2.dto.OAuth2Provider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name =  "user")
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "user_id")
	private String userId;

	@Enumerated(EnumType.STRING)
	@Column(name = "authorized_by")
	private OAuth2Provider authorizedBy;

	@Column(name = "nickname")
	private String nickname;

	@Column(name = "profile_image_url")
	private String profileImageUrl;

	@Column(name = "profile_thumbnail_url")
	private String profileThumbnailUrl;
}
