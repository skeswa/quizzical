package org.quizzical.backend.security.service.impl.tokenstorage;


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
import org.amdatu.security.tokenprovider.Token;
import org.amdatu.security.tokenprovider.TokenStorageProvider;
import org.apache.felix.dm.annotation.api.ServiceDependency;
import org.gauntlet.core.commons.util.jpa.JPAEntityUtil;
import org.osgi.service.log.LogService;
import org.quizzical.backend.security.api.tokenstorage.IExpiredTokenPurger;
import org.quizzical.backend.security.model.jpa.tokenstorage.JPAStoredToken;

@Transactional
public class JPATokenStorageProvider implements TokenStorageProvider, IExpiredTokenPurger {
	
	@ServiceDependency(required = false)
	private volatile LogService logger;
	
	@ServiceDependency(required = true, filter="("+org.osgi.service.jpa.EntityManagerFactoryBuilder.JPA_UNIT_NAME+"=idm)")
	private volatile EntityManager em;
	
	public LogService getLogger() {
		return logger;
	}

	public void setLogger(LogService logger) {
		this.logger = logger;
	}

	public EntityManager getEm() {
		return em;
	}	
	
	@Override
	public void addToken(Token token) {
		final JPAStoredToken tokenToStore = JPAEntityUtil.copy(token, JPAStoredToken.class);
		getEm().persist(tokenToStore);
	}

	@Override
	public Token getToken(String token) {
		Token rec = null;

		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<JPAStoredToken> query = builder.createQuery(JPAStoredToken.class);
			Root<JPAStoredToken> rootEntity = query.from(JPAStoredToken.class);
			ParameterExpression<String> p = builder.parameter(String.class);
			query.select(rootEntity).where(builder.equal(rootEntity.get("m_token"), p));

			TypedQuery<JPAStoredToken> typedQuery = getEm().createQuery(query);
			typedQuery.setParameter(p,token);

			rec = JPAEntityUtil.copy(typedQuery.getSingleResult(),Token.class);
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
	
	public JPAStoredToken getStoredToken(String token) {
		JPAStoredToken rec = null;

		try {
			CriteriaBuilder builder = getEm().getCriteriaBuilder();
			CriteriaQuery<JPAStoredToken> query = builder.createQuery(JPAStoredToken.class);
			Root<JPAStoredToken> rootEntity = query.from(JPAStoredToken.class);
			ParameterExpression<String> p = builder.parameter(String.class);
			query.select(rootEntity).where(builder.equal(rootEntity.get("m_token"), p));

			TypedQuery<JPAStoredToken> typedQuery = getEm().createQuery(query);
			typedQuery.setParameter(p,token);

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
	public boolean hasToken(String token) {
		return getToken(token) != null;
	}

	@Override
	public void removeToken(Token token) {
		JPAStoredToken storedToken = (JPAStoredToken)getStoredToken(token.getToken());
		getEm().remove(storedToken);
	}

    @Override
    public void updateToken(Token token) {
        
    }

	@Override
	public int purgeTokensIssuedBefore(long timestamp) {
		TypedQuery<Token> q = getEm().createQuery("select o from JPAStoredToken o WHERE o.m_timestamp < :timestamp",Token.class);
		q.setParameter("timestamp", timestamp);
		
		
		List<Token> tokens = q.getResultList();
		int count = tokens.size();
		
		for (Token tkn : tokens) {
			getEm().remove(tkn);
		}
		return count;
	}

}

