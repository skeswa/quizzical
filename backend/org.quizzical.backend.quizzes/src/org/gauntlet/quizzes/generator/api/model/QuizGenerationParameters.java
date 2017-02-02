package org.gauntlet.quizzes.generator.api.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.gauntlet.core.model.BaseEntity;

public class QuizGenerationParameters extends BaseEntity implements Serializable {
	
	private String generatorType;
	
	private Long problemSourceId;
	
	private Long problemCategoryId;
	
	private Integer quizSize;
	
	public QuizGenerationParameters() {
	}

	public Long getProblemCategoryId() {
		return problemCategoryId;
	}

	public void setProblemCategoryId(Long problemCategoryId) {
		this.problemCategoryId = problemCategoryId;
	}

	public Long getProblemSourceId() {
		return problemSourceId;
	}

	public void setProblemSourceId(Long problemSourceId) {
		this.problemSourceId = problemSourceId;
	}

	public Integer getQuizSize() {
		return quizSize;
	}

	public void setQuizSize(Integer quizSize) {
		this.quizSize = quizSize;
	}

	public String getGeneratorType() {
		return generatorType;
	}

	public void setGeneratorType(String generatorType) {
		this.generatorType = generatorType;
	}
}


