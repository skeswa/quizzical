package org.gauntlet.lessons.api.dao;

import java.util.List;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.core.api.service.IBaseService;
import org.gauntlet.lessons.api.model.Lesson;
import org.gauntlet.lessons.api.model.UserLesson;
import org.gauntlet.lessons.api.model.UserLessonPlan;
import org.gauntlet.quizzes.api.model.QuizSubmission;
import org.gauntlet.quizzes.api.model.QuizType;
import org.gauntlet.lessons.api.model.LessonProblem;
import org.gauntlet.lessons.api.model.LessonStatus;
import org.gauntlet.lessons.api.model.LessonType;
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
	
	public UserLesson deleteUserLesson(Long id) throws ApplicationException, NoSuchModelException;
	
	public UserLesson provideUserLesson(UserLesson record) throws ApplicationException;
	
	public UserLesson updateUserLesson(UserLesson record) throws ApplicationException;
	
	public UserLesson getUserLessonByPrimary(Long pk) throws ApplicationException, NoSuchModelException;

	public UserLesson getUserLessonByCode(String code) throws ApplicationException;

	public UserLesson getUserLessonByName(String name) throws ApplicationException;
	
	public UserLesson findUserLessonByType(User user, Long typeId) throws ApplicationException;
	
	public List<UserLesson> findAllUserLessonsByStatus(User user, Long statusId) throws ApplicationException;
	
	public List<UserLesson> findAllUserLessonsByType(User user, Long typeId) throws ApplicationException;

	public UserLesson findUserLessonByQuizId(Long quizId) throws ApplicationException;
	
	public void markUserLessonAsComplete(UserLesson ul, QuizSubmission qs) throws ApplicationException;

	public void pickNextUserLesson(Long userId) throws ApplicationException;

	//LesssonProblem
	public LessonProblem provideLessonProblem(LessonProblem record) throws ApplicationException;
	
	public LessonProblem deleteLessonProblem(Long id) throws ApplicationException, NoSuchModelException;
	
	//LessonType
	public List<LessonType> findAllLessonTypes(int start, int end) throws ApplicationException;
	
	public long countAllLessonTypes() throws ApplicationException;
	
	public int countByLessonType(Long typeId) throws ApplicationException;
	
	public int countByLessonTypeCode(User user, String typeCode) throws ApplicationException;
	
	public List<UserLesson>  findAllUserLessonsByLessonType(User user, Long typeId) throws ApplicationException;
	
	public LessonType getLessonTypeByPrimary(Long pk)  throws ApplicationException, NoSuchModelException;
	
	public LessonType provideLessonType(LessonType record) throws ApplicationException;
	
	public LessonType getLessonTypeByCode(String code) throws ApplicationException;
	
	public LessonType getLessonTypeByName(String name) throws ApplicationException;
	
	public LessonType deleteLessonType(Long id) throws ApplicationException, NoSuchModelException;
	
	//LessonStatus
	public List<LessonStatus> findAllLessonStatuses(int start, int end) throws ApplicationException;
	
	public long countAllLessonStatuses() throws ApplicationException;
	
	public int countByLessonStatusCode(User user, String statusCode) throws ApplicationException;
	
	public int countByLessonStatus(Long statusId) throws ApplicationException;
	
	public List<UserLesson>  findAllUserLessonsByLessonStatus(User user, Long statusId) throws ApplicationException;
	
	public LessonStatus getLessonStatusByPrimary(Long pk)  throws ApplicationException, NoSuchModelException;
	
	public LessonStatus provideLessonStatus(LessonStatus record) throws ApplicationException;
	
	public LessonStatus getLessonStatusByCode(String code) throws ApplicationException;
	
	public LessonStatus getLessonStatusByName(String name) throws ApplicationException;
	
	public LessonStatus deleteLessonStatus(Long id) throws ApplicationException, NoSuchModelException;	
	
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
