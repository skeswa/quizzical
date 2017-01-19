package org.gauntlet.problems.defaults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.problems.api.dao.IProblemDAOService;
import org.gauntlet.problems.api.model.ProblemCategory;
import org.gauntlet.problems.api.model.ProblemDifficulty;
import org.gauntlet.problems.api.model.ProblemSource;
import org.gauntlet.quizzes.api.dao.IQuizDAOService;
import org.gauntlet.quizzes.api.model.QuizType;
import org.osgi.service.log.LogService;
import org.quizzical.backend.security.api.dao.user.IUserDAOService;
import org.quizzical.backend.security.api.model.user.User;
import org.quizzical.backend.testdesign.api.dao.ITestDesignTemplateContentTypeDAOService;
import org.quizzical.backend.testdesign.api.dao.ITestDesignTemplateDAOService;
import org.quizzical.backend.testdesign.api.model.Constants;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplate;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateContentSubType;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateContentType;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateItem;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateSection;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateSectionType;


public class TestDesignTemplateGenerator {
	private volatile LogService logger;
	private volatile ITestDesignTemplateDAOService testDesignService;
	private volatile ITestDesignTemplateContentTypeDAOService contentTypeService;
	
	public TestDesignTemplateGenerator(final ITestDesignTemplateDAOService testDesignService,
									   final  ITestDesignTemplateContentTypeDAOService contentTypeService) {
		super();
		this.testDesignService = testDesignService;
		this.contentTypeService = contentTypeService;
	}
	
	public void generate() throws ApplicationException {
		//Categories and subCategories
		final Map<String,TestDesignTemplateContentSubType> subTypes = generateCategories();
		
		TestDesignTemplateContentSubType stLinEquations = subTypes.get(Constants.CONTENT_TYPE_HEART_OF_ALGEBRA+"/"+Constants.CONTENT_TYPE_LINEAR_EQUESTIONS);
		TestDesignTemplateContentSubType stSysLinEquations = subTypes.get(Constants.CONTENT_TYPE_HEART_OF_ALGEBRA+"/"+Constants.CONTENT_TYPE_SYSTEMS_OF_LINEAR_EQUESTIONS);
		TestDesignTemplateContentSubType stInequal = subTypes.get(Constants.CONTENT_TYPE_HEART_OF_ALGEBRA+"/"+Constants.CONTENT_TYPE_IEQUALITIES);
		
		
		TestDesignTemplateContentSubType stRatesRatios = subTypes.get(Constants.CONTENT_TYPE_PROBLEM_SOLVING_AND_DATA_ANALYSIS+"/"+Constants.CONTENT_TYPE_RATES_RATIOS_PROPORTIONS_AND_PERCENTAGES);
		
		//PT
		TestDesignTemplate practiceTest = new TestDesignTemplate("PracticeTest","PracticeTest");
		List<TestDesignTemplateSection> sections = new ArrayList<>();
		practiceTest.setSections(sections);
		
		//Sections
		TestDesignTemplateSection nonCalcSec = new TestDesignTemplateSection(TestDesignTemplateSectionType.CALCULATOR_NOT_ALLOWED,practiceTest,1);
		TestDesignTemplateSection calcSec = new TestDesignTemplateSection(TestDesignTemplateSectionType.CALCULATOR_ALLOWED,practiceTest,2);
		sections.add(nonCalcSec);
		sections.add(calcSec);
		
		//NonCal sect
		TestDesignTemplateItem item = new TestDesignTemplateItem();
	}

	private Map<String, TestDesignTemplateContentSubType> generateCategories() throws ApplicationException {
		Map<String, TestDesignTemplateContentSubType> subTypesMap = new HashMap<>();
		//Heart of Algebra
/*		public static final String CONTENT_TYPE_HEART_OF_ALGEBRA = "Heart of Algebra";
		public static final String CONTENT_TYPE_LINEAR_EQUESTIONS = "Linear Equations";
		public static final String CONTENT_TYPE_SYSTEMS_OF_LINEAR_EQUESTIONS = "Systems of Linear Equations";
		public static final String CONTENT_TYPE_IEQUALITIES = "Inequalities";*/
		TestDesignTemplateContentType type = new TestDesignTemplateContentType(Constants.CONTENT_TYPE_HEART_OF_ALGEBRA, Constants.CONTENT_TYPE_HEART_OF_ALGEBRA);
		List<TestDesignTemplateContentSubType> subTypes = new ArrayList<>();
		type.setSubTypes(subTypes);
		subTypes.add(createSubType(subTypesMap,type,Constants.CONTENT_TYPE_LINEAR_EQUESTIONS));
		subTypes.add(createSubType(subTypesMap,type,Constants.CONTENT_TYPE_SYSTEMS_OF_LINEAR_EQUESTIONS));
		subTypes.add(createSubType(subTypesMap,type,Constants.CONTENT_TYPE_IEQUALITIES));
		contentTypeService.provide(type);
		
		//Problem Solving and Data Analysis
/*		public static final String CONTENT_TYPE_PROBLEM_SOLVING_AND_DATA_ANALYSIS = "Problem Solving and Data Analysis";
		public static final String CONTENT_TYPE_RATES_RATIOS_PROPORTIONS_AND_PERCENTAGES = "Rates, Ratios, Proportions, and Percentages";
		public static final String CONTENT_TYPE_SCATTERPLOTS ="Scatterplots";
		public static final String CONTENT_TYPE_STATISTICS_AND_PROBABILITY ="Statistics and Probability";*/
		type = new TestDesignTemplateContentType(Constants.CONTENT_TYPE_PROBLEM_SOLVING_AND_DATA_ANALYSIS, Constants.CONTENT_TYPE_PROBLEM_SOLVING_AND_DATA_ANALYSIS);
		subTypes = new ArrayList<>();
		type.setSubTypes(subTypes);
		subTypes.add(createSubType(subTypesMap,type,Constants.CONTENT_TYPE_RATES_RATIOS_PROPORTIONS_AND_PERCENTAGES));
		subTypes.add(createSubType(subTypesMap,type,Constants.CONTENT_TYPE_SCATTERPLOTS));
		subTypes.add(createSubType(subTypesMap,type,Constants.CONTENT_TYPE_STATISTICS_AND_PROBABILITY));
		contentTypeService.provide(type);
		
		//PassportToAdvanceMath
/*		public static final String CONTENT_TYPE_PASSPORT_TO_ADVANCED_MATH = "Passport to Advanced Math";	
		public static final String CONTENT_TYPE_FUNCTIONS = "Functions";
		public static final String CONTENT_TYPE_QUADRATICS = "Quadratics";
		public static final String CONTENT_TYPE_EXPONENTS = "Exponents";*/
		type = new TestDesignTemplateContentType(Constants.CONTENT_TYPE_PASSPORT_TO_ADVANCED_MATH, Constants.CONTENT_TYPE_PASSPORT_TO_ADVANCED_MATH);
		subTypes = new ArrayList<>();
		type.setSubTypes(subTypes);
		subTypes.add(createSubType(subTypesMap,type,Constants.CONTENT_TYPE_FUNCTIONS));
		subTypes.add(createSubType(subTypesMap,type,Constants.CONTENT_TYPE_QUADRATICS));
		subTypes.add(createSubType(subTypesMap,type,Constants.CONTENT_TYPE_EXPONENTS));
		contentTypeService.provide(type);
		
		//AdditionalTopicsInMath
/*		public static final String CONTENT_TYPE_ADDITIONAL_TOPICS_IN_MATH = "Additional Topics in Math";
		public static final String CONTENT_TYPE_GEOMETRY = "Geometry";
		public static final String CONTENT_TYPE_IMAGINARY_NUMBERS = "Imaginary Numbers";
		public static final String CONTENT_TYPE_TRIGONOMETRY = "Trigonometry";*/
		type = new TestDesignTemplateContentType(Constants.CONTENT_TYPE_ADDITIONAL_TOPICS_IN_MATH, Constants.CONTENT_TYPE_ADDITIONAL_TOPICS_IN_MATH);
		subTypes = new ArrayList<>();
		type.setSubTypes(subTypes);
		subTypes.add(createSubType(subTypesMap,type,Constants.CONTENT_TYPE_GEOMETRY));
		subTypes.add(createSubType(subTypesMap,type,Constants.CONTENT_TYPE_IMAGINARY_NUMBERS));
		subTypes.add(createSubType(subTypesMap,type,Constants.CONTENT_TYPE_TRIGONOMETRY));
		contentTypeService.provide(type);
		
		return subTypesMap;
	}

	private TestDesignTemplateContentSubType createSubType(Map<String, TestDesignTemplateContentSubType> subTypesMap,
			TestDesignTemplateContentType type, String contentTypeLinearEquestions) {
		final TestDesignTemplateContentSubType st = new TestDesignTemplateContentSubType(type,Constants.CONTENT_TYPE_LINEAR_EQUESTIONS,Constants.CONTENT_TYPE_LINEAR_EQUESTIONS);
		subTypesMap.put(st.getCode(), st);
		return st;
	}
}
