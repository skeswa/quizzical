package org.gauntlet.lessons.model.jpa;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.gauntlet.core.model.Constants;
import org.gauntlet.core.model.JPABaseEntity;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name=Constants.Q7L_TABLE_NAME_PREFIX+Constants.Q7L_TABLE_NAME_SEPARATOR
	+"user_lessonplan")
public class JPAUserLessonPlan extends JPABaseEntity implements Serializable {
	private static final long serialVersionUID = 8550907688627859672L;

	private Long userId;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy="plan")
	private Set<JPAUserLesson> upcomingLessons = new java.util.HashSet<JPAUserLesson>();
	
	@OneToOne
	private JPAUserLesson currentLesson;
	

	public JPAUserLessonPlan() {
		super();
	}

	public JPAUserLessonPlan(String name, String code, Long userId) {
		this();
		this.name = name;
		this.code = code;
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Set<JPAUserLesson> getUpcomingLessons() {
		return upcomingLessons;
	}

	public void setUpcomingLessons(Set<JPAUserLesson> upcomingLessons) {
		this.upcomingLessons = upcomingLessons;
	}

	public JPAUserLesson getCurrentLesson() {
		return currentLesson;
	}

	public void setCurrentLesson(JPAUserLesson currentLesson) {
		this.currentLesson = currentLesson;
	}
}

