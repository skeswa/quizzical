package org.quizzical.backend.testdesign.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.gauntlet.core.api.ApplicationException;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.quizzical.backend.security.authorization.api.model.user.User;
import org.quizzical.backend.testdesign.api.ITestDesignTemplateGeneratorService;
import org.quizzical.backend.testdesign.api.dao.ITestDesignTemplateContentTypeDAOService;
import org.quizzical.backend.testdesign.api.dao.ITestDesignTemplateDAOService;
import org.quizzical.backend.testdesign.api.model.Constants;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplate;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateContentSubType;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateItem;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateItemDifficultyType;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateSection;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateSectionType;

@SuppressWarnings("restriction")
public class TestDesignTemplateGeneratorImpl implements ITestDesignTemplateGeneratorService {
	private volatile LogService logger;
	
	private volatile BundleContext ctx;
	
	private volatile ITestDesignTemplateDAOService testDesignService;
	
	private volatile ITestDesignTemplateContentTypeDAOService contentTypeService;

	@Override
	public TestDesignTemplate generateByCategoriesAndTestSize(final User user, final List<String> categoryCodes, final Integer testSize)
			throws ApplicationException {
		final int effectiveTestSize = deriveActualTestSize(testSize);
		final int sizePerSection = deriveSectionSize(effectiveTestSize,categoryCodes.size());
		final int sizeByDifficulty = sizePerSection/(3*2); //3=difficulty 2=cal/non-calc sections
		
		
		final String code = String.format("Generated TestDesignTemplate (%s) User(%s)",new Date().toString(),user.getCode());
		
		AtomicInteger counter = new AtomicInteger(0);
		
		TestDesignTemplate tt = new TestDesignTemplate(user.getId(),code,code);
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
		for (String catCode : categoryCodes) {
			final TestDesignTemplateContentSubType subType = contentTypeService.getTestDesignTemplateContentSubTypeByCode(catCode);
			
			//Easy
			createItems(counter, subType, nonCalcSec, items, TestDesignTemplateItemDifficultyType.EASY, sizeByDifficulty);

			//Medium
			createItems(counter, subType, nonCalcSec, items, TestDesignTemplateItemDifficultyType.MEDIUM, sizeByDifficulty);
			
			//Hard
			createItems(counter, subType, nonCalcSec, items, TestDesignTemplateItemDifficultyType.HARD, sizeByDifficulty);
		}
		
		//********** NonCal sect
		items = new ArrayList<>();
		calcSec.setItems(items);
		
		
		for (String catCode : categoryCodes) {
			final TestDesignTemplateContentSubType subType = contentTypeService.getTestDesignTemplateContentSubTypeByCode(catCode);
			
			//Easy
			createItems(counter, subType, calcSec, items, TestDesignTemplateItemDifficultyType.EASY, sizeByDifficulty);

			//Medium
			createItems(counter, subType, calcSec, items, TestDesignTemplateItemDifficultyType.MEDIUM, sizeByDifficulty);
			
			//Hard
			createItems(counter, subType, calcSec, items, TestDesignTemplateItemDifficultyType.HARD, sizeByDifficulty);
		}		
		
		return testDesignService.provide(tt);
	}
	

	private void createItems(AtomicInteger counter, TestDesignTemplateContentSubType cst, TestDesignTemplateSection nonCalcSec,
			List<TestDesignTemplateItem> items, TestDesignTemplateItemDifficultyType diff, Integer perDiffAndCalTypeCount) {
		TestDesignTemplateItem item;
		for (int index = 0; index < perDiffAndCalTypeCount; index++) {
			item = new TestDesignTemplateItem(cst,nonCalcSec,diff,counter.incrementAndGet());
			items.add(item);
		}
	}
	
	
	private int deriveSectionSize(int effectiveTestSize, int categorySize) {
		return effectiveTestSize/categorySize;
	}

	private int deriveActualTestSize(Integer testSize) {
		if (testSize <= Constants.QUIZ_SMALL_SIZE)
			return Constants.QUIZ_SMALL_SIZE;
		else if (testSize > Constants.QUIZ_SMALL_SIZE && testSize < Constants.QUIZ_MEDIUM_SIZE)
			return Constants.QUIZ_MEDIUM_SIZE;
		else
			return Constants.QUIZ_FULL_SIZE;
	}
}