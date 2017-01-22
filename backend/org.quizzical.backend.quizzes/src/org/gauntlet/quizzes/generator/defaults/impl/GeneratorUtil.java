package org.gauntlet.quizzes.generator.defaults.impl;

import java.util.concurrent.ThreadLocalRandom;

import org.quizzical.backend.testdesign.api.model.TestDesignTemplateItemDifficultyType;

public class GeneratorUtil {
	static public long generateRandowOffset(long count) {
		if (count == 0)
			return count;
		return ThreadLocalRandom.current().nextLong(0, count);
	}

	static public String getDifficultyCode(TestDesignTemplateItemDifficultyType difficultyType) {
		if (difficultyType.equals(TestDesignTemplateItemDifficultyType.EASY))
			return "Easy";
		else if (difficultyType.equals(TestDesignTemplateItemDifficultyType.MEDIUM))
			return "Medium";
		else
			return "Hard";
	}
}
