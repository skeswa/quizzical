package org.gauntlet.lessons.api.dao;

import java.util.List;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.core.api.service.IBaseService;
import org.gauntlet.lessons.api.model.Lesson;
import org.gauntlet.lessons.api.model.UserLesson;
import org.gauntlet.lessons.api.model.UserLessonPlan;
import org.gauntlet.lessons.api.model.LessonProblem;
import org.quizzical.backend.security.authorization.api.model.user.User;

public interface ILessonsDAOService extends IBaseService {
	// Lessons
	public List<Lesson> findAll() throws ApplicationException;
	
	public List<Lesson> findAll(int start, int end) throws ApplicationException;
	
	public long countAll() throws ApplicationException;
	
	public Lesson provide(Lesson record) throws ApplicationException;
	
	public Lesson update(Lesson record) throws ApplicationException;
	
	public Lesson delete(Long id) throws ApplicationException, NoSuchModelException;
	
	public Lesson getByPrimary(Long pk) throws ApplicationException, NoSuchModelException;

	public Lesson getByCode(String code) throws ApplicationException;

	public Lesson getByName(String name) throws ApplicationException;
	
	// UserLessons
	public List<UserLesson> findAllUserLessons(User user) throws ApplicationException;
	
	public long countAllUserLessons(User user) throws ApplicationException;
	
	public UserLesson provideUserLesson(UserLesson record) throws ApplicationException;
	
	public UserLesson updateUserLesson(UserLesson record) throws ApplicationException;
	
	public UserLesson getUserLessonByPrimary(Long pk) throws ApplicationException, NoSuchModelException;

	public UserLesson getUserLessonByCode(String code) throws ApplicationException;

	public UserLesson getUserLessonByName(String name) throws ApplicationException;


	//LesssonProblem
	public LessonProblem provideLessonProblem(LessonProblem record) throws ApplicationException;
	
	public LessonProblem deleteLessonProblem(Long id) throws ApplicationException, NoSuchModelException;
	
	// UserLessonPlans
	public List<UserLessonPlan> findAllUserLessonPlans(User user) throws ApplicationException;
	
	public long countAllUserLessonPlans(User user) throws ApplicationException;
	
	public UserLessonPlan provideUserLessonPlan(UserLessonPlan record) throws ApplicationException;
	
	public UserLessonPlan updateUserLessonPlan(UserLessonPlan record) throws ApplicationException;
	
	public UserLessonPlan getUserLessonPlanByPrimary(Long pk) throws ApplicationException, NoSuchModelException;

	public UserLessonPlan getUserLessonPlanByCode(String code) throws ApplicationException;

	public UserLessonPlan getUserLessonPlanByName(String name) throws ApplicationException;

	public List<UserLessonPlan> findAllUserLessonPlans() throws ApplicationException;

	public UserLessonPlan getUserLessonPlanByUserPk(Long userPk) throws ApplicationException;

	public UserLesson provideLessonAsCurrentToPlan(UserLesson userLesson, Long planPk) throws ApplicationException, NoSuchModelException;
	
	public UserLesson provideLessonAsUpcomingToPlan(UserLesson userLesson, Long planPk) throws ApplicationException, NoSuchModelException;

	public void resetUserLessonPlan(User user) throws ApplicationException, NoSuchModelException;
}
