package org.quizzical.backend.security.api.tokenstorage;

import org.amdatu.security.tokenprovider.Token;

public class StoredToken extends Token {
    protected Long id;

	public StoredToken() {
		super(null, null, 0);
	}
	
	public StoredToken(Token token) {
		super(token.getToken(), token.getTokenSecret(), token.getTimestamp());
		this.setProperties(token.getProperties());
	}
	
	public StoredToken(String token, String tokenSecret, long timestamp) {
		super(token, tokenSecret, timestamp);
	}

	public Long getId() {
		return id;
	}
}
