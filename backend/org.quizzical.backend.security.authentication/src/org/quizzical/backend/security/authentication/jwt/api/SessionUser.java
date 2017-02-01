package org.quizzical.backend.security.authentication.jwt.api;

import org.quizzical.backend.security.authorization.api.model.user.User;

public class SessionUser {
	private Long id;
	private String email;
	private String name;
	private String pictureUrl;
	private Boolean dignosed;
	
	public SessionUser() {
	}
	
	public SessionUser(final Long id, final String email, final String name, final String pictureUrl) {
		super();
		this.id = id;
		this.email = email;
		this.name = name;
		this.pictureUrl = pictureUrl;
	}
	public SessionUser(User user) {
		this(user.getId(), user.getEmailAddress(), user.getName(), user.getPictureUrl());
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		name = name;
	}
	public String getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public Boolean getDignosed() {
		return dignosed;
	}

	public void setDignosed(Boolean dignosed) {
		this.dignosed = dignosed;
	}
}
