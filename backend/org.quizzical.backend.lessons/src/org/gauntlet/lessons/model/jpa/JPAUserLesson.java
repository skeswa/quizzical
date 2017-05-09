package org.gauntlet.lessons.model.jpa;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.gauntlet.core.model.Constants;
import org.gauntlet.core.model.JPABaseEntity;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name=Constants.Q7L_TABLE_NAME_PREFIX+Constants.Q7L_TABLE_NAME_SEPARATOR
	+"lessonusr")
public class JPAUserLesson extends JPABaseEntity implements Serializable {
	private static final long serialVersionUID = -5512318597731923379L;

	private Long userId;
	
	private Long quizId;
	
	private Long quizSubmissionId;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateStarted;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastDateVisited;
	
	private Boolean lessonFinished = false;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateLessonFinished;
	
	private Integer quizScore;
	
	private Integer skippedProblems;
	
	private Integer totalProblems;
	
	@ManyToOne(targetEntity = JPALessonType.class)
	@JoinColumn
	private JPALessonType lessonType;
	
	@ManyToOne(targetEntity = JPALessonStatus.class)
	@JoinColumn
	private JPALessonStatus lessonStatus;	
	
	@ManyToOne(targetEntity = JPALesson.class)
	private JPALesson lesson;
	
	@ManyToOne(targetEntity = JPAUserLessonPlan.class)
	@JoinColumn
	private JPAUserLessonPlan plan;	

	public JPAUserLesson() {
		super();
	}

	public JPAUserLesson(String name, String code, Long userId, Long quizId) {
		this();
		this.name = name;
		this.code = code;
		this.userId = userId;
		this.quizId = quizId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getQuizId() {
		return quizId;
	}

	public void setQuizId(Long quizId) {
		this.quizId = quizId;
	}

	public JPALesson getLesson() {
		return lesson;
	}

	public void setLesson(JPALesson lesson) {
		this.lesson = lesson;
	}

	public JPAUserLessonPlan getPlan() {
		return plan;
	}

	public void setPlan(JPAUserLessonPlan plan) {
		this.plan = plan;
	}

	public JPALessonType getLessonType() {
		return lessonType;
	}

	public void setLessonType(JPALessonType lessonType) {
		this.lessonType = lessonType;
	}

	public JPALessonStatus getLessonStatus() {
		return lessonStatus;
	}

	public void setLessonStatus(JPALessonStatus lessonStatus) {
		this.lessonStatus = lessonStatus;
	}

	public Date getDateStarted() {
		return dateStarted;
	}

	public void setDateStarted(Date dateStarted) {
		this.dateStarted = dateStarted;
	}

	public Date getLastDateVisited() {
		return lastDateVisited;
	}

	public void setLastDateVisited(Date lastDateVisited) {
		this.lastDateVisited = lastDateVisited;
	}

	public Boolean getLessonFinished() {
		return lessonFinished;
	}

	public void setLessonFinished(Boolean lessonFinished) {
		this.lessonFinished = lessonFinished;
	}

	public Long getQuizSubmissionId() {
		return quizSubmissionId;
	}

	public void setQuizSubmissionId(Long quizSubmissionId) {
		this.quizSubmissionId = quizSubmissionId;
	}

	public Date getDateLessonFinished() {
		return dateLessonFinished;
	}

	public void setDateLessonFinished(Date dateLessonFinished) {
		this.dateLessonFinished = dateLessonFinished;
	}

	public Integer getQuizScore() {
		return quizScore;
	}

	public void setQuizScore(Integer quizScore) {
		this.quizScore = quizScore;
	}

	public Integer getSkippedProblems() {
		return skippedProblems;
	}

	public void setSkippedProblems(Integer skippedProblems) {
		this.skippedProblems = skippedProblems;
	}

	public Integer getTotalProblems() {
		return totalProblems;
	}

	public void setTotalProblems(Integer totalProblems) {
		this.totalProblems = totalProblems;
	}
}

