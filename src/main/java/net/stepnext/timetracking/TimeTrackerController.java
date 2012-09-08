package net.stepnext.timetracking;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;

import com.google.gson.Gson;

public class TimeTrackerController {
	TimeTracker tracker = new TimeTracker();
	String currentTask;
	StopWatch stopWatch = new StopWatch();
	
	public String getCurrentTask() {
		return currentTask;
	}
	
	public void setCurrentTask(String name) {
		this.currentTask = name;
		if(tracker.getTaskNames().contains(name) == false) {
			tracker.addTask(name);
		}
		stopWatch.reset();
		stopWatch.start();
	}

	public long getTime() {
		if(currentTask == null) {
			return 0l;
		}
		return tracker.getTaskTime(currentTask);
	}

	public void toggle() throws NoTaskDefinedException {
		if(currentTask == null) {
			throw new NoTaskDefinedException();
		}
		
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
	
	public Map<String, Long> getTasks() {
		return tracker.getTasks();
	}
	
	class DisplayUpdater implements Runnable {

		@Override
		public void run() {
			while(true) {
				try {
					Thread.sleep(20000);
					TimeTrackerController.this.update();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Collection<String> getTaskNames() {
		return tracker.getTaskNames();
	}
	
	public void update() {
		String tasks = new Gson().toJson(tracker.getTasks());
		
		try {
			(new PrintWriter("tasks.json")).write(tasks);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void load() {
		tracker.setTasks(new Gson().fromJson(currentTask, Map.class));
	}
}
