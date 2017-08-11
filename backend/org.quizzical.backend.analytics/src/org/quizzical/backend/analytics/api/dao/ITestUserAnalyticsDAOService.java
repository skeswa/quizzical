package org.quizzical.backend.analytics.api.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.quizzical.backend.analytics.api.model.TestCategoryRating;
import org.quizzical.backend.analytics.api.model.TestUserAnalytics;
import org.quizzical.backend.analytics.model.jpa.JPATestCategoryRating;
import org.quizzical.backend.security.authorization.api.model.user.User;

public interface ITestUserAnalyticsDAOService {
	//
	List<TestUserAnalytics> findAll(int start, int end) throws ApplicationException;

	long countAll() throws ApplicationException;

	TestUserAnalytics getByPrimary(Long pk) throws ApplicationException, NoSuchModelException;

	TestUserAnalytics provide(TestUserAnalytics record) throws ApplicationException;

	TestUserAnalytics update(TestUserAnalytics record) throws ApplicationException;

	TestUserAnalytics delete(Long id) throws ApplicationException, NoSuchModelException;
	
	TestCategoryRating deleteRating(Long id) throws ApplicationException, NoSuchModelException;

	TestUserAnalytics getByCode(String code) throws ApplicationException;

	TestUserAnalytics getByName(String name) throws ApplicationException;

	void updateRatings(String tuaCode, Map<Long, TestCategoryRating> newCategoryRatingsMap) throws ApplicationException;

	List<TestCategoryRating> findWeakestCategories(User user, Integer categoryLimit) throws ApplicationException;

	public List<TestCategoryRating> findWeakestCategories(final User user, final Collection onlyInNames, final Integer categoryLimit) throws ApplicationException;
	
	List<TestCategoryRating> findWeakestCategoriesLowerThanRating(User user, Collection<String> catNames, Integer startRatingCutoffIncl,
			Integer endRatingCutoffIncl) throws ApplicationException;

	void truncate() throws ApplicationException;

	//--
	public TestCategoryRating getCategoryRatingByName(Long analyticsId, String code) throws ApplicationException;
	
	public List<TestCategoryRating> getCategoryRatingsByName(Long analyticsId, String name) throws ApplicationException;
}
