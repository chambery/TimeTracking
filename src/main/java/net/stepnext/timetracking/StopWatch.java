
package net.stepnext.timetracking;

public class StopWatch {
	public static final int NOT_RUNNING = 0;
	public static final int RUNNING = 1;
	long startTime = 0l;
	int state = NOT_RUNNING;
	long lastTiming = 0;
	
	public void start() {
		startTime = System.currentTimeMillis();
		state = RUNNING;
	}
	
	public long stop() {
		lastTiming = getTime();
		state = NOT_RUNNING;
		return lastTiming;
	}
	
	public long getTime() {
		return System.currentTimeMillis() - startTime;
	}
	
	public int getState() {
		return state;
	}
}