package org.quizzical.backend.analytics.api.model;

import java.io.Serializable;
import java.util.Date;

import org.gauntlet.core.model.BaseEntity;

public class TestCategoryAttempt extends BaseEntity implements Serializable {
	private Long testProblemId;
	private Date dateAttempted;
	private Boolean successful;
	private Boolean skipped;
	
	public Long getTestProblemId() {
		return testProblemId;
	}
	public void setTestProblemId(Long testProblemId) {
		this.testProblemId = testProblemId;
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
	public TestCategoryAttempt(Long testProblemId, Date dateAttempted, Boolean successful, Boolean skipped) {
		super();
		this.testProblemId = testProblemId;
		this.dateAttempted = dateAttempted;
		this.successful = successful;
		this.skipped = skipped;
	}
}
