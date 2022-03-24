package com.mallang.bobby.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Setter;

@Setter
@MappedSuperclass
public abstract class BaseEntity {

	@Column(name = "created_at", updatable = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	protected LocalDateTime createdAt;

	@Column(name = "modified_at")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	protected LocalDateTime modifiedAt;

	@PrePersist
	void created() {
		this.createdAt = LocalDateTime.now();
	}

	@PreUpdate
	void updated() {
		this.modifiedAt = LocalDateTime.now();
	}
}