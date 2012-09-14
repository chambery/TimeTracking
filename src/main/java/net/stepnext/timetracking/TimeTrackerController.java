package net.stepnext.timetracking;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TimeTrackerController {
	TimeTracker tracker = new TimeTracker();
	StopWatch stopWatch = new StopWatch();
	Collection<ClockUpdateListener> listeners = new ArrayList<ClockUpdateListener>();
	Gson gson; 

	public TimeTrackerController() {
		gson = new GsonBuilder().setPrettyPrinting().create();
		load();
		
	}

	public String getCurrentTask() {
		return tracker.getCurrentTask();
	}

	public void setCurrentTask(String name) {
		tracker.setCurrentTask(name);
		if (tracker.getTaskNames().contains(name) == false) {
			tracker.addTask(name);
		}
		save();
		stopWatch.start();
	}

	public long getTime() {
		System.out.println("current task: " + tracker.getCurrentTask() + (StopWatch.NOT_RUNNING == stopWatch.getState() ? "  (stopped)" : ""));
		if (tracker.getCurrentTask() == null) {
			return 0l;
		}

		return tracker.getTaskTime(tracker.getCurrentTask())
				+ (stopWatch.getState() == StopWatch.RUNNING ? (System.currentTimeMillis() - stopWatch.startTime) : 0l);
	}

	public int toggle() throws NoTaskDefinedException {
		if (tracker.getCurrentTask() == null) {
			throw new NoTaskDefinedException();
		}

		switch (stopWatch.getState()) {
		case StopWatch.RUNNING:
			long timing = stopWatch.stop();
			tracker.getTasks().put(tracker.getCurrentTask(), getTime() + timing);
			save();
			break;

		case StopWatch.NOT_RUNNING:
			stopWatch.start();
			break;
		}

		return stopWatch.state;
	}

	public Map<String, Long> getTasks() {
		return tracker.getTasks();
	}

	public Collection<String> getTaskNames() {
		return tracker.getTaskNames();
	}

	public void save() {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("tasks.json");
			writer.write(gson.toJson(tracker));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			writer.flush();
			writer.close();
		}
	}

	public void load() {
		FileReader reader = null;
		try {
			reader = new FileReader("tasks.json");
			tracker = gson.fromJson(reader, TimeTracker.class);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}	
			}
		}
	}

	public void addTask(String name) {
		if (tracker.getTaskNames().contains(name) == false) {
			tracker.addTask(name);
		}
		setCurrentTask(name);
	}
}
