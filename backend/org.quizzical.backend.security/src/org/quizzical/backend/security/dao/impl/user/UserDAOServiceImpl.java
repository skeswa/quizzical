package org.quizzical.backend.security.dao.impl.user;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.amdatu.jta.Transactional;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.Init;
import org.apache.felix.dm.annotation.api.ServiceDependency;
import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.commons.util.Validator;
import org.gauntlet.core.service.impl.BaseServiceImpl;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.quizzical.backend.security.api.dao.user.IUserDAOService;
import org.quizzical.backend.security.api.dao.user.UserExistsByEmailException;
import org.quizzical.backend.security.api.dao.user.UserNotFoundException;
import org.quizzical.backend.security.api.model.user.User;


@Transactional
@Component(provides = {IUserDAOService.class})
public class UserDAOServiceImpl extends BaseServiceImpl implements IUserDAOService {
	
	@ServiceDependency(required = false)
	private volatile LogService logger;
	
	@ServiceDependency(required = true, filter="("+org.osgi.service.jpa.EntityManagerFactoryBuilder.JPA_UNIT_NAME+"=idm)")
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
	public void delete(String recordId) {
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
		User existingUser = getByEmail(record.getEmailAddress());
		if (Validator.isNull(existingUser))
		{
			existingUser = add(record);
		}
		else
			throw new UserExistsByEmailException(record.getEmailAddress());
		return existingUser;
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
	public User getByCode(String code) {
		User rec = null;

		try {
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<User> query = builder.createQuery(User.class);
			Root<User> rootEntity = query.from(User.class);
			ParameterExpression<String> p = builder.parameter(String.class);
			query.select(rootEntity).where(builder.equal(rootEntity.get("code"), p));

			TypedQuery<User> typedQuery = em.createQuery(query);
			typedQuery.setParameter(p, code);

			rec = typedQuery.getSingleResult();
		} catch (NoResultException e) {
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.log(LogService.LOG_ERROR,stacktrace);
		} catch (Error e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.log(LogService.LOG_ERROR,stacktrace);
		}

		return rec;
	}
	
	public User getByEmailAndPassword(String email, String encryptedPassword) {
		User rec = null;
		try {
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<User> query = builder.createQuery(User.class);
			Root<User> rootEntity = query.from(User.class);
			ParameterExpression<String> p1 = builder.parameter(String.class);
			ParameterExpression<String> p2 = builder.parameter(String.class);
			query.select(rootEntity).where(builder.and(builder.equal(rootEntity.get("emailAddress"),p1),builder.equal(rootEntity.get("password"), p2)));

			TypedQuery<User> typedQuery = em.createQuery(query);
			typedQuery.setParameter(p1, email);
			typedQuery.setParameter(p2, encryptedPassword);
			
			rec = typedQuery.getSingleResult();
		} catch (NoResultException e) {
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.log(LogService.LOG_ERROR,stacktrace);
		} catch (Error e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.log(LogService.LOG_ERROR,stacktrace);
		}

		return rec;
	}
	
	
	@Override
	public User getByEmail(String email) {
		User rec = null;
		try {
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<User> query = builder.createQuery(User.class);
			Root<User> rootEntity = query.from(User.class);
			ParameterExpression<String> p = builder.parameter(String.class);
			query.select(rootEntity).where(builder.equal(rootEntity.get("emailAddress"),p));

			TypedQuery<User> typedQuery = em.createQuery(query);
			typedQuery.setParameter(p, email);
			
			rec = typedQuery.getSingleResult();
		} catch (NoResultException e) {
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.log(LogService.LOG_ERROR,stacktrace);
		} catch (Error e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
			logger.log(LogService.LOG_ERROR,stacktrace);
		}

		return rec;
	}
	
	
	@Override
	public User getUserByEmail(String emailAddress) throws UserNotFoundException {
        User findOne = getByEmail(emailAddress);
        if(findOne == null) {
        	throw new UserNotFoundException(emailAddress);
        }
		return findOne;
	}

	
	@Override
	public User getUserByEmailAndPassword(String email, String password) throws UserNotFoundException {
		String hash = DigestUtils.sha256Hex(password);
		User findOne = getByEmailAndPassword(email, hash);
        if(findOne == null) {
        	throw new UserNotFoundException(email);
        }
		return findOne;
	}

	@Override
	@Init
	public void createDefaults() throws ApplicationException {
		User user = new User();
		user.setCode("mk");
		user.setName("Mandi");
		user.setEmailAddress("mandisakeswa999@gmail.com");
		add(user);
	}
	
}