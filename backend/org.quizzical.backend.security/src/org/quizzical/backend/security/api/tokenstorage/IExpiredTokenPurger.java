package org.quizzical.backend.security.api.tokenstorage;

public interface IExpiredTokenPurger {

	int purgeTokensIssuedBefore(long timestamp);

}
