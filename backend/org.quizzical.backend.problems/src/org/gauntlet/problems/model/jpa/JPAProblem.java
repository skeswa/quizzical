package org.gauntlet.problems.model.jpa;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.gauntlet.core.model.Constants;
import org.gauntlet.core.model.JPABaseEntity;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name=Constants.Q7L_TABLE_NAME_PREFIX+Constants.Q7L_TABLE_NAME_SEPARATOR
	+"problem")
public class JPAProblem extends JPABaseEntity implements Serializable {
	private static final long serialVersionUID = -8154233623279419488L;

	private String answer;
	
	private Boolean answerInRange;
	
	@ManyToOne(targetEntity = JPAProblemSource.class)
	@JoinColumn
	private JPAProblemSource source;
	
	@ManyToOne(targetEntity = JPAProblemCategory.class, fetch=FetchType.LAZY)
	@JoinColumn
	private JPAProblemCategory category;
	
	@ManyToOne(targetEntity = JPAProblemDifficulty.class)
	@JoinColumn	
	private JPAProblemDifficulty difficulty;	
	
	@OneToOne(targetEntity = JPAProblemPicture.class, cascade=CascadeType.ALL)
	@JoinColumn
	private JPAProblemPicture answerPicture;
	
	@OneToOne(targetEntity = JPAProblemPicture.class, cascade=CascadeType.ALL)
	@JoinColumn
	private JPAProblemPicture questionPicture;
	
	private Integer sourcePageNumber;
	
	private Integer sourceIndexWithinPage;

	private boolean multipleChoice = true;
	
	private Boolean requiresCalculator;
	
	private Boolean requiresFixing;
	
	private Boolean qaEd = false;
	
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public JPAProblemSource getSource() {
		return source;
	}

	public void setSource(JPAProblemSource source) {
		this.source = source;
	}

	public JPAProblemCategory getCategory() {
		return category;
	}

	public void setCategory(JPAProblemCategory category) {
		this.category = category;
	}

	public JPAProblemDifficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(JPAProblemDifficulty difficulty) {
		this.difficulty = difficulty;
	}

	public JPAProblemPicture getAnswerPicture() {
		return answerPicture;
	}

	public void setAnswerPicture(JPAProblemPicture answerPicture) {
		this.answerPicture = answerPicture;
	}

	public JPAProblemPicture getQuestionPicture() {
		return questionPicture;
	}

	public void setQuestionPicture(JPAProblemPicture questionPicture) {
		this.questionPicture = questionPicture;
	}

	public Integer getSourcePageNumber() {
		return sourcePageNumber;
	}

	public void setSourcePageNumber(Integer sourcePageNumber) {
		this.sourcePageNumber = sourcePageNumber;
	}

	public Integer getSourceIndexWithinPage() {
		return sourceIndexWithinPage;
	}

	public void setSourceIndexWithinPage(Integer sourceIndexWithinPage) {
		this.sourceIndexWithinPage = sourceIndexWithinPage;
	}

	public boolean getMultipleChoice() {
		return multipleChoice;
	}

	public void setMultipleChoice(boolean multipleChoice) {
		this.multipleChoice = multipleChoice;
	}

	public Boolean getRequiresCalculator() {
		return requiresCalculator;
	}

	public void setRequiresCalculator(Boolean requiresCalculator) {
		this.requiresCalculator = requiresCalculator;
	}

	public Boolean getRequiresFixing() {
		return requiresFixing;
	}

	public void setRequiresFixing(Boolean requiresFixing) {
		this.requiresFixing = requiresFixing;
	}

	public Boolean getAnswerInRange() {
		return answerInRange;
	}

	public void setAnswerInRange(Boolean answerInRange) {
		this.answerInRange = answerInRange;
	}

	public Boolean getQaEd() {
		return qaEd;
	}

	public void setQaEd(Boolean qaEd) {
		this.qaEd = qaEd;
	} 
}
