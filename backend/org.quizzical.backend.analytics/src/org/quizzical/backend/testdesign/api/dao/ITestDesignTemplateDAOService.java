package org.quizzical.backend.testdesign.api.dao;

import java.util.List;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplate;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateItem;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateSection;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateType;
import org.quizzical.backend.testdesign.model.jpa.JPATestDesignTemplateItem;

public interface ITestDesignTemplateDAOService {
	//
	List<TestDesignTemplate> findAll(int start, int end) throws ApplicationException;

	long countAll() throws ApplicationException;

	TestDesignTemplate getByPrimary(Long pk) throws ApplicationException, NoSuchModelException;

	TestDesignTemplate provide(TestDesignTemplate record) throws ApplicationException;

	TestDesignTemplate update(TestDesignTemplate record) throws ApplicationException;

	TestDesignTemplate delete(Long id) throws ApplicationException, NoSuchModelException;

	TestDesignTemplate getByCode(String code) throws ApplicationException;

	TestDesignTemplate getByName(String name) throws ApplicationException;

	List<TestDesignTemplate> findByTestDesignTemplateTypeId(Long typeId, int start, int end)
			throws ApplicationException;

	int countByTestDesignTemplateTypeId(Long typeId) throws ApplicationException;

	//
	List<TestDesignTemplateType> findAllTestDesignTemplateTypes(int start, int end) throws ApplicationException;

	long countAllTestDesignTemplateTypes() throws ApplicationException;

	TestDesignTemplateType getTestDesignTemplateTypeByPrimary(Long pk)
			throws ApplicationException, NoSuchModelException;

	TestDesignTemplateType provideTestDesignTemplateType(TestDesignTemplateType record) throws ApplicationException;

	TestDesignTemplateType provideTestDesignTemplateType(String name) throws ApplicationException;

	//
	List<TestDesignTemplateItem> findAllTestDesignTemplateCategories(int start, int end) throws ApplicationException;

	long countAllTestDesignTemplateCategories() throws ApplicationException;

	TestDesignTemplateItem getTestDesignTemplateItemByPrimary(Long pk)
			throws ApplicationException, NoSuchModelException;

	TestDesignTemplateItem provideTestDesignTemplateItem(TestDesignTemplateItem record) throws ApplicationException;

	TestDesignTemplateItem updateTestDesignTemplateItem(JPATestDesignTemplateItem record) throws ApplicationException;

	TestDesignTemplateItem getTestDesignTemplateItemByCode(String code) throws ApplicationException;

	TestDesignTemplateItem getTestDesignTemplateItemByName(String code) throws ApplicationException;

	TestDesignTemplateItem deleteTestDesignTemplateItem(Long id) throws ApplicationException, NoSuchModelException;

	
	//
	List<TestDesignTemplateSection> findAllTestDesignTemplateSections(int start, int end) throws ApplicationException;

	long countAllTestDesignTemplateSections() throws ApplicationException;

	TestDesignTemplateSection getTestDesignTemplateSectionByPrimary(Long pk)
			throws ApplicationException, NoSuchModelException;

	TestDesignTemplateSection provideTestDesignTemplateSection(TestDesignTemplateSection record)
			throws ApplicationException;

	List<TestDesignTemplateItem> getOrderedTestItems(Long pk)
			throws ApplicationException, NoSuchModelException;

}
