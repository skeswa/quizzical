package org.quizzical.backend.analytics.api.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.gauntlet.core.model.BaseEntity;

public class TestCategoryRating extends BaseEntity implements Serializable {
	private Integer rating = 0; //0-100
	
	private Long categoryId;
	
	private Date dateOfLastAttempt;
	
	private TestUserAnalytics userAnalytics;
	
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

	public TestUserAnalytics getUserAnalytics() {
		return userAnalytics;
	}

	public void setUserAnalytics(TestUserAnalytics userAnalytics) {
		this.userAnalytics = userAnalytics;
	}

	public List<TestCategoryAttempt> getAttempts() {
		return attempts;
	}

	public void setAttempts(List<TestCategoryAttempt> attempts) {
		this.attempts = attempts;
	}
}
