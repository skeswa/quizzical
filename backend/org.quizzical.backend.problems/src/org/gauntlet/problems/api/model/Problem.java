package org.gauntlet.problems.api.model;

import java.io.Serializable;

import org.gauntlet.core.model.BaseEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Problem extends BaseEntity implements Serializable {
	private String answer;
	
	private ProblemSource source;
	
	private ProblemCategory category;
	
	private Integer sourcePageNumber;
	
	private Integer sourceIndexWithinPage;

	private ProblemDifficulty difficulty;

	private ProblemPicture answerPicture;
	
	private ProblemPicture questionPicture;
	
	private boolean multipleChoice = true;
	
	private Boolean requiresCalculator;
	
	public Problem() {
		super.setCode(Long.toString(System.currentTimeMillis()));
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public ProblemSource getSource() {
		return source;
	}

	public void setSource(ProblemSource source) {
		this.source = source;
	}

	public ProblemCategory getCategory() {
		return category;
	}

	public void setCategory(ProblemCategory category) {
		this.category = category;
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

	public ProblemDifficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(ProblemDifficulty difficulty) {
		this.difficulty = difficulty;
	}

	public ProblemPicture getAnswerPicture() {
		return answerPicture;
	}

	public void setAnswerPicture(ProblemPicture answerPicture) {
		this.answerPicture = answerPicture;
	}

	public ProblemPicture getQuestionPicture() {
		return questionPicture;
	}

	public void setQuestionPicture(ProblemPicture questionPicture) {
		this.questionPicture = questionPicture;
	}

	public boolean isMultipleChoice() {
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

	public Problem(String answer, ProblemSource source, ProblemCategory category, Integer sourcePageNumber,
			Integer sourceIndexWithinPage, ProblemDifficulty difficulty, ProblemPicture answerPicture, ProblemPicture questionPicture,
			boolean multipleChoice, Boolean requiresCalculator) {
		this();
		this.answer = answer;
		this.source = source;
		this.category = category;
		this.sourcePageNumber = sourcePageNumber;
		this.sourceIndexWithinPage = sourceIndexWithinPage;
		this.difficulty = difficulty;
		this.answerPicture = answerPicture;
		this.questionPicture = questionPicture;
		this.multipleChoice = multipleChoice;
		this.requiresCalculator = requiresCalculator;
		
		this.code = source.getCode()+"_"+sourcePageNumber+"_"+sourceIndexWithinPage+"_"+(this.requiresCalculator == null?"N/A":this.requiresCalculator);
	} 
	
	@Override
	public String toString() {
		return this.code;
	}
}
