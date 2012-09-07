package net.stepnext.timetracking;

import java.util.Collection;

public class TimeTrackerController {
	TimeTracker tracker = new TimeTracker();
	String currentTask;
	StopWatch stopWatch = new StopWatch();
	
	public String getCurrentTask() {
		return currentTask;
	}
	
	public void setCurrentTask(String name) {
		this.currentTask = name;
		stopWatch.reset();
	}

	public long getTime() {
		return tracker.getTaskTime(currentTask);
	}

	public void toggle() {
		switch (stopWatch.getState()) {
		case StopWatch.STATE_RUNNING:
			stopWatch.suspend();			
			break;
			
		case StopWatch.STATE_SUSPENDED:
			stopWatch.resume();
			break;

		case StopWatch.STATE_STOPPED:
			stopWatch.start();
			break;
		}
	}
	
	public Collection<String> getTasks() {
		return tracker.getTasks();
	}
}
