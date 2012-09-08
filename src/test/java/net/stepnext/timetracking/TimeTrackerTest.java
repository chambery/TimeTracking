package net.stepnext.timetracking;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeTrackerTest {

	@Test
	public void test() {
		TimeTrackerController controller = new TimeTrackerController();
		assertNull(controller.getCurrentTask());
		controller.setCurrentTask("foo");
		assertNotNull(controller.getCurrentTask());
		

	}

}
