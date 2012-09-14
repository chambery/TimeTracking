package net.stepnext.timetracking;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
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
	private Menu menu;
	
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
		shell.setText("Time Tracker");
		shell.setLayout(new GridLayout(2, false));

		lblClock = new Label(shell, SWT.NONE);
		lblClock.setFont(SWTResourceManager.getFont("Lucida Grande", 44, SWT.BOLD));
		lblClock.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		lblClock.setEnabled(false);
		lblClock.setAlignment(SWT.CENTER);
		lblClock.setText("000 : 00");
		new Label(shell, SWT.NONE);

		lblCurrentTask = new Label(shell, SWT.RIGHT);
		lblCurrentTask.setFont(SWTResourceManager.getFont("Lucida Grande", 10, SWT.NORMAL));
		lblCurrentTask.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		menu = new Menu (shell, SWT.POP_UP);
		shell.pack();
		update();
	}

	protected void showProjectSelector() {
		Collection<String> tasks = controller.getTaskNames();
		if (tasks.size() == 0) {
			showNewTaskDialog();
		}

		MenuItem addNew = new MenuItem(menu, SWT.NONE);
		addNew.setText("New Task...");
		addNew.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				showNewTaskDialog();
			}			
		});
		for (final String name : tasks) {
			MenuItem item = new MenuItem (menu, SWT.PUSH);
			item.setText(name);
			item.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					controller.setCurrentTask(name);
					update();
				}
			});
		}
		shell.setMenu(menu);
	}

	private void showNewTaskDialog() {
		InputDialog newTaskDialog = new InputDialog(shell, "New Task", "Enter Task Name", null, null);
		if (newTaskDialog.open() == InputDialog.OK) {
			controller.setCurrentTask(newTaskDialog.getValue());
		}
		update();
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
		
		lblCurrentTask.setText(controller.getCurrentTask() == null ? "" : controller.getCurrentTask());
		shell.layout();
	}
}
