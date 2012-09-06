package net.stepnext.timetracking;

import java.util.HashMap;
import java.util.Map;

public class TimeTracker {
	private String recordState;
	private Map<String, Long> tasks = new HashMap<String, Long>();
	
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
}
