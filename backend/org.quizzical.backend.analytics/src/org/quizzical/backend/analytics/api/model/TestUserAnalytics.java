package org.quizzical.backend.analytics.api.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.gauntlet.core.api.ApplicationException;
import org.gauntlet.core.api.dao.NoSuchModelException;
import org.gauntlet.core.model.BaseEntity;
import org.quizzical.backend.security.api.model.user.User;

public class TestUserAnalytics extends BaseEntity implements Serializable {
	
	private Long userId;

	private List<TestCategoryRating> ratings;
	
	public TestUserAnalytics() {
	}
	
	public TestUserAnalytics(final Long userId, final String code, final String name) {
		super.setCode(code);
		super.setName(name);
		this.userId = userId;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<TestCategoryRating> getRatings() {
		if (ratings == null)
			ratings = new ArrayList<>();
		return ratings;
	}

	public void setRatings(List<TestCategoryRating> ratings) {
		this.ratings = ratings;
	}

	public void addRating(TestCategoryRating rating) {
		getRatings().add(rating);
	}
	public List<TestCategoryRating> getOrderedSections() {
		List<TestCategoryRating> orderedSections = getRatings();
		Collections.sort(orderedSections, new Comparator<TestCategoryRating>() {
			@Override
			public int compare(TestCategoryRating o1, TestCategoryRating o2) {
				if  (o1.getRating() < o2.getRating())
					return -1;
				else if (o1.getRating() > o2.getRating())
					return  1;
				else 
					return 0;//they must be the same
			}
		});
		return orderedSections;
	}
}