package net.stepnext.timetracking;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
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
			while(true) {
				try {
					Thread.sleep(30000);
					TimeTrackerApp.this.update();
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
			@Override
			public void mouseDown(MouseEvent e) {
				doClick(e);
			}

			protected void doClick(MouseEvent e) {
				switch(e.button) {
				case 1:
					System.out.println("left click");
					showProjectSelector();
				case 3:
					System.out.println("right click");
					controller.toggle();
				}
				controller.getCurrentTask();
			}
	}

	protected Shell shell;
	protected TimeTrackerController controller = new TimeTrackerController();
	private Label lblClock;

	/**
	 * Launch the application.
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

	public void update() {
		long time = controller.getTime();
		int hours = (int) (time / 60);
		int minutes = (int) (time % 60);
		lblClock.setText(hours + " : " + minutes);
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
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
		shell.setSize(315, 96);
		shell.setText("SWT Application");
		shell.setLayout(new GridLayout(1, false));
		
		shell.addMouseListener(new MouseAdapter());
		
		lblClock = new Label(shell, SWT.NONE);
		lblClock.setFont(SWTResourceManager.getFont("Lucida Grande", 44, SWT.BOLD));
		lblClock.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		lblClock.setEnabled(false);
		lblClock.setAlignment(SWT.CENTER);
		lblClock.setText("000 : 00");
		lblClock.addMouseListener(new MouseAdapter());

	}

	protected void showProjectSelector() {
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
		for (String task : controller.getAllTasks()) {
			(new TableItem(table, SWT.NONE)).setText(task);
		}
	}
}
