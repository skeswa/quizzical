package org.quizzical.backend.scheduler.api;

import org.quartz.SchedulerException;

public interface IScheduler {
	void pauseAll() throws SchedulerException;
	Boolean isStarted() throws SchedulerException;
	void resumeAll() throws SchedulerException;
	void shutdown() throws SchedulerException;
	void clear() throws SchedulerException;
}
