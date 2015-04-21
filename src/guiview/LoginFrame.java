package guiview;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * Login screen for application.
 * 
 * @author Ananth Rao
 * @author Allon Fineziber
 *
 */
public class LoginFrame extends JFrame implements ActionListener {
	
	private MainApp parent;
	private JTextField userID;
	private JButton login;
	private JButton quit;
	private StatusBar status;
	
	public LoginFrame(MainApp parent) {
		super();
		this.parent = parent;
	}

	public LoginFrame(String name, MainApp parent) {
		super(name);
		this.parent = parent;
	}

	public void showGUI() {

        JPanel loginPanel = new JPanel(new BorderLayout());
        loginPanel.setOpaque(true);
        
        userID = new JTextField(20);
        loginPanel.add(userID, BorderLayout.NORTH);
        
        login = new JButton("Login");
        login.addActionListener(this);

        quit = new JButton("Quit");
        quit.addActionListener(this);
                
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(login);
        buttonPanel.add(quit);
        
        loginPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        panel.add(loginPanel, gbc);
        //Set the menu bar and add the label to the content pane.
        this.getContentPane().setLayout(new BorderLayout());
		status = new StatusBar();
		JPanel statusBar = new JPanel(new BorderLayout());
		statusBar.setBorder(BorderFactory.createLoweredSoftBevelBorder());
		statusBar.add(status, BorderLayout.WEST);
		this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        this.getContentPane().add(panel, BorderLayout.CENTER);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		if(source == login) {
			String user = userID.getText();
			if(user.isEmpty()) {
				error("userID cannot be empty.");
			} else {
				if(user.equalsIgnoreCase("admin")) {
					parent.adminMode();
				} else {
					try {
						parent.userMode(user);
						message("");
					} catch(Exception exp) {
						error("Sorry, user " + user + " does not exist");
					}
				}
			}
		}
		else if(source == quit) {
			System.exit(0);
		}
	}
	
	/**
	 * Utility for putting messages to the statusbar
	 * @param message
	 */
	public void error(String message) {
		status.setForeground(parent.red);
		status.setText(message);
	}
	
	/**
	 * Utility for putting errors to the statusbar
	 * @param message
	 */	
	public void message(String message) {
		status.setForeground(parent.black);
		status.setText(message);
	}

}
