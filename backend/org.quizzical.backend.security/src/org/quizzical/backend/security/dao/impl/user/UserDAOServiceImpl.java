package org.quizzical.backend.security.dao.impl.user;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.amdatu.jta.Transactional;
import org.apache.commons.codec.digest.DigestUtils;
import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.commons.util.Validator;
import org.gauntlet.core.commons.util.jpa.JPAEntityUtil;
import org.gauntlet.core.model.JPABaseEntity;
import org.gauntlet.core.service.impl.AttrPair;
import org.gauntlet.core.service.impl.BaseServiceImpl;
import org.osgi.service.log.LogService;
import org.quizzical.backend.security.api.dao.user.IUserDAOService;
import org.quizzical.backend.security.api.dao.user.UserNotFoundException;
import org.quizzical.backend.security.api.model.user.User;
import org.quizzical.backend.security.model.jpa.user.JPAUser;


@SuppressWarnings("restriction")
@Transactional
public class UserDAOServiceImpl extends BaseServiceImpl implements IUserDAOService {
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
	public List<User> getAllActiveUsers() {
		return em.createQuery("select o from User o record where o.active = true order by o.id", User.class).getResultList();
	}
	

	
	@Override
	public User add(User record) {
		if (record.isPasswordEncrypted())
			record.setPassword(DigestUtils.sha256Hex(record.getPassword()));
		
		em.persist(record);
		em.flush();
		em.refresh(record);
		return record;
	}

	@Override
	public void delete(User record) {
		record = em.merge(record);
		em.remove(record);
	}
	
	@Override
	public void delete(String recordId) throws ApplicationException {
		User evt = getByCode(recordId);
		delete(evt);
	}	

	@Override
	public User update(User record) {
		//record.setDateLastUpdated(new Date());
		return em.merge(record);
	}

	@Override
	public User provide(User record)
			throws Exception {
		User existingProblem = getByCode(record.getCode());
		if (Validator.isNull(existingProblem))
		{
			JPABaseEntity res = super.add(JPAEntityUtil.copy(record, JPAUser.class));
			existingProblem = JPAEntityUtil.copy(res, User.class);
		}
		else {
			return update(record);
		}

		return existingProblem;
	}
	
	

	@Override
	public User get(long id) {
		return em.getReference(User.class, id);
	}

	@Override
	public List<User> getAll() {
		return em.createQuery("select o from User o order by o.id", User.class).getResultList();
	}

	@Override
	public User getByCode(String code) throws ApplicationException {
		JPAUser jpaEntity = (JPAUser) super.findWithAttribute(JPAUser.class, String.class,"code", code);
		return JPAEntityUtil.copy(jpaEntity, User.class);
	}
	
	public User getByEmailAndPassword(String email, String encryptedPassword) throws ApplicationException {
		Set<AttrPair> attrs = new HashSet<AttrPair>();
		attrs.add(new AttrPair(String.class, "emailAddress", email));
		attrs.add(new AttrPair(String.class, "encryptedPassword", encryptedPassword));
		
		JPAUser jpaEntity = (JPAUser) super.findWithAttributes(JPAUser.class, attrs);
		return JPAEntityUtil.copy(jpaEntity, User.class);
	}
	
	
	@Override
	public User getByEmail(String email) throws ApplicationException {
		JPAUser jpaEntity = (JPAUser) super.findWithAttribute(JPAUser.class, String.class,"emailAddress", email);
		return JPAEntityUtil.copy(jpaEntity, User.class);
	}
	
	
	@Override
	public User getUserByEmail(String emailAddress) throws UserNotFoundException, ApplicationException {
        User findOne = getByEmail(emailAddress);
        if(findOne == null) {
        	throw new UserNotFoundException(emailAddress);
        }
		return findOne;
	}

	
	@Override
	public User getUserByEmailAndPassword(String email, String password) throws UserNotFoundException, ApplicationException {
		String hash = DigestUtils.sha256Hex(password);
		User findOne = getByEmailAndPassword(email, hash);
        if(findOne == null) {
        	throw new UserNotFoundException(email);
        }
		return findOne;
	}

	@Override
	public void createDefaults() throws ApplicationException {
		//MK
		User user = new User();
		user.setCode("mk");
		user.setName("Mandi");
		user.setEmailAddress("mandisakeswa999@gmail.com");
		
		String hash = DigestUtils.sha256Hex("aceit");
		user.setPasswordEncrypted(true);
		user.setPassword(hash);
		
		add(user);
		
		//Test
		user = new User();
		user.setCode("tester");
		user.setName("Tester");
		user.setEmailAddress("test@me.io");
		
		hash = DigestUtils.sha256Hex("test");
		user.setPasswordEncrypted(true);
		user.setPassword(hash);
		
		add(user);
	}
	
}