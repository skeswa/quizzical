package org.quizzical.backend.security.tokenstorage.jpa;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.amdatu.security.tokenprovider.Token;

public class JPAStoredToken extends Token {
    @Id
    @Column(name = "id", scale = 0)
    @GeneratedValue(strategy = GenerationType.AUTO)	
    protected Long id;

	public JPAStoredToken() {
		super(null, null, 0);
	}
	
	public JPAStoredToken(Token token) {
		super(token.getToken(), token.getTokenSecret(), token.getTimestamp());
		this.setProperties(token.getProperties());
	}
	
	public JPAStoredToken(String token, String tokenSecret, long timestamp) {
		super(token, tokenSecret, timestamp);
	}

	public Long getId() {
		return id;
	}
}
