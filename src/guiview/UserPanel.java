package guiview;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ListUI;

import cs213.photoAlbum.control.*;
import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.util.AlbumException;
import cs213.photoAlbum.util.DuplicateUserException;
import cs213.photoAlbum.util.PhotoException;

/**
 * Top level user screen.
 * Shows list of album and allows top level interactions like creating and deleting albums, renaming albums, etc.
 * @author Ananth Rao
 * @author Allon Fineziber
 *
 */
public class UserPanel extends JPanel implements ActionListener, ListSelectionListener {
	public MainApp parent;

	public JButton deleteAlbumBtn, renameAlbumBtn, openAlbumBtn, searchPhotosBtn, logoutBtn;

	public JButton addAlbumBtn;
	public JTextField albumNameText;
	
	public JList<Album> folders;
	public DefaultListModel<Album> listModel;
	
	public JPanel albumInfoPanel;
	public JLabel albumNameLabel;
	public JLabel albumNumPhotosLabel;
	public JLabel albumDateRangeLabel;

	public StatusBar status;
	
	public Color red = new Color(200, 0, 0);
	public Color black = new Color(0, 0, 0);
	
/**
 * Constructor
 * 
 * @author Ananth Rao
 * @author Allon Fineziber
 * 
 * 
 * @param parent
 */
	
	public UserPanel(MainApp parent) {
		super();
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
		panel.add(createMainPanel(), BorderLayout.CENTER);
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
 * Sets the list for display in the JList of albums
 * 
 * @author Ananth Rao
 * @author Allon Fineziber
 * 
 * 
 */
	public void setAlbumList() {
		listModel.clear();
		for(Album album : parent.control.listAlbums()) {
			listModel.addElement(album);
			
		}
	}
	
/**
 * Creates UI for area with list of albums and returns the JPanel associated with it
 * 
 * @author Ananth Rao
 * @author Allon Fineziber
 * 
 * 
 * @return JPanel
 */
	public JPanel createListPanel() {
		JPanel listPanel = new JPanel(new GridLayout(1,1));
		listModel = new DefaultListModel<Album>();
		setAlbumList();
		
		//Setting up the list and listmodel
		folders = new JList<Album>(listModel);
		folders.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		folders.setSelectedIndex(-1);
		folders.setLayoutOrientation(JList.VERTICAL);
		folders.setVisibleRowCount(1);
		folders.setCellRenderer(new AlbumRenderer());
		folders.addListSelectionListener(this);
		
		//Adding a scrollpane and doing some styling
		JScrollPane scrollUsers = new JScrollPane(folders);
		listPanel.add(scrollUsers);
		listPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10),
				BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Albums"),
						BorderFactory.createEmptyBorder(10, 10, 10, 10))));
		return listPanel;
	}

	/**
	 * Creates UI for area showing album information, returns JPanel associated with that area
	 *
	 * @author Ananth Rao
	 * @author Allon Fineziber
	 * 
	 * @return JPanel
	 */
	public JPanel createInfoDisplayPanel() {
		albumInfoPanel = new JPanel(new BorderLayout());
		albumInfoPanel.setBorder(BorderFactory.createTitledBorder("Current Album"));
		
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(0,1));

		JLabel nameLabel = new JLabel("Album name: ");
		nameLabel.setAlignmentX(LEFT_ALIGNMENT);
		albumNameLabel = new JLabel();
		albumNameLabel.setAlignmentX(LEFT_ALIGNMENT);
		JPanel namePanel = new JPanel();
		namePanel.add(nameLabel);
		namePanel.add(albumNameLabel);
		infoPanel.add(namePanel);
		
		JLabel numLabel = new JLabel("Number of photos: ");
		albumNumPhotosLabel = new JLabel();
		JPanel numPanel = new JPanel();
		numPanel.add(numLabel);
		numPanel.add(albumNumPhotosLabel);
		infoPanel.add(numPanel);
		
		JLabel rangeLabel = new JLabel("Date range: ");
		albumDateRangeLabel = new JLabel();
		JPanel rangePanel = new JPanel();
		rangePanel.add(rangeLabel);
		rangePanel.add(albumDateRangeLabel);
		infoPanel.add(rangePanel);
		
		albumInfoPanel.add(new JScrollPane(infoPanel), BorderLayout.CENTER);
		albumInfoPanel.setVisible(false);
		
		return albumInfoPanel;
	}
	
	/**
	 * Creates and return the JPanel associated with the side menu area of the screen
	 * 
	 * @return JPanel
	 */
	public JPanel createSideMenu() {
		openAlbumBtn = new JButton("Open Album");
		openAlbumBtn.addActionListener(this);
		openAlbumBtn.setEnabled(false);
		
		renameAlbumBtn = new JButton("Rename Album");
		renameAlbumBtn.addActionListener(this);
		renameAlbumBtn.setEnabled(false);

		deleteAlbumBtn = new JButton("Delete Album");
		deleteAlbumBtn.addActionListener(this);
		deleteAlbumBtn.setEnabled(false);

		JPanel top = new JPanel(new GridLayout(0,1));
		top.add(openAlbumBtn);
		top.add(renameAlbumBtn);
		top.add(deleteAlbumBtn);
		
		searchPhotosBtn = new JButton("Search for Photos");
		searchPhotosBtn.addActionListener(this);

		JPanel middle = new JPanel(new GridBagLayout());
		middle.add(searchPhotosBtn, new GridBagConstraints());

		logoutBtn = new JButton("Logout");
		logoutBtn.addActionListener(this);

		//filler
		//Adding a sidemenu with the delete and logout buttons
		JPanel sideMenu = new JPanel(new BorderLayout());
		sideMenu.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
		
		sideMenu.add(top, BorderLayout.NORTH);		
		sideMenu.add(middle, BorderLayout.CENTER);
		sideMenu.add(logoutBtn, BorderLayout.SOUTH);
		
		return sideMenu;
	}
	
	/**
	 * Creates and returns the JPanel associated with the main area of the screen
	 * @return JPanel
	 */
	public JPanel createMainPanel() {
		
		//Create the main panel and add all sub components
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(createInfoDisplayPanel(), BorderLayout.NORTH);
		mainPanel.add(createListPanel(), BorderLayout.CENTER);
		mainPanel.add(createSideMenu(), BorderLayout.EAST);
		
		return mainPanel;
	}
	
	/**
	 * Creates and returns the JPanel associated with the area on the screen where the user adds new albums
	 * 
	 * @return JPanel
	 */
	public JPanel createAddPanel() {
		JPanel inputPanel = new JPanel();
		JLabel albumNameLabel = new JLabel("Album name");
		albumNameText = new JTextField(10);
		
		inputPanel.add(albumNameLabel);
		inputPanel.add(albumNameText);
	
		addAlbumBtn = new JButton("Add Album");
		addAlbumBtn.addActionListener(this);
		JPanel buttonHolder = new JPanel();
		buttonHolder.add(addAlbumBtn);
		
		JPanel addPanel = new JPanel();
		addPanel.add(inputPanel, BorderLayout.CENTER);
		addPanel.add(buttonHolder, BorderLayout.EAST);
		
		return addPanel;
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		if(source == openAlbumBtn) {
			if(folders.getSelectedIndex() != -1) {
				Album album = folders.getSelectedValue();
				String albumName = album.getName();
				message("");
				folders.clearSelection();
				albumInfoPanel.setVisible(false);
				parent.showPhotos(albumName);
			} else {
				error("No album selected for opening.");
			}
		} else if(source == deleteAlbumBtn) {
			if(folders.getSelectedIndex() != -1) {
				Album album = folders.getSelectedValue();
				String albumName = album.getName();
				String[] options = {"Delete","Cancel"};
				int returnVal = JOptionPane.showOptionDialog(this, "Are you sure you want to delete album " + albumName + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
				if(returnVal ==  0) {
					try {
						parent.deleteAlbum(albumName);
						setAlbumList();
						message(albumName + " was deleted from the system.");
					} catch(Exception exp) {
						error(albumName + " is not an existing album in the system.");
					}					
				}
			} else {
				error("No album selected for deletion.");
			}
		} else if(source == searchPhotosBtn) {
			parent.showSearch();
		} else if(source == logoutBtn) {
			parent.logout();
		} else if(source == addAlbumBtn) {
			String albumName = this.albumNameText.getText();
			if(albumName.equals("")) {
				error("The new album's name cannot be empty.");
			} else {
				try {
					parent.addAlbum(albumName);
					setAlbumList();
					message("Album " + albumName + " was added to the system.");
				} catch (AlbumException e1) {
					error("Sorry, the album " + albumName + " already exists. Try using a different name.");
				}
			}
		} else if(source == renameAlbumBtn) {
			if(folders.getSelectedIndex() != -1) {
				Album album = folders.getSelectedValue();
				String oldAlbumName = album.getName();
				
				String albumName = (String)JOptionPane.showInputDialog(this, "What is the new name of the album?", "Rename Album", JOptionPane.QUESTION_MESSAGE, null, null, null);
				if(albumName != null) {
					if(albumName.equals("")) {
						error("The new album's name cannot be empty.");
					} else {
						try {
							parent.renameAlbum(oldAlbumName, albumName);
							setAlbumList();
							message("Album " + oldAlbumName + " was renamed to " + albumName);
						} catch (AlbumException e1) {
							error("Sorry, the album " + albumName + " already exists. Try using a different name.");
						} catch (PhotoException e1) {
							error(e1.getMessage());
						}
					}
				}
			}
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

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == folders) {
			if(folders.getSelectedIndex() != -1) {
				Album currAlbum = folders.getSelectedValue();
				albumNameLabel.setText(currAlbum.getName());
				albumNumPhotosLabel.setText(currAlbum.numPhotos + "");

				LinkedList<Photo> photos = new LinkedList<Photo>(currAlbum.listPhotos());
				Collections.sort(photos);
				if(!photos.isEmpty()) {
					albumDateRangeLabel.setText(parent.formatDate(photos.getFirst().getDate().getTime()) + " - " + parent.formatDate(photos.getLast().getDate().getTime()));								
				} else {
					albumDateRangeLabel.setText("");
				}
				
				albumInfoPanel.setVisible(true);
				openAlbumBtn.setEnabled(true);
				deleteAlbumBtn.setEnabled(true);
				renameAlbumBtn.setEnabled(true);
			} else {
				albumInfoPanel.setVisible(false);
				openAlbumBtn.setEnabled(false);
				deleteAlbumBtn.setEnabled(false);
				renameAlbumBtn.setEnabled(false);
			}
		}
	}
}
