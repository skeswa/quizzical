package org.quizzical.backend.security.authorization.api.tokenstorage;

public interface IExpiredTokenPurger {

	int purgeTokensIssuedBefore(long timestamp);

}
