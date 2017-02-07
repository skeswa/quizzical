package org.quizzical.backend.analytics.api.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.gauntlet.core.model.BaseEntity;

public class TestCategoryRating extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -5340029917005503078L;

	private Integer rating = 0; //0-100
	
	private Long categoryId;
	
	private Date dateOfLastAttempt;
	
	private Integer lastAttemptRating;
	
	private Long lastAttemptTestId;
	
	private Integer lastAttemptCorrect;
	
	private Integer lastAttemptTotal;
	
	private Integer totalAttemptsCorrect;
	
	private Integer totalAttemptsTotal;
	
	private TestUserAnalytics analytics;
	
	private List<TestCategoryRatingSubmission>  ratingSubmissions;
	
	public TestCategoryRating() {
	}
	
	public TestCategoryRating(final TestUserAnalytics userAnalytics,
				final Long categoryId, final String name) {
		this(categoryId,name,null);		
	}
	
	public TestCategoryRating(final Long categoryId, final String name, final String description) {
		this.categoryId = categoryId;
		this.name = name;
		this.description = description;
	}
	
	
	public TestCategoryRating(final Integer rating, final Long lastAttemptTestId, final Date dateOfLastAttempt, final Long categoryId, final String name) {
		this(categoryId, name, null);
		this.lastAttemptTestId = lastAttemptTestId;
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

	public List<TestCategoryRatingSubmission> getRatingSubmissions() {
		if (this.ratingSubmissions == null)
			this.ratingSubmissions = new ArrayList<>();
		return ratingSubmissions;
	}

	public void setRatingSubmissions(List<TestCategoryRatingSubmission> ratingSubmissions) {
		this.ratingSubmissions = ratingSubmissions;
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
		TestCategoryRatingSubmission rs = null;
		Optional<TestCategoryRatingSubmission> rsOptional = this.getRatingSubmissions().stream()
			. filter(s -> s.getDateAttempted() == attempt.getDateAttempted())
		    .findFirst();
		if (!rsOptional.isPresent()) {
			rs = new TestCategoryRatingSubmission(attempt.getDateAttempted());
			getRatingSubmissions().add(rs);
		}
		else
			rs = rsOptional.get();
		rs.getAttempts().add(attempt);
	}

	public Long getLastAttemptTestId() {
		return lastAttemptTestId;
	}

	public void setLastAttemptTestId(Long lastAttemptTestId) {
		this.lastAttemptTestId = lastAttemptTestId;
	}

	public Integer getLastAttemptCorrect() {
		return lastAttemptCorrect;
	}

	public void setLastAttemptCorrect(Integer lastAttemptCorrect) {
		this.lastAttemptCorrect = lastAttemptCorrect;
	}

	public Integer getLastAttemptTotal() {
		return lastAttemptTotal;
	}

	public void setLastAttemptTotal(Integer lastAttemptTotal) {
		this.lastAttemptTotal = lastAttemptTotal;
	}

	public Integer getTotalAttemptsCorrect() {
		return totalAttemptsCorrect;
	}

	public void setTotalAttemptsCorrect(Integer totalAttemptsCorrect) {
		this.totalAttemptsCorrect = totalAttemptsCorrect;
	}

	public Integer getTotalAttemptsTotal() {
		return totalAttemptsTotal;
	}

	public void setTotalAttemptsTotal(Integer totalAttemptsTotal) {
		this.totalAttemptsTotal = totalAttemptsTotal;
	}
}
