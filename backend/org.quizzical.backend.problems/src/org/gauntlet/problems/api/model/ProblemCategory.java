package org.gauntlet.problems.api.model;

import java.io.Serializable;
import java.util.Set;

import org.gauntlet.core.model.BaseEntity;

public class ProblemCategory extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -6636030769667528455L;

	private Set<ProblemCategoryLesson> lessons = new java.util.HashSet<ProblemCategoryLesson>();

	public ProblemCategory() {
	}
	
	public ProblemCategory(String name, String code) {
		setName(name);
		setCode(code);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (lessons != null) {
			for (ProblemCategoryLesson l : lessons) {
				sb.append(String.format("{%s=%d;%s=%s},", "id", l.getId(),"name", l.getCode()));
			}
		}
		return String.format("{\n\t%s=%d;\n\t%s=%s;\n\tlessons:[%s]\n}", "id",getId(),"name",getName(),sb.toString());
	}

	public Set<ProblemCategoryLesson> getLessons() {
		return lessons;
	}

	public void setLessons(Set<ProblemCategoryLesson> lessons) {
		this.lessons = lessons;
	}
}
