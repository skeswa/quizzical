package org.quizzical.backend.testdesign.api;

import java.util.List;

import org.gauntlet.core.api.ApplicationException;
import org.quizzical.backend.security.authorization.api.model.user.User;
import org.quizzical.backend.testdesign.api.model.TestDesignTemplate;

public interface ITestDesignTemplateGeneratorService {
	public TestDesignTemplate generateByCategoriesAndTestSize(final User user, final List<String> categoryCodes, final Integer testSize)
			throws ApplicationException;
}
