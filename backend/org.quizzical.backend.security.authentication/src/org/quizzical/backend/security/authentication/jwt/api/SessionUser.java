package org.quizzical.backend.security.authentication.jwt.api;

import org.quizzical.backend.security.authorization.api.model.user.User;

public class SessionUser {
	private Long id;
	private String email;
	private String name;
	private String pictureUrl;
	private Boolean dignosed;
	private Boolean admin;
	private Boolean qa;
	
	public SessionUser() {
	}
	
	public SessionUser(final Long id, final String email, final String name, final String pictureUrl, final Boolean admin, final Boolean qa) {
		super();
		this.id = id;
		this.email = email;
		this.name = name;
		this.pictureUrl = pictureUrl;
		this.admin = admin;
		this.qa = qa;
	}
	public SessionUser(User user) {
		this(user.getId(), user.getEmailAddress(), user.getName(), user.getPictureUrl(),user.getAdmin(),user.getQa());
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

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public Boolean getQa() {
		return qa;
	}

	public void setQa(Boolean qa) {
		this.qa = qa;
	}
}
