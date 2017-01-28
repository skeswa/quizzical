package org.quizzical.backend.contentrepository.dao.impl;

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
import org.quizzical.backend.contentrepository.api.dao.IContentItemDAOService;
import org.quizzical.backend.contentrepository.api.model.ContentItem;
import org.quizzical.backend.contentrepository.model.jpa.JPAContentItem;


@SuppressWarnings("restriction")
@Transactional
public class ContentItemDAOImpl extends BaseServiceImpl implements IContentItemDAOService {
	//ContentItems
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
	public List<ContentItem> findAll(int start, int end) throws ApplicationException {
		List<ContentItem> result = new ArrayList<>();
		try {
			List<JPABaseEntity> resultList = super.findAll(JPAContentItem.class,start,end);
			result = JPAEntityUtil.copy(resultList, ContentItem.class);
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
			res = super.countAll(JPAContentItem.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}	
	
	@Override
	public ContentItem getByPrimary(Long pk) throws ApplicationException, NoSuchModelException {
		JPAContentItem jpaEntity = (JPAContentItem) super.findByPrimaryKey(JPAContentItem.class, pk);
		return JPAEntityUtil.copy(jpaEntity, ContentItem.class);
	}
	
	@Override
	public ContentItem update(ContentItem record) throws ApplicationException {
		JPABaseEntity res = super.update(JPAEntityUtil.copy(record, JPAContentItem.class));
		ContentItem dto = JPAEntityUtil.copy(res, ContentItem.class);
		return dto;	
	}	
	
	@Override
	public ContentItem delete(Long id) throws ApplicationException, NoSuchModelException {
		JPAContentItem jpaEntity = (JPAContentItem) super.findByPrimaryKey(JPAContentItem.class, id);
		super.remove(jpaEntity);
		return JPAEntityUtil.copy(jpaEntity, ContentItem.class);
	}
	
	@Override
	public ContentItem getByCode(String code) throws ApplicationException {
		JPAContentItem jpaEntity = (JPAContentItem) super.findWithAttribute(JPAContentItem.class, String.class,"code", code);
		return JPAEntityUtil.copy(jpaEntity, ContentItem.class);
	}


	@Override
	public ContentItem getByName(String name) throws ApplicationException {
		JPAContentItem jpaEntity = (JPAContentItem) super.findWithAttribute(JPAContentItem.class, String.class,"name", name);
		return JPAEntityUtil.copy(jpaEntity, ContentItem.class);
	}
	
	@Override
	public void createDefaults() throws ApplicationException {
	}

	@Override
	public ContentItem provide(ContentItem record) throws ApplicationException {
		ContentItem recordEntity = getByCode(record.getCode());
		if (Validator.isNull(recordEntity))
		{
			JPABaseEntity res = super.add(JPAEntityUtil.copy(record, JPAContentItem.class));
			recordEntity = JPAEntityUtil.copy(res, ContentItem.class);
		}

		return recordEntity;
	}

}