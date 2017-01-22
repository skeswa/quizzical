package org.quizzical.backend.analytics.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.amdatu.jta.Transactional;
import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.core.commons.util.Validator;
import org.gauntlet.core.commons.util.jpa.JPAEntityUtil;
import org.gauntlet.core.model.JPABaseEntity;
import org.gauntlet.core.service.impl.BaseServiceImpl;
import org.osgi.service.log.LogService;
import org.quizzical.backend.analytics.api.dao.ITestUserAnalyticsDAOService;
import org.quizzical.backend.analytics.api.model.TestCategoryAttempt;
import org.quizzical.backend.analytics.api.model.TestCategoryRating;
import org.quizzical.backend.analytics.api.model.TestUserAnalytics;
import org.quizzical.backend.analytics.model.jpa.JPATestCategoryAttempt;
import org.quizzical.backend.analytics.model.jpa.JPATestCategoryRating;
import org.quizzical.backend.analytics.model.jpa.JPATestUserAnalytics;


@SuppressWarnings("restriction")
@Transactional
public class TestUserAnalyticsDAOImpl extends BaseServiceImpl implements ITestUserAnalyticsDAOService {
	//TestDesignTemplates
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
	public List<TestUserAnalytics> findAll(int start, int end) throws ApplicationException {
		List<TestUserAnalytics> result = new ArrayList<>();
		try {
			List<JPABaseEntity> resultList = super.findAll(JPATestUserAnalytics.class,start,end);
			result = JPAEntityUtil.copy(resultList, TestUserAnalytics.class);
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
			res = super.countAll(JPATestUserAnalytics.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}	
	
	@Override
	public TestUserAnalytics getByPrimary(Long pk) throws ApplicationException, NoSuchModelException {
		JPATestUserAnalytics jpaEntity = (JPATestUserAnalytics) super.findByPrimaryKey(JPATestUserAnalytics.class, pk);
		return JPAEntityUtil.copy(jpaEntity, TestUserAnalytics.class);
	}

	@Override
	public TestUserAnalytics provide(TestUserAnalytics record)
			  throws ApplicationException {
		TestUserAnalytics existingTestDesignTemplate = getByCode(record.getCode());
		if (Validator.isNull(existingTestDesignTemplate))
		{
			JPATestUserAnalytics td = null;//fromDTO(record);
			JPABaseEntity res = super.add(td);
			existingTestDesignTemplate = JPAEntityUtil.copy(res, TestUserAnalytics.class);
		}

		return existingTestDesignTemplate;
	}
	

	@Override
	public TestUserAnalytics update(TestUserAnalytics record) throws ApplicationException {
		JPABaseEntity res = super.update(JPAEntityUtil.copy(record, JPATestUserAnalytics.class));
		TestUserAnalytics dto = JPAEntityUtil.copy(res, TestUserAnalytics.class);
		return dto;	
	}	
	
	@Override
	public TestUserAnalytics delete(Long id) throws ApplicationException, NoSuchModelException {
		JPATestUserAnalytics jpaEntity = (JPATestUserAnalytics) super.findByPrimaryKey(JPATestUserAnalytics.class, id);
		super.remove(jpaEntity);
		return JPAEntityUtil.copy(jpaEntity, TestUserAnalytics.class);
	}
	
	@Override
	public TestUserAnalytics getByCode(String code) throws ApplicationException {
		JPATestUserAnalytics jpaEntity = (JPATestUserAnalytics) super.findWithAttribute(JPATestUserAnalytics.class, String.class,"code", code);
		return JPAEntityUtil.copy(jpaEntity, TestUserAnalytics.class);
	}


	@Override
	public TestUserAnalytics getByName(String name) throws ApplicationException {
		JPATestUserAnalytics jpaEntity = (JPATestUserAnalytics) super.findWithAttribute(JPATestUserAnalytics.class, String.class,"name", name);
		return JPAEntityUtil.copy(jpaEntity, TestUserAnalytics.class);
	}

	@Override
	public void createDefaults() throws ApplicationException, Exception {
	}
}