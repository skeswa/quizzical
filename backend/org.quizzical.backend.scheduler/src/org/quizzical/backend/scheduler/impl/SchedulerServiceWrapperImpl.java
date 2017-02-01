package org.quizzical.backend.scheduler.impl;

import org.osgi.service.log.LogService;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quizzical.backend.scheduler.api.IScheduler;

public class SchedulerServiceWrapperImpl implements IScheduler {
	private volatile LogService logger;
	
	private volatile Scheduler scheduler;

	@Override
	public void pauseAll() throws SchedulerException {
		scheduler.pauseAll();
	}

	@Override
	public Boolean isStarted() throws SchedulerException {
		return scheduler.isStarted();
	}

	@Override
	public void resumeAll() throws SchedulerException {
		scheduler.resumeAll();
	}

	@Override
	public void shutdown() throws SchedulerException {
		scheduler.shutdown();
	}

	@Override
	public void clear() throws SchedulerException {
		scheduler.clear();
	}
	
}
