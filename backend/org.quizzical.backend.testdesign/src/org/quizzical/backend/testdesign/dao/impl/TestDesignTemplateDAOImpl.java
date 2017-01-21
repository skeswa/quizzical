package org.quizzical.backend.testdesign.dao.impl;

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
import org.quizzical.backend.testdesign.api.dao.ITestDesignTemplateDAOService;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplate;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateItem;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateItemDifficultyType;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateSection;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateSectionType;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplateType;
import org.quizzical.backend.testdesign.model.jpa.JPATestDesignTemplate;
import org.quizzical.backend.testdesign.model.jpa.JPATestDesignTemplateContentSubType;
import org.quizzical.backend.testdesign.model.jpa.JPATestDesignTemplateItem;
import org.quizzical.backend.testdesign.model.jpa.JPATestDesignTemplateSection;
import org.quizzical.backend.testdesign.model.jpa.JPATestDesignTemplateType;


@SuppressWarnings("restriction")
@Transactional
public class TestDesignTemplateDAOImpl extends BaseServiceImpl implements ITestDesignTemplateDAOService {
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
	public List<TestDesignTemplate> findAll(int start, int end) throws ApplicationException {
		List<TestDesignTemplate> result = new ArrayList<>();
		try {
			List<JPABaseEntity> resultList = super.findAll(JPATestDesignTemplate.class,start,end);
			result = JPAEntityUtil.copy(resultList, TestDesignTemplate.class);
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
			res = super.countAll(JPATestDesignTemplate.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}	
	
	@Override
	public TestDesignTemplate getByPrimary(Long pk) throws ApplicationException, NoSuchModelException {
		JPATestDesignTemplate jpaEntity = (JPATestDesignTemplate) super.findByPrimaryKey(JPATestDesignTemplate.class, pk);
		return JPAEntityUtil.copy(jpaEntity, TestDesignTemplate.class);
	}

	@Override
	public TestDesignTemplate provide(TestDesignTemplate record)
			  throws ApplicationException {
		TestDesignTemplate existingTestDesignTemplate = getByCode(record.getCode());
		if (Validator.isNull(existingTestDesignTemplate))
		{
			JPATestDesignTemplate td = fromDTO(record);
			JPABaseEntity res = super.add(td);
			existingTestDesignTemplate = JPAEntityUtil.copy(res, TestDesignTemplate.class);
		}

		return existingTestDesignTemplate;
	}
	

	private JPATestDesignTemplate fromDTO(TestDesignTemplate record) {
		final JPATestDesignTemplate jpaEntity = JPAEntityUtil.copy(new TestDesignTemplate(record.getName(), record.getCode()), JPATestDesignTemplate.class);
    	final List<JPATestDesignTemplateSection> sections = record.getSections()
    		.parallelStream()
    		.map(section -> {
    			JPATestDesignTemplateSection jpaEntitySection = 
    					JPAEntityUtil.copy(new TestDesignTemplateSection(section.getType(),record,section.getOrdinal()), JPATestDesignTemplateSection.class);
    	    	final List<JPATestDesignTemplateItem> items = section.getItems()
    	        		.parallelStream()
    	        		.map(item -> {
    	        			JPATestDesignTemplateItem jpaEntityItem = null;
    	        			JPATestDesignTemplateContentSubType subType = null;
    	        			try {
								jpaEntityItem = 
										JPAEntityUtil.copy(new TestDesignTemplateItem(item.getName(), item.getCode(), item.getOrdinal(), item.getDifficultyType()), JPATestDesignTemplateItem.class);
								subType = getSubTypeByCode(item.getContentSubType().getCode());
								jpaEntityItem.setDifficultyType(item.getDifficultyType());
								jpaEntityItem.setContentSubType(subType);
							} catch (ApplicationException e) {
								throw new RuntimeException(String.format("JPATestDesignTemplateContentSubType(%s) not found",item.getContentSubType().getCode()));
							}
    	        			
    	        			jpaEntityItem.setSection(jpaEntitySection);
    	        			
    	    				return jpaEntityItem;
    	    	    	})
    	        		.collect(Collectors.toList());
    			
    	    	jpaEntitySection.setTemplate(jpaEntity);
    	    	jpaEntitySection.setItems(items);
    	    	
				return jpaEntitySection;
	    	})
    		.collect(Collectors.toList());
    	
    	jpaEntity.setSections(new HashSet<JPATestDesignTemplateSection>(sections));
		
		return jpaEntity;
	}	
	
	public JPATestDesignTemplateContentSubType getSubTypeByCode(String code) throws ApplicationException {
		JPATestDesignTemplateContentSubType jpaEntity = (JPATestDesignTemplateContentSubType) super.findWithAttribute(JPATestDesignTemplateContentSubType.class, String.class,"code", code);
		return JPAEntityUtil.copy(jpaEntity, JPATestDesignTemplateContentSubType.class);
	}

	@Override
	public TestDesignTemplate update(TestDesignTemplate record) throws ApplicationException {
		JPABaseEntity res = super.update(JPAEntityUtil.copy(record, JPATestDesignTemplate.class));
		TestDesignTemplate dto = JPAEntityUtil.copy(res, TestDesignTemplate.class);
		return dto;	
	}	
	
	@Override
	public TestDesignTemplate delete(Long id) throws ApplicationException, NoSuchModelException {
		JPATestDesignTemplate jpaEntity = (JPATestDesignTemplate) super.findByPrimaryKey(JPATestDesignTemplate.class, id);
		super.remove(jpaEntity);
		return JPAEntityUtil.copy(jpaEntity, TestDesignTemplate.class);
	}
	
	@Override
	public TestDesignTemplate getByCode(String code) throws ApplicationException {
		JPATestDesignTemplate jpaEntity = (JPATestDesignTemplate) super.findWithAttribute(JPATestDesignTemplate.class, String.class,"code", code);
		return JPAEntityUtil.copy(jpaEntity, TestDesignTemplate.class);
	}


	@Override
	public TestDesignTemplate getByName(String name) throws ApplicationException {
		JPATestDesignTemplate jpaEntity = (JPATestDesignTemplate) super.findWithAttribute(JPATestDesignTemplate.class, String.class,"name", name);
		return JPAEntityUtil.copy(jpaEntity, TestDesignTemplate.class);
	}
	
	@Override 
	public List<TestDesignTemplate> findByTestDesignTemplateTypeId(final Long typeId, int start, int end) throws ApplicationException {
		List<TestDesignTemplate> resultList = null;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<JPATestDesignTemplate> query = builder.createQuery(JPATestDesignTemplate.class);
			Root<JPATestDesignTemplate> rootEntity = query.from(JPATestDesignTemplate.class);
			
			ParameterExpression<Long> p = builder.parameter(Long.class);
			query.select(rootEntity).where(builder.equal(rootEntity.get("type").get("id"),p));
			query.select(rootEntity);
			
			Map<ParameterExpression,Object> pes = new HashMap<>();
			pes.put(p, typeId);
			
			resultList = findWithDynamicQueryAndParams(query,pes,start,end);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return resultList;		
	}
	
	@Override 
	public int countByTestDesignTemplateTypeId(final Long typeId) throws ApplicationException {
		int count = 0;
		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<JPATestDesignTemplate> query = builder.createQuery(JPATestDesignTemplate.class);
			Root<JPATestDesignTemplate> rootEntity = query.from(JPATestDesignTemplate.class);
			
			ParameterExpression<Long> p = builder.parameter(Long.class);
			query.select(rootEntity).where(builder.equal(rootEntity.get("type").get("id"),p));
			query.select(rootEntity);
			
			Map<ParameterExpression,Object> pes = new HashMap<>();
			pes.put(p, typeId);
			
			count = countWithDynamicQueryAndParams(query,pes);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return count;		
	}	
	
	
	//TestDesignTemplateType
	@Override 
	public List<TestDesignTemplateType> findAllTestDesignTemplateTypes(int start, int end) throws ApplicationException {
		List<TestDesignTemplateType> result = new ArrayList<>();
		try {
			List<JPABaseEntity> resultList = super.findAll(JPATestDesignTemplateType.class,start,end);
			result = JPAEntityUtil.copy(resultList, TestDesignTemplateType.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return result;		
	}
	
	@Override
	public long countAllTestDesignTemplateTypes() throws ApplicationException {
		long res = 0;
		try {
			res = super.countAll(JPATestDesignTemplateType.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}	
	
	@Override
	public TestDesignTemplateType getTestDesignTemplateTypeByPrimary(Long pk) throws ApplicationException, NoSuchModelException {
		JPATestDesignTemplateType jpaEntity = (JPATestDesignTemplateType) super.findByPrimaryKey(JPATestDesignTemplateType.class, pk);
		return JPAEntityUtil.copy(jpaEntity, TestDesignTemplateType.class);
	}
	
	@Override 
	public TestDesignTemplateType provideTestDesignTemplateType(TestDesignTemplateType record) throws ApplicationException {
		TestDesignTemplateType entity = getTestDesignTemplateTypeByCode(record.getCode());
		if (Validator.isNull(entity))
		{
			JPABaseEntity res = super.add(JPAEntityUtil.copy(record, JPATestDesignTemplateType.class));
			entity = JPAEntityUtil.copy(res, TestDesignTemplateType.class);
		}

		return entity;	
	}
	
	public TestDesignTemplateType updateTestDesignTemplateType(JPATestDesignTemplateType record) throws ApplicationException {
		JPABaseEntity res = super.update(JPAEntityUtil.copy(record, JPATestDesignTemplateType.class));
		TestDesignTemplateType dto = JPAEntityUtil.copy(res, TestDesignTemplateType.class);
		return dto;	
	}	
	
	public TestDesignTemplateType getTestDesignTemplateTypeByCode(String code) throws ApplicationException{
		JPATestDesignTemplateType jpaEntity = (JPATestDesignTemplateType) super.findWithAttribute(JPATestDesignTemplateType.class, String.class,"code", code);
		return JPAEntityUtil.copy(jpaEntity, TestDesignTemplateType.class);		
	}
	
	public TestDesignTemplateType getTestDesignTemplateTypeByName(String code) throws ApplicationException{
		JPATestDesignTemplateType jpaEntity = (JPATestDesignTemplateType) super.findWithAttribute(JPATestDesignTemplateType.class, String.class,"name", code);
		return JPAEntityUtil.copy(jpaEntity, TestDesignTemplateType.class);		
	}
	
	public TestDesignTemplateType deleteTestDesignTemplateType(Long id) throws ApplicationException, NoSuchModelException {
		JPATestDesignTemplateType jpaEntity = (JPATestDesignTemplateType) super.findByPrimaryKey(JPATestDesignTemplateType.class, id);
		super.remove(jpaEntity);
		return JPAEntityUtil.copy(jpaEntity, TestDesignTemplateType.class);
	}
		
	
	//TestDesignTemplateItem
	@Override 
	public List<TestDesignTemplateItem> findAllTestDesignTemplateCategories(int start, int end) throws ApplicationException {
		List<TestDesignTemplateItem> result = new ArrayList<>();
		try {
			List<JPABaseEntity> resultList = super.findAll(JPATestDesignTemplateItem.class,start,end);
			result = JPAEntityUtil.copy(resultList, TestDesignTemplateItem.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return result;		
	}
	
	@Override
	public long countAllTestDesignTemplateCategories() throws ApplicationException {
		long res = 0;
		try {
			res = super.countAll(JPATestDesignTemplateItem.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}	
	
	@Override
	public TestDesignTemplateItem getTestDesignTemplateItemByPrimary(Long pk) throws ApplicationException, NoSuchModelException {
		JPATestDesignTemplateItem jpaEntity = (JPATestDesignTemplateItem) super.findByPrimaryKey(JPATestDesignTemplateItem.class, pk);
		return JPAEntityUtil.copy(jpaEntity, TestDesignTemplateItem.class);
	}
	
	@Override 
	public TestDesignTemplateItem provideTestDesignTemplateItem(TestDesignTemplateItem record) throws ApplicationException {
		TestDesignTemplateItem entity = getTestDesignTemplateItemByCode(record.getCode());
		if (Validator.isNull(entity))
		{
			JPABaseEntity res = super.add(JPAEntityUtil.copy(record, JPATestDesignTemplateItem.class));
			entity = JPAEntityUtil.copy(res, TestDesignTemplateItem.class);
		}

		return entity;	
	}
	
	@Override
	public TestDesignTemplateItem updateTestDesignTemplateItem(JPATestDesignTemplateItem record) throws ApplicationException {
		JPABaseEntity res = super.update(JPAEntityUtil.copy(record, JPATestDesignTemplateItem.class));
		TestDesignTemplateItem dto = JPAEntityUtil.copy(res, TestDesignTemplateItem.class);
		return dto;	
	}	
	
	@Override
	public TestDesignTemplateItem getTestDesignTemplateItemByCode(String code) throws ApplicationException{
		JPATestDesignTemplateItem jpaEntity = (JPATestDesignTemplateItem) super.findWithAttribute(JPATestDesignTemplateItem.class, String.class,"code", code);
		return JPAEntityUtil.copy(jpaEntity, TestDesignTemplateItem.class);		
	}
	
	@Override
	public TestDesignTemplateItem getTestDesignTemplateItemByName(String code) throws ApplicationException{
		JPATestDesignTemplateItem jpaEntity = (JPATestDesignTemplateItem) super.findWithAttribute(JPATestDesignTemplateItem.class, String.class,"name", code);
		return JPAEntityUtil.copy(jpaEntity, TestDesignTemplateItem.class);		
	}
	
	@Override
	public TestDesignTemplateItem deleteTestDesignTemplateItem(Long id) throws ApplicationException, NoSuchModelException {
		JPATestDesignTemplateItem jpaEntity = (JPATestDesignTemplateItem) super.findByPrimaryKey(JPATestDesignTemplateItem.class, id);
		super.remove(jpaEntity);
		return JPAEntityUtil.copy(jpaEntity, TestDesignTemplateItem.class);
	}	
	
	//TestDesignTemplateSection
	@Override 
	public List<TestDesignTemplateSection> findAllTestDesignTemplateSections(int start, int end) throws ApplicationException {
		List<TestDesignTemplateSection> result = new ArrayList<>();
		try {
			List<JPABaseEntity> resultList = super.findAll(JPATestDesignTemplateSection.class,start,end);
			result = JPAEntityUtil.copy(resultList, TestDesignTemplateSection.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return result;		
	}
	
	@Override
	public long countAllTestDesignTemplateSections() throws ApplicationException {
		long res = 0;
		try {
			res = super.countAll(JPATestDesignTemplateSection.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}	
	
	@Override
	public TestDesignTemplateSection getTestDesignTemplateSectionByPrimary(Long pk) throws ApplicationException, NoSuchModelException {
		JPATestDesignTemplateSection jpaEntity = (JPATestDesignTemplateSection) super.findByPrimaryKey(JPATestDesignTemplateSection.class, pk);
		return JPAEntityUtil.copy(jpaEntity, TestDesignTemplateSection.class);
	}
	
	@Override 
	public TestDesignTemplateSection provideTestDesignTemplateSection(TestDesignTemplateSection record) throws ApplicationException {
		TestDesignTemplateSection entity = getTestDesignTemplateSectionByCode(record.getCode());
		if (Validator.isNull(entity))
		{
			JPABaseEntity res = super.add(JPAEntityUtil.copy(record, JPATestDesignTemplateSection.class));
			entity = JPAEntityUtil.copy(res, TestDesignTemplateSection.class);
		}

		return entity;	
	}
	
	public TestDesignTemplateSection updateTestDesignTemplateSection(JPATestDesignTemplateSection record) throws ApplicationException {
		JPABaseEntity res = super.update(JPAEntityUtil.copy(record, JPATestDesignTemplateSection.class));
		TestDesignTemplateSection dto = JPAEntityUtil.copy(res, TestDesignTemplateSection.class);
		return dto;	
	}	
	
	public TestDesignTemplateSection getTestDesignTemplateSectionByCode(String code) throws ApplicationException{
		JPATestDesignTemplateSection jpaEntity = (JPATestDesignTemplateSection) super.findWithAttribute(JPATestDesignTemplateSection.class, String.class,"code", code);
		return JPAEntityUtil.copy(jpaEntity, TestDesignTemplateSection.class);		
	}
	
	public TestDesignTemplateSection getTestDesignTemplateSectionByName(String code) throws ApplicationException{
		JPATestDesignTemplateSection jpaEntity = (JPATestDesignTemplateSection) super.findWithAttribute(JPATestDesignTemplateSection.class, String.class,"name", code);
		return JPAEntityUtil.copy(jpaEntity, TestDesignTemplateSection.class);		
	}
	
	public TestDesignTemplateSection deleteTestDesignTemplateSection(Long id) throws ApplicationException, NoSuchModelException {
		JPATestDesignTemplateSection jpaEntity = (JPATestDesignTemplateSection) super.findByPrimaryKey(JPATestDesignTemplateSection.class, id);
		super.remove(jpaEntity);
		return JPAEntityUtil.copy(jpaEntity, TestDesignTemplateSection.class);
	}

	@Override
	public void createDefaults() throws ApplicationException, Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TestDesignTemplateType provideTestDesignTemplateType(String name) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<TestDesignTemplateItem> getOrderedTestItems(final Long pk) throws ApplicationException, NoSuchModelException {
		final TestDesignTemplate td = getByPrimary(pk);
		final List<TestDesignTemplateItem> unorderedItems = new ArrayList<>();
		for (TestDesignTemplateSection sec : td.getSections()) {
			unorderedItems.addAll(sec.getItems());
		}

		Collections.sort(unorderedItems, new Comparator<TestDesignTemplateItem>() {
			@Override
			public int compare(TestDesignTemplateItem o1, TestDesignTemplateItem o2) {
				if  (((o1.getSection().getOrdinal() < o2.getSection().getOrdinal())
						|| o1.getSection().getOrdinal() == o2.getSection().getOrdinal()) && o1.getOrdinal() < o2.getOrdinal())
					return -1;
				else if (((o1.getSection().getOrdinal() > o2.getSection().getOrdinal())
						|| o1.getSection().getOrdinal() == o2.getSection().getOrdinal()) && o1.getOrdinal() > o2.getOrdinal())
					return  1;
				else 
					return 0;//they must be the same
			}
		});
		
		return unorderedItems;
	}
}