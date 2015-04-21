package guiview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

/**
 * Subclass of JFrame that writes back on close before exiting,
 * For session persistence
 * 
 * @author Ananth Rao
 * @author Allon Fineziber
 *
 */
public class MainFrame extends JFrame {
	private MainApp parent;
	
	public MainFrame(MainApp parent) {
		super();
		this.parent = parent;
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				exitProcedure();
			}
		});
	}
	
	public MainFrame(String name) {
		super(name);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				exitProcedure();
			}
		});
	}

	/**
	 * Exit function to be called on close of mainframe window before exiting
	 * Saves all user data
	 */
	public void exitProcedure() {
		parent.logout();
		this.dispose();
		System.exit(0);
	}
}
