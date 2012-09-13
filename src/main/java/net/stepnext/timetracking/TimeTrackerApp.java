package net.stepnext.timetracking;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

public class TimeTrackerApp {
	class DisplayUpdater implements Runnable {

		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(20000);
					System.out.println("TimeTrackerApp.DisplayUpdater.run()");

					display.asyncExec(new Runnable() {

						@Override
						public void run() {
							TimeTrackerApp.this.update();
						}
					});
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	class MouseAdapter extends org.eclipse.swt.events.MouseAdapter {
		@Override
		public void mouseUp(MouseEvent e) {
			doClick(e);
		}

		protected void doClick(MouseEvent e) {
			switch (e.button) {
			case 1:
				System.out.println("left click");
				try {
					controller.toggle();
				} catch (NoTaskDefinedException e1) {
					showProjectSelector();
				}
				break;
			case 3:
				System.out.println("right click");
				showProjectSelector();
				break;
			}
			update();
		}
	}

	protected Shell shell;
	protected TimeTrackerController controller = new TimeTrackerController();
	private Label lblClock;
	Display display;
	private Label lblCurrentTask;
	
	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TimeTrackerApp window = new TimeTrackerApp();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		display = Display.getDefault();
		createContents();
		new Thread(new DisplayUpdater()).start();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(315, 97);
		shell.setText("SWT Application");
		shell.setLayout(new GridLayout(2, false));

		shell.addMouseListener(new MouseAdapter());

		lblClock = new Label(shell, SWT.NONE);
		lblClock.setFont(SWTResourceManager.getFont("Lucida Grande", 44, SWT.BOLD));
		lblClock.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		lblClock.setEnabled(false);
		lblClock.setAlignment(SWT.CENTER);
		lblClock.setText("000 : 00");
//		lblClock.addMouseListener(new MouseAdapter());

		/* filler */
		Label filler = new Label(shell, SWT.NONE);
		filler.setFont(SWTResourceManager.getFont("Lucida Grande", 8, SWT.NORMAL));
		filler.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 1, 1));

		lblCurrentTask = new Label(shell, SWT.NONE);
		lblCurrentTask.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		shell.pack();
		update();
	}

	protected void showProjectSelector() {
		Collection<String> tasks = controller.getTaskNames();
		if (tasks.size() == 0) {
			InputDialog newTaskDialog = new InputDialog(shell, "New Task", "Enter Task Name", null, null);
			if (newTaskDialog.open() == InputDialog.OK) {
				controller.setCurrentTask(newTaskDialog.getValue());
			}
		}
		Shell selector = new Shell(this.shell, SWT.APPLICATION_MODAL);
		final Table table = new Table(selector, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				String task = table.getItem(new Point(e.x, e.y)).getText();
				controller.setCurrentTask(task);
			}
		});
		(new TableItem(table, SWT.NONE)).setText("New Task...");
		for (String task : tasks) {
			(new TableItem(table, SWT.NONE)).setText(task);
		}
		shell.open();
	}

	public void update() {
		long time = controller.getTime();
		String displayTime = String.format("%03d : %02d", TimeUnit.MILLISECONDS.toHours(time),
				TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)));
		System.out.println("updating: " + time + " -> " + displayTime);
		lblClock.setText(displayTime);
		
		if(controller.stopWatch.state == StopWatch.NOT_RUNNING) {
			lblClock.setForeground(display.getSystemColor(SWT.COLOR_GRAY));
			System.out.println("stopped");
		}
		else {
			lblClock.setForeground(display.getSystemColor(SWT.COLOR_BLACK));						
			System.out.println("started");
		}
		
		lblCurrentTask.setText(controller.getCurrentTask());
	}
}
