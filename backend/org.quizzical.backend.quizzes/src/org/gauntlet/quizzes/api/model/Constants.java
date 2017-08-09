package org.gauntlet.quizzes.api.model;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.ServiceReference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

public interface Constants {
	public static final String GNT_MODULE = "quiz";
	public static final String GNT_JPA_MODULE = "gnt_quiz";
	
	// Quiz Types.
	public static final String QUIZ_TYPE_GENERATED_CODE = "GeneratedQuiz";
	public static final String QUIZ_TYPE_GENERATED_NAME = "Generated Quiz";
	
	public static final String QUIZ_TYPE_DIAGNOSTIC_CODE = "Diagnostic";
	public static final String QUIZ_TYPE_DIAGNOSTIC_NAME = "Diagnostic Quiz";
	
	public static final String QUIZ_TYPE_PRACTICE_CODE = "Practice";
	public static final String QUIZ_TYPE_PRACTICE_NAME = "Full Practice Quiz";
	
	public static final String QUIZ_TYPE_WEAKNESS_CODE = "WeaknessBased";
	public static final String QUIZ_TYPE_WEAKNESS_NAME = "WeaknessBased Quiz";
	
	public static final String QUIZ_TYPE_LRU_CODE = "LeastRecentlyUsed";
	public static final String QUIZ_TYPE_LRU_NAME = "Least Recently Used Quiz";
	
	public static final String QUIZ_TYPE_SKIPPED_OR_INCORRECT_CODE = "SkippedOrIncorrect";
	public static final String QUIZ_TYPE_SKIPPED_OR_INCORRECT_NAME = "Skipped Or Incorrect Quiz";
	
	public static final String QUIZ_TYPE_UNPRACTICED_CODE = "Unpracticed";
	public static final String QUIZ_TYPE_UNPRACTICED_NAME = "Unpracticed Quiz";
	
	public static final String QUIZ_TYPE_NON_SAT_UNPRACTICED_CODE = "NonSATUnpracticed";
	public static final String QUIZ_TYPE_NON_SAT_UNPRACTICED__NAME = "NonSAT Unpracticed Quiz";
	
	public static final String QUIZ_TYPE_NON_SAT_WEAKNESS_CODE = "NonSATWeakness";
	public static final String QUIZ_TYPE_NON_SAT_WEAKNESS__NAME = "NonSAT Weakness Quiz";
	
	public static final String QUIZ_TYPE_CATEGORY_CODE = "Category";
	public static final String QUIZ_TYPE_CATEGORY_NAME = "By Category Quiz";
	
	public static final String QUIZ_TYPE_LESSON_CODE = "Lesson";
	public static final String QUIZ_TYPE_LESSON_NAME = "By Lesson Quiz";
	
	//Quiz Event Topics
	public static final String EVENT_TOPIC_QUIZ_SUBMITTED = "org/quizzical/backend/quizzes/QUIZ_SUBMITTED";
}
