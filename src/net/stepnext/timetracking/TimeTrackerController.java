package net.stepnext.timetracking;

public class TimeTrackerController {
	TimeTracker tracker = new TimeTracker();
	String currentTask;
	StopWatch stopWatch = new StopWatch();
	
	public String getCurrentTask() {
		return currentTask;
	}
	
	public void setCurrentTask(String name) {
		this.currentTask = name;
	}

	public long getTime() {
		return tracker.getTaskTime(currentTask);
	}

	public void toggle() {
		if(stopWatch.isRunning()) {
			stopWatch.suspend();
		} else {
			stopWatch.resume();
		}
	}
}
