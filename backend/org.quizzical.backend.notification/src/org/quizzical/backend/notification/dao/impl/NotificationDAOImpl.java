package org.quizzical.backend.notification.dao.impl;

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
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.osgi.service.log.LogService;
import org.quizzical.backend.notification.api.dao.INotificationDAOService;
import org.quizzical.backend.notification.api.model.SubscriberEmailNotification;
import org.quizzical.backend.notification.api.model.SubscriberSMSNotification;
import org.quizzical.backend.notification.model.jpa.JPASubscriberEmailNotification;
import org.quizzical.backend.notification.model.jpa.JPASubscriberSMSNotification;


@SuppressWarnings("restriction")
@Transactional
public class NotificationDAOImpl extends BaseServiceImpl implements INotificationDAOService, EventHandler {
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
	public List<SubscriberEmailNotification> findAllSubscriberEmailNotification(int start, int end) throws ApplicationException {
		List<SubscriberEmailNotification> result = new ArrayList<>();
		try {
			List<JPABaseEntity> resultList = super.findAll(JPASubscriberEmailNotification.class,start,end);
			result = JPAEntityUtil.copy(resultList, SubscriberEmailNotification.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return result;		
	}
	

	@Override
	public long countAllSubscriberEmailNotification() throws ApplicationException {
		long res = 0;
		try {
			res = super.countAll(JPASubscriberEmailNotification.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}	
	
	//Email
	@Override
	public SubscriberEmailNotification getSubscriberEmailNotificationByPrimary(Long pk) throws ApplicationException, NoSuchModelException {
		JPASubscriberEmailNotification jpaEntity = (JPASubscriberEmailNotification) super.findByPrimaryKey(JPASubscriberEmailNotification.class, pk);
		return JPAEntityUtil.copy(jpaEntity, SubscriberEmailNotification.class);
	}

	@Override
	public SubscriberEmailNotification provideSubscriberEmailNotification(SubscriberEmailNotification record)
			  throws ApplicationException {
		SubscriberEmailNotification existingTestDesignTemplate = getSubscriberEmailNotificationByCode(record.getCode());
		if (Validator.isNull(existingTestDesignTemplate))
		{
			JPASubscriberEmailNotification td = JPAEntityUtil.copy(record, JPASubscriberEmailNotification.class);
			JPABaseEntity res = super.add(td);
			existingTestDesignTemplate = JPAEntityUtil.copy(res, SubscriberEmailNotification.class);
		}

		return existingTestDesignTemplate;
	}
	

	@Override
	public SubscriberEmailNotification updateSubscriberEmailNotification(SubscriberEmailNotification record) throws ApplicationException {
		JPABaseEntity res = super.update(JPAEntityUtil.copy(record, JPASubscriberEmailNotification.class));
		SubscriberEmailNotification dto = JPAEntityUtil.copy(res, SubscriberEmailNotification.class);
		return dto;	
	}	
	
	@Override
	public SubscriberEmailNotification deleteSubscriberEmailNotification(Long id) throws ApplicationException, NoSuchModelException {
		JPASubscriberEmailNotification jpaEntity = (JPASubscriberEmailNotification) super.findByPrimaryKey(JPASubscriberEmailNotification.class, id);
		super.remove(jpaEntity);
		return JPAEntityUtil.copy(jpaEntity, SubscriberEmailNotification.class);
	}
	
	@Override
	public SubscriberEmailNotification getSubscriberEmailNotificationByCode(String code) throws ApplicationException {
		JPASubscriberEmailNotification jpaEntity = getByCode_(code);
		return JPAEntityUtil.copy(jpaEntity, SubscriberEmailNotification.class);
	}

	private JPASubscriberEmailNotification getByCode_(String code) throws ApplicationException {
		return  (JPASubscriberEmailNotification) super.findWithAttribute(JPASubscriberEmailNotification.class, String.class,"code", code);
	}
	
	//SMS
	@Override
	public List<SubscriberSMSNotification> findAllSubscriberSMSNotification(int start, int end) throws ApplicationException {
		List<SubscriberSMSNotification> result = new ArrayList<>();
		try {
			List<JPABaseEntity> resultList = super.findAll(JPASubscriberSMSNotification.class,start,end);
			result = JPAEntityUtil.copy(resultList, SubscriberSMSNotification.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return result;		
	}
	

	@Override
	public long countAllSubscriberSMSNotification() throws ApplicationException {
		long res = 0;
		try {
			res = super.countAll(JPASubscriberSMSNotification.class);
		}
		catch (Exception e) {
			throw processException(e);
		}
		return res;
	}	
	
	@Override
	public SubscriberSMSNotification getSubscriberSMSNotificationByPrimary(Long pk) throws ApplicationException, NoSuchModelException {
		JPASubscriberSMSNotification jpaEntity = (JPASubscriberSMSNotification) super.findByPrimaryKey(JPASubscriberSMSNotification.class, pk);
		return JPAEntityUtil.copy(jpaEntity, SubscriberSMSNotification.class);
	}

	@Override
	public SubscriberSMSNotification provideSubscriberSMSNotification(SubscriberSMSNotification record)
			  throws ApplicationException {
		SubscriberSMSNotification existingTestDesignTemplate = getSubscriberSMSNotificationByCode(record.getCode());
		if (Validator.isNull(existingTestDesignTemplate))
		{
			JPASubscriberSMSNotification td = JPAEntityUtil.copy(record, JPASubscriberSMSNotification.class);
			JPABaseEntity res = super.add(td);
			existingTestDesignTemplate = JPAEntityUtil.copy(res, SubscriberSMSNotification.class);
		}

		return existingTestDesignTemplate;
	}
	

	@Override
	public SubscriberSMSNotification updateSubscriberSMSNotification(SubscriberSMSNotification record) throws ApplicationException {
		JPABaseEntity res = super.update(JPAEntityUtil.copy(record, JPASubscriberSMSNotification.class));
		SubscriberSMSNotification dto = JPAEntityUtil.copy(res, SubscriberSMSNotification.class);
		return dto;	
	}	
	
	@Override
	public SubscriberSMSNotification deleteSubscriberSMSNotification(Long id) throws ApplicationException, NoSuchModelException {
		JPASubscriberSMSNotification jpaEntity = (JPASubscriberSMSNotification) super.findByPrimaryKey(JPASubscriberSMSNotification.class, id);
		super.remove(jpaEntity);
		return JPAEntityUtil.copy(jpaEntity, SubscriberSMSNotification.class);
	}
	
	@Override
	public SubscriberSMSNotification getSubscriberSMSNotificationByCode(String code) throws ApplicationException {
		JPASubscriberSMSNotification jpaEntity = (JPASubscriberSMSNotification) super.findWithAttribute(JPASubscriberSMSNotification.class, String.class,"code", code);
		return JPAEntityUtil.copy(jpaEntity, SubscriberSMSNotification.class);
	}


	@Override
	public void createDefaults() throws ApplicationException, Exception {
	}
	
	//-- Misc
	@Override
	public void truncate() throws ApplicationException {
		super.truncate("gnt_ntfy_event");
	}

	@Override
	public void handleEvent(Event event) {
        String userId = (String) event.getProperty(EVENT_TOPIC_PROP_USERID);
        String type = (String) event.getProperty(EVENT_TOPIC_PROP_NOTIFICATION_TYPE);
        try {
        	if (EVENT_TOPIC_PROP_NOTIFICATION_TYPE_EMAIL.equalsIgnoreCase(type)) {
        	}
        	else {
        	}
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}