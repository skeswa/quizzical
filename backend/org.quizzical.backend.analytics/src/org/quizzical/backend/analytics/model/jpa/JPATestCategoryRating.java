package org.quizzical.backend.analytics.model.jpa;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.math.fraction.Fraction;
import org.gauntlet.core.model.Constants;
import org.gauntlet.core.model.JPABaseEntity;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name=Constants.Q7L_TABLE_NAME_PREFIX+Constants.Q7L_TABLE_NAME_SEPARATOR
	+org.quizzical.backend.analytics.api.model.Constants.Q7L_MODULE
	+Constants.Q7L_TABLE_NAME_SEPARATOR
	+"cat_rating")
public class JPATestCategoryRating extends JPABaseEntity implements Serializable {
	private static final long serialVersionUID = -5148669857283031803L;

	private Integer rating = 0; //0-100
	
	private Long categoryId;
	
	private Date dateOfLastAttempt;
	
	private Long lastAttemptTestId;
	
	private Integer lastAttemptRating;
	
	private Integer lastAttemptCorrect;
	
	private Integer lastAttemptTotal;
	
	private Integer totalAttemptsCorrect;
	
	private Integer totalAttemptsTotal;

	@ManyToOne
	private JPATestUserAnalytics analytics;
	
	@OneToMany(targetEntity = JPATestCategoryRatingSubmission.class, fetch=FetchType.EAGER, cascade=CascadeType.ALL, mappedBy="rating")
	private Set<JPATestCategoryRatingSubmission>  ratingSubmissions = new HashSet<JPATestCategoryRatingSubmission>();
	
	public JPATestCategoryRating() {
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

	public JPATestUserAnalytics getAnalytics() {
		return analytics;
	}

	public void setAnalytics(JPATestUserAnalytics analytics) {
		this.analytics = analytics;
	}

	public Set<JPATestCategoryRatingSubmission> getSubmissions() {
		return ratingSubmissions;
	}
	
	public Set<JPATestCategoryRatingSubmission> addSubmission(JPATestCategoryRatingSubmission submission) {
		System.out.print(String.format("Adding %d th submission to rating %s", getSubmissions().size()+1,getName()));
		getSubmissions().add(submission);
		return getSubmissions();
	}

	public void setSubmissions(Set<JPATestCategoryRatingSubmission> ratingSubmissions) {
		this.ratingSubmissions = ratingSubmissions;
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
	
	public Integer calculateScore() {
		//Total avg rating
		final int sumOfScores = getSubmissions().stream().mapToInt(s -> {
			Integer sscore = s.calculateScore();
			System.out.println(String.format("Calculated submission %s with score %d",s.getDateAttempted().toString(),sscore));
			return sscore;
		}).sum();
		int avrgScore = (int)(new Fraction(sumOfScores,getSubmissions().size()).doubleValue());
		setRating(avrgScore);
		
		//Total correct attempts
		final int sumOfCorrectAttempts = getSubmissions().stream().mapToInt(s -> {
			return s.getCorrectCount();
		}).sum();
		setTotalAttemptsCorrect(sumOfCorrectAttempts);
		
		//Last submission stats
		Date dateOfLastAttempt = getSubmissions().stream().map(s -> s.getDateAttempted()).max(Date::compareTo).get();
		setDateOfLastAttempt(dateOfLastAttempt);
		
		final JPATestCategoryRatingSubmission  recentSubmmission = getSubmissions()
			    .stream()
			    .filter(p -> p.getDateAttempted() == dateOfLastAttempt)
			    .findFirst().get();
		setLastAttemptTestId(recentSubmmission.getTestId());
		
		setLastAttemptCorrect(recentSubmmission.getCorrectCount());
		setLastAttemptTotal(recentSubmmission.getAttempts().size());
		setLastAttemptRating(recentSubmmission.getSubmissionScore());
		
		return getRating();
	}
}

