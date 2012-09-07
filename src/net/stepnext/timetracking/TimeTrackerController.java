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
		if(stopWatch.isRunning()) {
			stopWatch.suspend();
		} else {
			stopWatch.start();
		}
	}
	
	public Collection<String> getAllTasks() {
		return tracker.getTasks();
	}
}
