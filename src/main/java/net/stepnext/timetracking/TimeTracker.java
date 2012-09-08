package net.stepnext.timetracking;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TimeTracker {
	private String recordState;
	private Map<String, Long> tasks = new HashMap<String, Long>();
	private String currentTask;
	
	public String getRecordState() {
		return recordState;
	}

	public void setRecordState(String recordState) {
		this.recordState = recordState;
	}
	
	public void addTask(String name) {
		tasks.put(name, 0l);
	}
	
	public void deleteTask(String name) {
		tasks.remove(name);
	}
	
	public long getTaskTime(String name) {
		return tasks.get(name);
	}
	
	public Collection<String> getTaskNames() {
		return tasks.keySet();
	}
	
	public Map<String, Long> getTasks() {
		return tasks;
	}

	public void setTasks(Map<String, Long> tasks) {
		this.tasks = tasks;
	}

	public String getCurrentTask() {
		return currentTask;
	}

	public void setCurrentTask(String currentTask) {
		this.currentTask = currentTask;
	}
	
}
