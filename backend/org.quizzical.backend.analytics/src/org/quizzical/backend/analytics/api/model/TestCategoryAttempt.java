package org.quizzical.backend.analytics.api.model;

import java.io.Serializable;
import java.util.Date;

import org.gauntlet.core.model.BaseEntity;

public class TestCategoryAttempt extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -400437286101443018L;
	
	private Long testId;
	private Long testProblemId;
	private Date dateAttempted;
	private Boolean successful;
	private Boolean skipped;

	private Integer secondsElapsed;
	
	public Long getTestProblemResponseId() {
		return testProblemId;
	}
	public void setTestProblemResponseId(Long testProblemResponseId) {
		this.testProblemId = testProblemResponseId;
	}
	public Date getDateAttempted() {
		return dateAttempted;
	}
	public void setDateAttempted(Date dateAttempted) {
		this.dateAttempted = dateAttempted;
	}
	public Boolean getSuccessful() {
		return successful;
	}
	public void setSuccessful(Boolean successful) {
		this.successful = successful;
	}
	public Boolean getSkipped() {
		return skipped;
	}
	public void setSkipped(Boolean skipped) {
		this.skipped = skipped;
	}
	
	public Long getTestId() {
		return testId;
	}
	public void setTestId(Long testId) {
		this.testId = testId;
	}
	public Long getTestProblemId() {
		return testProblemId;
	}
	public void setTestProblemId(Long testProblemId) {
		this.testProblemId = testProblemId;
	}
	
	public Integer getSecondsElapsed() {
		return secondsElapsed;
	}
	public void setSecondsElapsed(Integer secondsElapsed) {
		this.secondsElapsed = secondsElapsed;
	}
	public TestCategoryAttempt() {
	}

	public TestCategoryAttempt(Long testId, Long testProblemId, Date dateAttempted, Boolean successful, Boolean skipped, Integer secondsElapsed) {
		super();
		this.testProblemId = testProblemId;
		this.dateAttempted = dateAttempted;
		this.successful = successful;
		this.skipped = skipped;
		this.testId = testId;
		this.secondsElapsed = secondsElapsed;
	}
}
