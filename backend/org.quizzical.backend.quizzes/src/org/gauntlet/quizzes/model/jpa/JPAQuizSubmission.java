package org.gauntlet.quizzes.model.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.gauntlet.core.model.Constants;
import org.gauntlet.core.model.JPABaseEntity;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name=Constants.CNX_TABLE_NAME_PREFIX+Constants.GNT_TABLE_NAME_SEPARATOR
	+"quiz_submission")
public class JPAQuizSubmission extends JPABaseEntity implements Serializable {
	@ManyToOne(targetEntity = JPAQuiz.class)
	@JoinColumn
	private JPAQuiz quiz;
	
	@OneToMany(targetEntity = JPAQuizProblemAnswer.class)
	@JoinColumn
	private List<JPAQuizProblemAnswer>	answers = new ArrayList<>();

	public JPAQuizSubmission() {
		super();
	}

	public JPAQuizSubmission(JPAQuiz quiz) {
		this();
		String code_ = String.format("Take %d of %s",new Date().toString(),quiz.getCode());
		setCode(code_);
		setName(code_);
	}

	public JPAQuiz getQuiz() {
		return quiz;
	}

	public void setQuiz(JPAQuiz quiz) {
		this.quiz = quiz;
	}

	public List<JPAQuizProblemAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<JPAQuizProblemAnswer> answers) {
		this.answers = answers;
	}
	
	public void addAnswer(JPAQuizProblemAnswer answer) {
		getAnswers().add(answer);
	}
}

