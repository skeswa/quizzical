package org.gauntlet.problems.defaults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateItemDifficultyType;
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
		final Map<String,TestDesignTemplateContentSubType> subTypes = generateCategories();
		
		//-- Full PT
		generateFullDefaultPracticeTest(subTypes);
		
		//-- Tests by Cat & Size
		generateCategoryTestTemplate(subTypes,Constants.QUIZ_SMALL_SIZE, Constants.QUIZ_GENERATION_PREFIX_CATEGORY, Constants.QUIZ_SMALL_SUFFIX);
		generateCategoryTestTemplate(subTypes,Constants.QUIZ_MEDIUM_SIZE, Constants.QUIZ_GENERATION_PREFIX_CATEGORY, Constants.QUIZ_MEDIUM_SUFFIX);
		generateCategoryTestTemplate(subTypes,Constants.QUIZ_FULL_SIZE, Constants.QUIZ_GENERATION_PREFIX_CATEGORY, Constants.QUIZ_FULL_SUFFIX);
	}
	
	public void generateFullDefaultPracticeTest(final Map<String,TestDesignTemplateContentSubType> subTypes) throws ApplicationException {
		TestDesignTemplateContentSubType stLinEquations = subTypes.get(Constants.CONTENT_TYPE_HEART_OF_ALGEBRA+"/"+Constants.CONTENT_TYPE_LINEAR_EQUESTIONS);
		TestDesignTemplateContentSubType stSysLinEquations = subTypes.get(Constants.CONTENT_TYPE_HEART_OF_ALGEBRA+"/"+Constants.CONTENT_TYPE_SYSTEMS_OF_LINEAR_EQUESTIONS);
		TestDesignTemplateContentSubType stInequal = subTypes.get(Constants.CONTENT_TYPE_HEART_OF_ALGEBRA+"/"+Constants.CONTENT_TYPE_IEQUALITIES);
		
		
		TestDesignTemplateContentSubType stRatesRatios = subTypes.get(Constants.CONTENT_TYPE_PROBLEM_SOLVING_AND_DATA_ANALYSIS+"/"+Constants.CONTENT_TYPE_RATES_RATIOS_PROPORTIONS_AND_PERCENTAGES);
		TestDesignTemplateContentSubType stScatterplots = subTypes.get(Constants.CONTENT_TYPE_PROBLEM_SOLVING_AND_DATA_ANALYSIS+"/"+Constants.CONTENT_TYPE_SCATTERPLOTS);
		TestDesignTemplateContentSubType stStatsAndProb = subTypes.get(Constants.CONTENT_TYPE_PROBLEM_SOLVING_AND_DATA_ANALYSIS+"/"+Constants.CONTENT_TYPE_STATISTICS_AND_PROBABILITY);
		
		TestDesignTemplateContentSubType stFunctions = subTypes.get(Constants.CONTENT_TYPE_PASSPORT_TO_ADVANCED_MATH+"/"+Constants.CONTENT_TYPE_FUNCTIONS);
		TestDesignTemplateContentSubType stQuadratics = subTypes.get(Constants.CONTENT_TYPE_PASSPORT_TO_ADVANCED_MATH+"/"+Constants.CONTENT_TYPE_QUADRATICS);
		TestDesignTemplateContentSubType stExponents= subTypes.get(Constants.CONTENT_TYPE_PASSPORT_TO_ADVANCED_MATH+"/"+Constants.CONTENT_TYPE_EXPONENTS);
		
		TestDesignTemplateContentSubType stGeometry = subTypes.get(Constants.CONTENT_TYPE_ADDITIONAL_TOPICS_IN_MATH+"/"+Constants.CONTENT_TYPE_GEOMETRY);
		TestDesignTemplateContentSubType stImagNumbers = subTypes.get(Constants.CONTENT_TYPE_ADDITIONAL_TOPICS_IN_MATH+"/"+Constants.CONTENT_TYPE_IMAGINARY_NUMBERS);
		TestDesignTemplateContentSubType stTrig = subTypes.get(Constants.CONTENT_TYPE_ADDITIONAL_TOPICS_IN_MATH+"/"+Constants.CONTENT_TYPE_TRIGONOMETRY);
		
		//PT
		TestDesignTemplate practiceTest = new TestDesignTemplate("PracticeTest1","PracticeTest1");
		List<TestDesignTemplateSection> sections = new ArrayList<>();
		practiceTest.setSections(sections);
		
		//Sections
		TestDesignTemplateSection nonCalcSec = new TestDesignTemplateSection(TestDesignTemplateSectionType.CALCULATOR_NOT_ALLOWED,practiceTest,1);
		nonCalcSec.setTemplate(practiceTest);
		TestDesignTemplateSection calcSec = new TestDesignTemplateSection(TestDesignTemplateSectionType.CALCULATOR_ALLOWED,practiceTest,2);
		calcSec.setTemplate(practiceTest);
		sections.add(nonCalcSec);
		sections.add(calcSec);
		
		//********** NonCal sect
		List<TestDesignTemplateItem> items = new ArrayList<>();
		nonCalcSec.setItems(items);
		
		//---- Easy
		TestDesignTemplateItem item = new TestDesignTemplateItem(stLinEquations,nonCalcSec,TestDesignTemplateItemDifficultyType.EASY,1);
		items.add(item);
		item = new TestDesignTemplateItem(stExponents,nonCalcSec,TestDesignTemplateItemDifficultyType.EASY,2);
		items.add(item);
		item = new TestDesignTemplateItem(stGeometry,nonCalcSec,TestDesignTemplateItemDifficultyType.EASY,3);
		items.add(item);
		item = new TestDesignTemplateItem(stFunctions,nonCalcSec,TestDesignTemplateItemDifficultyType.EASY,4);
		items.add(item);
		
		//---- Medium
		item = new TestDesignTemplateItem(stLinEquations,nonCalcSec,TestDesignTemplateItemDifficultyType.MEDIUM,5);
		items.add(item);
		item = new TestDesignTemplateItem(stFunctions,nonCalcSec,TestDesignTemplateItemDifficultyType.MEDIUM,6);
		items.add(item);
		item = new TestDesignTemplateItem(stScatterplots,nonCalcSec,TestDesignTemplateItemDifficultyType.MEDIUM,7);
		items.add(item);
		item = new TestDesignTemplateItem(stLinEquations,nonCalcSec,TestDesignTemplateItemDifficultyType.MEDIUM,8);
		items.add(item);
		item = new TestDesignTemplateItem(stExponents,nonCalcSec,TestDesignTemplateItemDifficultyType.MEDIUM,9);
		items.add(item);
		item = new TestDesignTemplateItem(stLinEquations,nonCalcSec,TestDesignTemplateItemDifficultyType.MEDIUM,10);
		items.add(item);
		
		//---- Hard
		item = new TestDesignTemplateItem(stSysLinEquations,nonCalcSec,TestDesignTemplateItemDifficultyType.HARD,11);
		items.add(item);
		item = new TestDesignTemplateItem(stInequal,nonCalcSec,TestDesignTemplateItemDifficultyType.HARD,12);
		items.add(item);
		item = new TestDesignTemplateItem(stFunctions,nonCalcSec,TestDesignTemplateItemDifficultyType.HARD,13);
		items.add(item);
		item = new TestDesignTemplateItem(stImagNumbers,nonCalcSec,TestDesignTemplateItemDifficultyType.MEDIUM,14);
		items.add(item);
		item = new TestDesignTemplateItem(stExponents,nonCalcSec,TestDesignTemplateItemDifficultyType.HARD,15);
		items.add(item);
		item = new TestDesignTemplateItem(stLinEquations,nonCalcSec,TestDesignTemplateItemDifficultyType.MEDIUM,16);
		items.add(item);
		item = new TestDesignTemplateItem(stInequal,nonCalcSec,TestDesignTemplateItemDifficultyType.MEDIUM,17);
		items.add(item);
		item = new TestDesignTemplateItem(stExponents,nonCalcSec,TestDesignTemplateItemDifficultyType.HARD,18);
		items.add(item);
		item = new TestDesignTemplateItem(stGeometry,nonCalcSec,TestDesignTemplateItemDifficultyType.HARD,19);
		items.add(item);
		item = new TestDesignTemplateItem(stQuadratics,nonCalcSec,TestDesignTemplateItemDifficultyType.HARD,20);
		items.add(item);
		
		//********** Calc allowed sect
		items = new ArrayList<>();
		calcSec.setItems(items);
		
		//---- Easy
		item = new TestDesignTemplateItem(stRatesRatios,calcSec,TestDesignTemplateItemDifficultyType.EASY,1);
		items.add(item);
		item = new TestDesignTemplateItem(stLinEquations,calcSec,TestDesignTemplateItemDifficultyType.EASY,2);
		items.add(item);
		item = new TestDesignTemplateItem(stInequal,calcSec,TestDesignTemplateItemDifficultyType.EASY,3);
		items.add(item);
		item = new TestDesignTemplateItem(stQuadratics,calcSec,TestDesignTemplateItemDifficultyType.EASY,4);
		items.add(item);
		item = new TestDesignTemplateItem(stRatesRatios,calcSec,TestDesignTemplateItemDifficultyType.EASY,5);
		items.add(item);
		item = new TestDesignTemplateItem(stScatterplots,calcSec,TestDesignTemplateItemDifficultyType.EASY,6);
		items.add(item);
		item = new TestDesignTemplateItem(stStatsAndProb,calcSec,TestDesignTemplateItemDifficultyType.EASY,7);
		items.add(item);
		
		//---- Medium
		//8.
		item = new TestDesignTemplateItem(stStatsAndProb,calcSec,TestDesignTemplateItemDifficultyType.MEDIUM,8);
		items.add(item);
		item = new TestDesignTemplateItem(stSysLinEquations,calcSec,TestDesignTemplateItemDifficultyType.MEDIUM,9);
		items.add(item);
		item = new TestDesignTemplateItem(stSysLinEquations,calcSec,TestDesignTemplateItemDifficultyType.MEDIUM,10);
		items.add(item);
		item = new TestDesignTemplateItem(stLinEquations,calcSec,TestDesignTemplateItemDifficultyType.MEDIUM,11);
		items.add(item);
		item = new TestDesignTemplateItem(stGeometry,calcSec,TestDesignTemplateItemDifficultyType.MEDIUM,12);
		items.add(item);
		item = new TestDesignTemplateItem(stRatesRatios,calcSec,TestDesignTemplateItemDifficultyType.MEDIUM,13);
		items.add(item);
		
		//14.
		item = new TestDesignTemplateItem(stStatsAndProb,calcSec,TestDesignTemplateItemDifficultyType.MEDIUM,14);
		items.add(item);
		item = new TestDesignTemplateItem(stStatsAndProb,calcSec,TestDesignTemplateItemDifficultyType.MEDIUM,15);
		items.add(item);
		item = new TestDesignTemplateItem(stQuadratics,calcSec,TestDesignTemplateItemDifficultyType.MEDIUM,16);
		items.add(item);
		item = new TestDesignTemplateItem(stLinEquations,calcSec,TestDesignTemplateItemDifficultyType.MEDIUM,17);
		items.add(item);
		item = new TestDesignTemplateItem(stStatsAndProb,calcSec,TestDesignTemplateItemDifficultyType.MEDIUM,18);
		items.add(item);
		item = new TestDesignTemplateItem(stQuadratics,calcSec,TestDesignTemplateItemDifficultyType.MEDIUM,19);
		items.add(item);
		
		
		//20.
		item = new TestDesignTemplateItem(stFunctions,calcSec,TestDesignTemplateItemDifficultyType.MEDIUM,20);
		items.add(item);
		item = new TestDesignTemplateItem(stLinEquations,calcSec,TestDesignTemplateItemDifficultyType.MEDIUM,21);
		items.add(item);
		item = new TestDesignTemplateItem(stFunctions,calcSec,TestDesignTemplateItemDifficultyType.MEDIUM,22);
		items.add(item);
		item = new TestDesignTemplateItem(stRatesRatios,calcSec,TestDesignTemplateItemDifficultyType.MEDIUM,23);
		items.add(item);
		item = new TestDesignTemplateItem(stInequal,calcSec,TestDesignTemplateItemDifficultyType.MEDIUM,24);
		items.add(item);
		item = new TestDesignTemplateItem(stRatesRatios,calcSec,TestDesignTemplateItemDifficultyType.MEDIUM,25);
		items.add(item);
		
		//26.
		item = new TestDesignTemplateItem(stScatterplots,calcSec,TestDesignTemplateItemDifficultyType.MEDIUM,26);
		items.add(item);
		
		//-- Hard
		//27.
		item = new TestDesignTemplateItem(stGeometry,calcSec,TestDesignTemplateItemDifficultyType.HARD,27);
		items.add(item);
		item = new TestDesignTemplateItem(stLinEquations,calcSec,TestDesignTemplateItemDifficultyType.HARD,28);
		items.add(item);
		item = new TestDesignTemplateItem(stStatsAndProb,calcSec,TestDesignTemplateItemDifficultyType.HARD,29);
		items.add(item);
		item = new TestDesignTemplateItem(stExponents,calcSec,TestDesignTemplateItemDifficultyType.HARD,30);
		items.add(item);
		item = new TestDesignTemplateItem(stLinEquations,calcSec,TestDesignTemplateItemDifficultyType.EASY,31);
		items.add(item);
		
		//32.
		item = new TestDesignTemplateItem(stExponents,calcSec,TestDesignTemplateItemDifficultyType.MEDIUM,32);
		items.add(item);
		item = new TestDesignTemplateItem(stStatsAndProb,calcSec,TestDesignTemplateItemDifficultyType.MEDIUM,33);
		items.add(item);
		item = new TestDesignTemplateItem(stGeometry,calcSec,TestDesignTemplateItemDifficultyType.HARD,34);
		items.add(item);
		item = new TestDesignTemplateItem(stLinEquations,calcSec,TestDesignTemplateItemDifficultyType.HARD,35);
		items.add(item);
		item = new TestDesignTemplateItem(stRatesRatios,calcSec,TestDesignTemplateItemDifficultyType.HARD,36);
		items.add(item);
		item = new TestDesignTemplateItem(stRatesRatios,calcSec,TestDesignTemplateItemDifficultyType.MEDIUM,37);
		items.add(item);
		item = new TestDesignTemplateItem(stTrig,calcSec,TestDesignTemplateItemDifficultyType.HARD,38);
		items.add(item);
		
		testDesignService.provide(practiceTest);
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
	
	
	public void generateCategoryTestTemplate(final Map<String,TestDesignTemplateContentSubType> subTypes, final Integer testItemCount, final String typePrefix,final String sizeSuffix) throws ApplicationException {
		final Integer perSectionCount = (int)(testItemCount/2);
		final Integer perDiffAndCalTypeCount = (int)(perSectionCount/3);
		AtomicInteger counter = new AtomicInteger(0);
		
		for (String catKey : subTypes.keySet()) {
			//PT
			final String code = typePrefix+org.gauntlet.core.model.Constants.GNT_TABLE_NAME_SEPARATOR+catKey+org.gauntlet.core.model.Constants.GNT_TABLE_NAME_SEPARATOR+sizeSuffix;
			
			TestDesignTemplateContentSubType cst = subTypes.get(catKey);
			TestDesignTemplate tt = new TestDesignTemplate(code,code);
			List<TestDesignTemplateSection> sections = new ArrayList<>();
			tt.setSections(sections);
			
			//Sections
			TestDesignTemplateSection nonCalcSec = new TestDesignTemplateSection(TestDesignTemplateSectionType.CALCULATOR_NOT_ALLOWED,tt,1);
			nonCalcSec.setTemplate(tt);
			TestDesignTemplateSection calcSec = new TestDesignTemplateSection(TestDesignTemplateSectionType.CALCULATOR_ALLOWED,tt,2);
			calcSec.setTemplate(tt);
			sections.add(nonCalcSec);
			sections.add(calcSec);
			
			//********** NonCal sect
			List<TestDesignTemplateItem> items = new ArrayList<>();
			nonCalcSec.setItems(items);
			
			
			TestDesignTemplateItem item = null;
			//---- Easy
			createItems(counter, cst, nonCalcSec, items, TestDesignTemplateItemDifficultyType.EASY, perDiffAndCalTypeCount);
			createItems(counter, cst, nonCalcSec, items, TestDesignTemplateItemDifficultyType.MEDIUM, perDiffAndCalTypeCount);
			createItems(counter, cst, nonCalcSec, items, TestDesignTemplateItemDifficultyType.MEDIUM, perDiffAndCalTypeCount);

			//********** Cal sect
			items = new ArrayList<>();
			calcSec.setItems(items);
			
			createItems(counter, cst, calcSec, items, TestDesignTemplateItemDifficultyType.EASY, perDiffAndCalTypeCount);
			createItems(counter, cst, calcSec, items, TestDesignTemplateItemDifficultyType.MEDIUM, perDiffAndCalTypeCount);
			createItems(counter, cst, calcSec, items, TestDesignTemplateItemDifficultyType.HARD, perDiffAndCalTypeCount);
			
			testDesignService.provide(tt);
		}
	}

	private void createItems(AtomicInteger counter, TestDesignTemplateContentSubType cst, TestDesignTemplateSection nonCalcSec,
			List<TestDesignTemplateItem> items, TestDesignTemplateItemDifficultyType diff, Integer perDiffAndCalTypeCount) {
		TestDesignTemplateItem item;
		for (int index = 0; index < perDiffAndCalTypeCount; index++) {
			item = new TestDesignTemplateItem(cst,nonCalcSec,diff,counter.incrementAndGet());
			items.add(item);
		}
	}


	private TestDesignTemplateContentSubType createSubType(Map<String, TestDesignTemplateContentSubType> subTypesMap,
			TestDesignTemplateContentType type, String subType) {
		final TestDesignTemplateContentSubType st = new TestDesignTemplateContentSubType(type,subType,subType);
		subTypesMap.put(st.getCode(), st);
		return st;
	}
}
