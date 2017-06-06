package org.gauntlet.quizzes.api.model;

import java.io.Serializable;

import org.gauntlet.core.model.BaseEntity;
import org.gauntlet.problems.api.model.Problem;

public class QuizProblem extends BaseEntity implements Serializable {
	
	private Long problemId;
	
	private Long categoryId;
	
	private Long difficultyId;
	
	private Integer quizOrdinal;
	
	private Integer sectionOrdinal;
	
	private Integer ordinal;
	
	private Problem problem;
	
	private Quiz quiz;
	
	private QuizProblemType type = QuizProblemType.REGULAR;
	
	public QuizProblem() {
	}
	
	public QuizProblem(final String name, final String code, final Integer ordinal) {
		this.name = name;
		this.code = code;
		this.ordinal = ordinal;
	}
	
	public QuizProblem(
			final String name,
			final String code,
			final Integer ordinal,
			final Long problemId,
			final Long categoryId) {
		this(name, code, ordinal);
		this.problemId = problemId;
		this.categoryId = categoryId;
	}
	
	public QuizProblem(
			final String name,
			final String code,
			final Integer ordinal,
			final Long problemId,
			final Long categoryId,
			final Long difficultyId) {
		this(name, code, ordinal);
		this.problemId = problemId;
		this.categoryId = categoryId;
		this.difficultyId = difficultyId;
	}
	
	public QuizProblem(
			final String name,
			final String code,
			final Integer ordinal,
			final Long problemId,
			final Long categoryId,
			final Problem problem) {
		this(name, code, ordinal, problemId, categoryId);
		this.problem = problem;
	}
	
	public QuizProblem(
			final String quizCode,
			final Integer quizOrdinal,
			final Integer sectionOrdinal,
			final Integer ordinal,
			final Long problemId,
			final Problem problem) {
		this.ordinal = ordinal;
		this.problemId = problemId;
		this.categoryId = problem.getCategory().getId();
		this.difficultyId = problem.getDifficulty().getId();
		this.problem = problem;
		this.quizOrdinal = quizOrdinal;
		this.sectionOrdinal = sectionOrdinal;
		updateCode(quizCode);
	}	
	
	public void updateCode(String quizCode) {
		String code = String.format("%s-%s-O%d", quizCode, problem.getCode(), quizOrdinal);
		setCode(code);
		setName(code);
	}

	public Integer getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
	}

	public Long getProblemId() {
		return problemId;
	}

	public void setProblemId(Long problemId) {
		this.problemId = problemId;
	}

	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
		if (this.problem != null) {
			this.problemId = problem.getId();
			this.categoryId = problem.getCategory().getId();
			this.difficultyId = problem.getDifficulty().getId();
			this.problem = problem;
		}
	}

	public Quiz getQuiz() {
		return quiz;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}

	public Integer getSectionOrdinal() {
		return sectionOrdinal;
	}

	public void setSectionOrdinal(Integer sectionOrdinal) {
		this.sectionOrdinal = sectionOrdinal;
	}

	public QuizProblemType getType() {
		return type;
	}

	public void setType(QuizProblemType type) {
		this.type = type;
	}

	public Integer getQuizOrdinal() {
		return quizOrdinal;
	}

	public void setQuizOrdinal(Integer quizOrdinal) {
		this.quizOrdinal = quizOrdinal;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	
	public Long getDifficultyId() {
		return difficultyId;
	}

	public void setDifficultyId(Long difficultyId) {
		this.difficultyId = difficultyId;
	}	
}
