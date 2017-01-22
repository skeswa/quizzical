package org.quizzical.backend.testdesign.api.model;


public interface Constants {
	public static final String Q7L_MODULE = "td";
	public static final String Q7L_JPA_MODULE = "q7l_"+Q7L_MODULE;

	// Quiz Types.
	public static final String QUIZ_TYPE_GENERATED_CODE = "GeneratedQuiz";
	public static final String QUIZ_TYPE_GENERATED_NAME = "Generated Quiz";
	
	// Categories & Sub-Categories
	
	//Heart of Algebra
	public static final String CONTENT_TYPE_HEART_OF_ALGEBRA = "Heart of Algebra";
	public static final String CONTENT_TYPE_LINEAR_EQUESTIONS = "Linear Equations";
	public static final String CONTENT_TYPE_SYSTEMS_OF_LINEAR_EQUESTIONS = "Systems of Linear Equations";
	public static final String CONTENT_TYPE_IEQUALITIES = "Inequalities";
	
	//Problem Solving and Data Analysis
	public static final String CONTENT_TYPE_PROBLEM_SOLVING_AND_DATA_ANALYSIS = "Problem Solving and Data Analysis";
	public static final String CONTENT_TYPE_RATES_RATIOS_PROPORTIONS_AND_PERCENTAGES = "Rates, Ratios, Proportions, and Percentages";
	public static final String CONTENT_TYPE_SCATTERPLOTS ="Scatterplots";
	public static final String CONTENT_TYPE_STATISTICS_AND_PROBABILITY ="Statistics and Probability";
	
	//PassportToAdvanceMath
	public static final String CONTENT_TYPE_PASSPORT_TO_ADVANCED_MATH = "Passport to Advanced Math";	
	public static final String CONTENT_TYPE_FUNCTIONS = "Functions";
	public static final String CONTENT_TYPE_QUADRATICS = "Quadratics";
	public static final String CONTENT_TYPE_EXPONENTS = "Exponents";
	
	//AdditionalTopicsInMath
	public static final String CONTENT_TYPE_ADDITIONAL_TOPICS_IN_MATH = "Additional Topics in Math";
	public static final String CONTENT_TYPE_GEOMETRY = "Geometry";
	public static final String CONTENT_TYPE_IMAGINARY_NUMBERS = "Imaginary Numbers";
	public static final String CONTENT_TYPE_TRIGONOMETRY = "Trigonometry";
}
