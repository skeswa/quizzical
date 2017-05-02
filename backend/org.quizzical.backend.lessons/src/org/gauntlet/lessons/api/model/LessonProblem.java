package org.gauntlet.lessons.api.model;

import java.io.Serializable;

import org.gauntlet.core.model.BaseEntity;
import org.gauntlet.problems.api.model.Problem;

public class LessonProblem extends BaseEntity implements Serializable {
	
	private Long problemId;
	
	private Integer ordinal;
	
	private Problem problem;
	
	private Lesson lesson;
	
	
	public LessonProblem() {
	}
	
	public LessonProblem(final String name, final String code, final Integer ordinal) {
		this.name = name;
		this.code = code;
		this.ordinal = ordinal;
	}
	
	public LessonProblem(
			final String name,
			final String code,
			final Integer ordinal,
			final Long problemId) {
		this(name, code, ordinal);
		this.problemId = problemId;
	}
	
	public LessonProblem(
			final String name,
			final String code,
			final Integer ordinal,
			final Long problemId,
			final Problem problem) {
		this(name, code, ordinal, problemId);
		this.problem = problem;
	}
	
	public LessonProblem(
			final String lessonCode,
			final Integer ordinal,
			final Long problemId,
			final Problem problem) {
		this(null, null, ordinal, problemId);
		this.problem = problem;
		String code = String.format("%s-%s-O%d", lessonCode, problem.getCode(), ordinal);
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
	}
}
