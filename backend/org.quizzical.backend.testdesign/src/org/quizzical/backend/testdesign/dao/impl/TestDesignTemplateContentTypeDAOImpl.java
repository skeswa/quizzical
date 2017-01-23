package org.quizzical.backend.testdesign.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.amdatu.jta.Transactional;
import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.core.commons.util.Validator;
import org.gauntlet.core.commons.util.jpa.JPAEntityUtil;
import org.gauntlet.core.model.JPABaseEntity;
import org.gauntlet.core.service.impl.BaseServiceImpl;
import org.osgi.service.log.LogService;
import org.quizzical.backend.testdesign.api.dao.ITestDesignTemplateContentTypeDAOService;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateContentSubType;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateContentType;
import org.quizzical.backend.testdesign.model.jpa.JPATestDesignTemplateContentSubType;
import org.quizzical.backend.testdesign.model.jpa.JPATestDesignTemplateContentType;


@SuppressWarnings("restriction")
@Transactional
public class TestDesignTemplateContentTypeDAOImpl extends BaseServiceImpl implements ITestDesignTemplateContentTypeDAOService {
	//TestDesignTemplateContentTypes
	private volatile LogService logger;
	
	private volatile EntityManager em;
	
	@Override
	public LogService getLogger() {
		return logger;
	}

	public void setLogger(LogService logger) {
		this.logger = logger;
	}

	@Override
	public EntityManager getEm() {
		return em;
	}	
	
	@Override
	public List<TestDesignTemplateContentType> findAll() throws ApplicationException {
		List<TestDesignTemplateContentType> result = new ArrayList<>();
		try {
			List<JPABaseEntity> resultList = super.findAll(JPATestDesignTemplateContentType.class);
			result = JPAEntityUtil.copy(resultList, TestDesignTemplateContentType.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return result;		
	}
	
	@Override
	public List<TestDesignTemplateContentType> findAll(int start, int end) throws ApplicationException {
		List<TestDesignTemplateContentType> result = new ArrayList<>();
		try {
			List<JPABaseEntity> resultList = super.findAll(JPATestDesignTemplateContentType.class,start,end);
			result = JPAEntityUtil.copy(resultList, TestDesignTemplateContentType.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return result;		
	}
	
	@Override
	public long countAll() throws ApplicationException {
		long res = 0;
		try {
			res = super.countAll(JPATestDesignTemplateContentType.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}	
	
	@Override
	public TestDesignTemplateContentType getByPrimary(Long pk) throws ApplicationException, NoSuchModelException {
		JPATestDesignTemplateContentType jpaEntity = (JPATestDesignTemplateContentType) super.findByPrimaryKey(JPATestDesignTemplateContentType.class, pk);
		return JPAEntityUtil.copy(jpaEntity, TestDesignTemplateContentType.class);
	}

	@Override
	public TestDesignTemplateContentType provide(TestDesignTemplateContentType record)
			  throws ApplicationException {
		TestDesignTemplateContentType existingTestDesignTemplateContentType = getByCode(record.getCode());
		if (Validator.isNull(existingTestDesignTemplateContentType))
		{
			JPABaseEntity res = super.add(JPAEntityUtil.copy(record, JPATestDesignTemplateContentType.class));
			existingTestDesignTemplateContentType = JPAEntityUtil.copy(res, TestDesignTemplateContentType.class);
		}

		return existingTestDesignTemplateContentType;
	}
	
	@Override
	public TestDesignTemplateContentType update(TestDesignTemplateContentType record) throws ApplicationException {
		JPABaseEntity res = super.update(JPAEntityUtil.copy(record, JPATestDesignTemplateContentType.class));
		TestDesignTemplateContentType dto = JPAEntityUtil.copy(res, TestDesignTemplateContentType.class);
		return dto;	
	}	
	
	@Override
	public TestDesignTemplateContentType delete(Long id) throws ApplicationException, NoSuchModelException {
		JPATestDesignTemplateContentType jpaEntity = (JPATestDesignTemplateContentType) super.findByPrimaryKey(JPATestDesignTemplateContentType.class, id);
		super.remove(jpaEntity);
		return JPAEntityUtil.copy(jpaEntity, TestDesignTemplateContentType.class);
	}
	
	@Override
	public TestDesignTemplateContentType getByCode(String code) throws ApplicationException {
		JPATestDesignTemplateContentType jpaEntity = (JPATestDesignTemplateContentType) super.findWithAttribute(JPATestDesignTemplateContentType.class, String.class,"code", code);
		return JPAEntityUtil.copy(jpaEntity, TestDesignTemplateContentType.class);
	}


	@Override
	public TestDesignTemplateContentType getByName(String name) throws ApplicationException {
		JPATestDesignTemplateContentType jpaEntity = (JPATestDesignTemplateContentType) super.findWithAttribute(JPATestDesignTemplateContentType.class, String.class,"name", name);
		return JPAEntityUtil.copy(jpaEntity, TestDesignTemplateContentType.class);
	}
	
	
	
	//TestDesignTemplateContentSubType
	@Override 
	public List<TestDesignTemplateContentSubType> findAllContentSubTypes(int start, int end) throws ApplicationException {
		List<TestDesignTemplateContentSubType> result = new ArrayList<>();
		try {
			List<JPABaseEntity> resultList = super.findAll(JPATestDesignTemplateContentSubType.class,start,end);
			result = JPAEntityUtil.copy(resultList, TestDesignTemplateContentSubType.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return result;		
	}
	
	@Override
	public long countAllContentSubTypes() throws ApplicationException {
		long res = 0;
		try {
			res = super.countAll(JPATestDesignTemplateContentSubType.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}	
	
	@Override
	public TestDesignTemplateContentSubType getContentSubTypeByPrimary(Long pk) throws ApplicationException, NoSuchModelException {
		JPATestDesignTemplateContentSubType jpaEntity = (JPATestDesignTemplateContentSubType) super.findByPrimaryKey(JPATestDesignTemplateContentSubType.class, pk);
		return JPAEntityUtil.copy(jpaEntity, TestDesignTemplateContentSubType.class);
	}
	
	@Override 
	public TestDesignTemplateContentSubType provideContentSubType(TestDesignTemplateContentSubType record) throws ApplicationException {
		TestDesignTemplateContentSubType entity = getTestDesignTemplateContentSubTypeByCode(record.getCode());
		if (Validator.isNull(entity))
		{
			JPABaseEntity res = super.add(JPAEntityUtil.copy(record, JPATestDesignTemplateContentSubType.class));
			entity = JPAEntityUtil.copy(res, TestDesignTemplateContentSubType.class);
		}

		return entity;	
	}
	
	public TestDesignTemplateContentSubType updateTestDesignTemplateContentSubType(JPATestDesignTemplateContentSubType record) throws ApplicationException {
		JPABaseEntity res = super.update(JPAEntityUtil.copy(record, JPATestDesignTemplateContentSubType.class));
		TestDesignTemplateContentSubType dto = JPAEntityUtil.copy(res, TestDesignTemplateContentSubType.class);
		return dto;	
	}	
	
	@Override
	public TestDesignTemplateContentSubType getTestDesignTemplateContentSubTypeByCode(String code) throws ApplicationException{
		JPATestDesignTemplateContentSubType jpaEntity = (JPATestDesignTemplateContentSubType) super.findWithAttribute(JPATestDesignTemplateContentSubType.class, String.class,"code", code);
		return JPAEntityUtil.copy(jpaEntity, TestDesignTemplateContentSubType.class);		
	}
	
	public TestDesignTemplateContentSubType getTestDesignTemplateContentSubTypeByName(String name) throws ApplicationException{
		JPATestDesignTemplateContentSubType jpaEntity = (JPATestDesignTemplateContentSubType) super.findWithAttribute(JPATestDesignTemplateContentSubType.class, String.class,"name", name);
		return JPAEntityUtil.copy(jpaEntity, TestDesignTemplateContentSubType.class);		
	}
	
	public TestDesignTemplateContentSubType deleteTestDesignTemplateContentSubType(Long id) throws ApplicationException, NoSuchModelException {
		JPATestDesignTemplateContentSubType jpaEntity = (JPATestDesignTemplateContentSubType) super.findByPrimaryKey(JPATestDesignTemplateContentSubType.class, id);
		super.remove(jpaEntity);
		return JPAEntityUtil.copy(jpaEntity, TestDesignTemplateContentSubType.class);
	}

	@Override
	public void createDefaults() throws ApplicationException, Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TestDesignTemplateContentSubType updatContentSubTypee(TestDesignTemplateContentSubType record)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TestDesignTemplateContentSubType deleteContentSubType(Long id)
			throws ApplicationException, NoSuchModelException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TestDesignTemplateContentSubType getContentSubTypeByCode(String code) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TestDesignTemplateContentSubType getContentSubTypeByName(String name) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}
}