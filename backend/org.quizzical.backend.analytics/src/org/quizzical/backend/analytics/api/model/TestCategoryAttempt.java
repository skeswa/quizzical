package org.quizzical.backend.analytics.api.model;

import java.io.Serializable;
import java.util.Date;

import org.gauntlet.core.model.BaseEntity;

public class TestCategoryAttempt extends BaseEntity implements Serializable {
	private Long testProblemId;
	private Date dateAttempted;
}
