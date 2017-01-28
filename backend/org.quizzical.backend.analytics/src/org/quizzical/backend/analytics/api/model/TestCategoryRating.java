package org.quizzical.backend.analytics.api.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.gauntlet.core.model.BaseEntity;

public class TestCategoryRating extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -5340029917005503078L;

	private Integer rating = 0; //0-100
	
	private Long categoryId;
	
	private Date dateOfLastAttempt;
	
	private Integer lastAttemptRating;
	
	private TestUserAnalytics analytics;
	
	private List<TestCategoryAttempt>  attempts;
	
	public TestCategoryRating() {
	}
	
	public TestCategoryRating(final TestUserAnalytics userAnalytics,
				final Long categoryId, final String name, final String code) {
		this(categoryId,name,code,null);		
	}
	
	public TestCategoryRating(final Long categoryId, final String name, final String code, final String description) {
		this.categoryId = categoryId;
		this.name = name;
		this.code = code;	
		this.description = description;
	}
	
	
	public TestCategoryRating(final Integer rating, final Date dateOfLastAttempt, final Long categoryId, final String name, final String code) {
		this(categoryId, name, code, null);
	}


	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public List<TestCategoryAttempt> getAttempts() {
		if (this.attempts == null)
			this.attempts = new ArrayList<>();
		return attempts;
	}

	public void setAttempts(List<TestCategoryAttempt> attempts) {
		this.attempts = attempts;
	}
	
	
	public Date getDateOfLastAttempt() {
		return dateOfLastAttempt;
	}

	public void setDateOfLastAttempt(Date dateOfLastAttempt) {
		this.dateOfLastAttempt = dateOfLastAttempt;
	}
	

	public Integer getLastAttemptRating() {
		return lastAttemptRating;
	}

	public void setLastAttemptRating(Integer lastAttemptRating) {
		this.lastAttemptRating = lastAttemptRating;
	}

	public TestUserAnalytics getAnalytics() {
		return analytics;
	}

	public void setAnalytics(TestUserAnalytics analytics) {
		this.analytics = analytics;
	}

	public void addAttempt(TestCategoryAttempt attempt) {
		getAttempts().add(attempt);
	}

}
