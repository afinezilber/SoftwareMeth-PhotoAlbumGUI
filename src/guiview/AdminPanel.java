package guiview;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.*;
import javax.swing.plaf.ListUI;

import cs213.photoAlbum.control.*;
import cs213.photoAlbum.util.DuplicateUserException;

/**
 * Admin display screen, shows users and allows administrative operations
 * 
 * @author Ananth Rao
 * @author Allon Fineziber
 *
 */
public class AdminPanel extends JPanel implements ActionListener {
	
	public MainApp parent;
	public JButton delete;
	
	public JButton add;
	public JTextField username;
	public JTextField userID;
	
	public JList<String> userList;
	public DefaultListModel<String> listModel;
	
	public JButton logout;
	
	public StatusBar status;
	
	public AdminPanel(MainApp parent) {
		super();
		//this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setLayout(new BorderLayout());
		this.parent = parent;
		setupPanels();
	}
	
	/**
	 * Central method for setting up all parts of the UI in this screen
	 * 
	 * @author Ananth Rao
	 * @author Allon Fineziber
	 * 
	 * 
	 */
	public void setupPanels() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(createListPanel(), BorderLayout.CENTER);
		panel.add(createAddPanel(), BorderLayout.SOUTH);
		panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		this.add(panel, BorderLayout.CENTER);
		
		status = new StatusBar();
		JPanel statusBar = new JPanel(new BorderLayout());
		statusBar.setBorder(BorderFactory.createLoweredSoftBevelBorder());
		statusBar.add(status, BorderLayout.WEST);
		this.add(statusBar, BorderLayout.SOUTH);
	}
	
	/**
	 * Populates list of users
	 * 
	 * @author Ananth Rao
	 * @author Allon Fineziber
	 * 
	 */
	public void setUserList() {
		listModel.clear();
		for(String userID : parent.control.listUsers()) {
			listModel.addElement(userID);
		}
	}
	
	/**
	 * Creates UI elements of list area
	 * 
	 * @author Ananth Rao
	 * @author Allon Fineziber
	 * 
	 * @return
	 */
	public JPanel createListPanel() {
		JPanel listPanel = new JPanel(new GridLayout(1,1));
		listModel = new DefaultListModel<String>();
		setUserList();
		
		//Setting up the list and listmodel
		userList = new JList<String>(listModel);
		userList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		userList.setSelectedIndex(-1);
		userList.setLayoutOrientation(JList.VERTICAL);
		userList.setVisibleRowCount(1);
		
		//Adding a scrollpane and doing some styling
		JScrollPane scrollUsers = new JScrollPane(userList);
		listPanel.add(scrollUsers);
		listPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10),
				BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Users"),
						BorderFactory.createEmptyBorder(10, 10, 10, 10))));
		
		//Creating the delete button
		delete = new JButton("Delete");
		delete.addActionListener(this);
		
		//Creating the logout button
		logout = new JButton("Logout");
		logout.addActionListener(this);
		
		//Adding a sidemenu with the delete and logout buttons
		JPanel sideMenu = new JPanel(new BorderLayout());
		sideMenu.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
		sideMenu.add(delete, BorderLayout.NORTH);
		sideMenu.add(new JPanel(), BorderLayout.CENTER);
		sideMenu.add(logout, BorderLayout.SOUTH);
		
		//Create the main panel and add all sub components
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(listPanel, BorderLayout.CENTER);
		mainPanel.add(sideMenu, BorderLayout.EAST);
		
		return mainPanel;
	}
	
	/**
	 * Creates UI elements of area for adding new user
	 * 
	 * @author Ananth Rao
	 * @author Allon Fineziber
	 * 
	 * @return
	 */
	public JPanel createAddPanel() {
		JPanel inputPanel = new JPanel();
		JLabel usernameLabel = new JLabel("Username:");
		username = new JTextField(10);
		JLabel userIDLabel = new JLabel("UserID:");
		userID = new JTextField(10);
		
		inputPanel.add(usernameLabel);
		inputPanel.add(username);
		inputPanel.add(userIDLabel);
		inputPanel.add(userID);
		
		add = new JButton("Add User");
		add.addActionListener(this);
		JPanel buttonHolder = new JPanel();
		buttonHolder.add(add);
		
		JPanel addPanel = new JPanel();
		addPanel.add(inputPanel, BorderLayout.CENTER);
		addPanel.add(buttonHolder, BorderLayout.EAST);
		
		return addPanel;
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		
		if(source == delete) {
			if(userList.getSelectedIndex() == -1) {
				error("No user was selected for deletion");
			} else {
				String userID = userList.getSelectedValue();
				String[] options = {"Delete","Cancel"};
				int returnVal = JOptionPane.showOptionDialog(this, "Are you sure you want to delete user " + userID + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
				if(returnVal ==  0) {
					try {
						parent.deleteUser(userID);
						setUserList();
						message("User " + userID + " was deleted");
					} catch (Exception e1) {
						error("No user was selected for deletion");
					}
				}
			}
		} else if(source == logout) {
			parent.logout();
		} else if(source == add) {
			String username = this.username.getText();
			String userID = this.userID.getText();
			if(username.equals("") || userID.equals("")) {
				String fields = "";
				if(username.equals("")) {
					fields += "Username";
				}
				if(userID.equals("")) {
					if(!fields.equals("")) {
						fields += " and ";
					}
					fields += "UserID";
					
				}
				error(fields + " cannot be empty.");
			} else if(userID.equals("admin")) {
				error("Sorry, admin is a reserved user. Select a different username.");
			} else {
				try {
					parent.addUser(username, userID);
					setUserList();
					message("User " + userID + " was added with username " + username);
				} catch (DuplicateUserException e1) {
					error("User " + userID + " already exists. Try using a different ID");
				}				
			}
		}
	}
	
	/**
	 * Utility for putting messages to the statusbar
	 * @param message
	 */
	public void message(String message) {
		status.setForeground(new Color(0, 0, 0));
		status.setMessage(message);
	}

	/**
	 * Utility for putting error to the statusbar
	 * @param message
	 */
	public void error(String message) {
		status.setForeground(new Color(200, 0, 0));
		status.setMessage(message);
	}
}
