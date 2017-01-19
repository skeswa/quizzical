package org.quizzical.backend.testdesign.api.dao;

import java.util.List;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateContentSubType;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateContentType;

public interface ITestDesignTemplateContentTypeDAOService {
	//
	List<TestDesignTemplateContentType> findAll(int start, int end) throws ApplicationException;

	long countAll() throws ApplicationException;

	TestDesignTemplateContentType getByPrimary(Long pk) throws ApplicationException, NoSuchModelException;

	TestDesignTemplateContentType provide(TestDesignTemplateContentType record) throws ApplicationException;

	TestDesignTemplateContentType update(TestDesignTemplateContentType record) throws ApplicationException;

	TestDesignTemplateContentType delete(Long id) throws ApplicationException, NoSuchModelException;

	TestDesignTemplateContentType getByCode(String code) throws ApplicationException;

	TestDesignTemplateContentType getByName(String name) throws ApplicationException;


	//
	List<TestDesignTemplateContentSubType> findAllContentSubTypes(int start, int end) throws ApplicationException;

	long countAllContentSubTypes() throws ApplicationException;

	TestDesignTemplateContentSubType getContentSubTypeByPrimary(Long pk) throws ApplicationException, NoSuchModelException;

	TestDesignTemplateContentSubType provideContentSubType(TestDesignTemplateContentSubType record) throws ApplicationException;

	TestDesignTemplateContentSubType updatContentSubTypee(TestDesignTemplateContentSubType record) throws ApplicationException;

	TestDesignTemplateContentSubType deleteContentSubType(Long id) throws ApplicationException, NoSuchModelException;

	TestDesignTemplateContentSubType getContentSubTypeByCode(String code) throws ApplicationException;

	TestDesignTemplateContentSubType getContentSubTypeByName(String name) throws ApplicationException;
}
