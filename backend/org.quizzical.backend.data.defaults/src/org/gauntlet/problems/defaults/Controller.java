package org.gauntlet.problems.defaults;

import org.gauntlet.lessons.api.dao.ILessonsDAOService;
import org.gauntlet.lessons.api.model.LessonStatus;
import org.gauntlet.lessons.api.model.LessonType;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.quizzes.api.dao.IQuizDAOService;
import org.gauntlet.quizzes.api.model.Constants;
import org.gauntlet.quizzes.api.model.QuizType;
import org.osgi.service.log.LogService;
import org.quizzical.backend.security.authorization.api.dao.user.IUserDAOService;
import org.quizzical.backend.security.authorization.api.model.user.User;
import org.quizzical.backend.testdesign.api.dao.ITestDesignTemplateContentTypeDAOService;
import org.quizzical.backend.testdesign.api.dao.ITestDesignTemplateDAOService;


public class Controller {
	private volatile LogService logger;
	private volatile IProblemDAOService problemService;
	private volatile IQuizDAOService quizService;
	private volatile IUserDAOService userService;
	private volatile ILessonsDAOService lessonService;
	
	private volatile ITestDesignTemplateDAOService testDesignService;
	private volatile ITestDesignTemplateContentTypeDAOService contentTypeService;
	
	
	private void start() throws Exception {
		/**
		 * 
		 * Types
		 * 
		 */
		new TestDesignTemplateGenerator(testDesignService, contentTypeService).generate();
		
		
		/**
		 * 
		 * Misc
		 * 
		 */
		//-- Lesson related
		LessonType lt = new LessonType(
					org.gauntlet.lessons.api.model.Constants.LESSON_TYPE_CURRENT, 
					org.gauntlet.lessons.api.model.Constants.LESSON_TYPE_CURRENT);
		lessonService.provideLessonType(lt);

		lt = new LessonType(
				org.gauntlet.lessons.api.model.Constants.LESSON_TYPE_SCHEDULED, 
				org.gauntlet.lessons.api.model.Constants.LESSON_TYPE_SCHEDULED);
		lessonService.provideLessonType(lt);
		
		LessonStatus ls = new LessonStatus(
				org.gauntlet.lessons.api.model.Constants.LESSON_STATUS_NEW, 
				org.gauntlet.lessons.api.model.Constants.LESSON_STATUS_NEW);
		lessonService.provideLessonStatus(ls);
	
		ls = new LessonStatus(
				org.gauntlet.lessons.api.model.Constants.LESSON_STATUS_STARTED, 
				org.gauntlet.lessons.api.model.Constants.LESSON_STATUS_STARTED);
		lessonService.provideLessonStatus(ls);
		
		ls = new LessonStatus(
				org.gauntlet.lessons.api.model.Constants.LESSON_STATUS_FINISHED, 
				org.gauntlet.lessons.api.model.Constants.LESSON_STATUS_FINISHED);
		lessonService.provideLessonStatus(ls);
		
		//-- Quiz related
		QuizType qt = new QuizType(Constants.QUIZ_TYPE_GENERATED_CODE, Constants.QUIZ_TYPE_GENERATED_NAME);
		quizService.provideQuizType(qt);

		qt = new QuizType(Constants.QUIZ_TYPE_DIAGNOSTIC_CODE, Constants.QUIZ_TYPE_DIAGNOSTIC_NAME);
		quizService.provideQuizType(qt);
		//-- Users
		//MK
		User user = new User();
		user.setCode("mk");
		user.setFirstName("Mandi");
		user.setEmailAddress("mandisakeswa999@gmail.com");
		
		user.setPasswordEncrypted(true);
		user.setPassword("aceit");
		
		userService.provide(user);
		
		//Admin
		user = new User();
		user.setCode(org.quizzical.backend.security.authorization.api.Constants.ADMIN_USER_CODE);
		user.setFirstName(org.quizzical.backend.security.authorization.api.Constants.ADMIN_USER_NAME);
		user.setEmailAddress(org.quizzical.backend.security.authorization.api.Constants.ADMIN_USER_CODE);
		
		user.setAdmin(true);
		user.setPasswordEncrypted(true);
		user.setPassword("q7ladmin");
		
		userService.provide(user);
		
		//QA
		user = new User();
		user.setCode(org.quizzical.backend.security.authorization.api.Constants.QA_USER_CODE);
		user.setFirstName(org.quizzical.backend.security.authorization.api.Constants.QA_USER_NAME);
		user.setEmailAddress(org.quizzical.backend.security.authorization.api.Constants.QA_USER_CODE);
		
		user.setQa(true);
		user.setPasswordEncrypted(true);
		user.setPassword("q7lqa");
		
		userService.provide(user);
		
		//Test
		user = new User();
		user.setCode("tester");
		user.setFirstName("Tester");
		user.setEmailAddress("test@me.io");
		
		user.setPasswordEncrypted(true);
		user.setPassword("test");
		
		userService.provide(user);
	}
}
