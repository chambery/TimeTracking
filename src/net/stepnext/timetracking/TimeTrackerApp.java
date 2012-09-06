package net.stepnext.timetracking;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
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

	protected Shell shell;
	protected TimeTrackerController controller;
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
		
		lblClock = new Label(shell, SWT.NONE);
		lblClock.setFont(SWTResourceManager.getFont("Lucida Grande", 44, SWT.BOLD));
		lblClock.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				switch(e.button) {
				case SWT.BUTTON3:
					showProject1Selector();
				case SWT.BUTTON1:
					controller.toggle();
				}
				controller.getCurrentTask();
			}
		});
		lblClock.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		lblClock.setEnabled(false);
		lblClock.setAlignment(SWT.CENTER);
		lblClock.setText("000 : 00");

	}

	protected void showProject1Selector() {
		// TODO Auto-generated method stub
		
	}
}
