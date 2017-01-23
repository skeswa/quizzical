package org.quizzical.backend.analytics.api.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.core.model.BaseEntity;

public class TestCategoryRating extends BaseEntity implements Serializable {
	private Integer rating = 0; //0-100
	
	private Long categoryId;
	
	private Date dateOfLastAttempt;
	
	private TestUserAnalytics analytics;
	
	private List<TestCategoryAttempt>  attempts;
	
	public TestCategoryRating() {
	}
	
	public TestCategoryRating(final TestUserAnalytics userAnalytics,
				final Long categoryId, final String name, final String code) {
		this(categoryId,name,code);		
	}
	
	public TestCategoryRating(final Long categoryId, final String name, final String code) {
		this.categoryId = categoryId;
		this.name = name;
		this.code = code;		
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

	public TestUserAnalytics getAnalytics() {
		return analytics;
	}

	public void setAnalytics(TestUserAnalytics analytics) {
		this.analytics = analytics;
	}

	public void addAttempt(TestCategoryAttempt attempt) {
		getAttempts().add(attempt);
	}

	public void calculateRating() {
		final List<TestCategoryAttempt> correctAttempts = getAttempts()
			    .stream()
			    .filter(p -> p.getSuccessful())
			    .collect(Collectors.toList());
		setRating((int)((correctAttempts.size()/getAttempts().size())*100));
	}
}
