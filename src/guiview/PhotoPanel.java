package guiview;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.model.Tag;
import cs213.photoAlbum.util.AlbumException;
import cs213.photoAlbum.util.PhotoException;

/**
 * Individual photo screen.
 * Can add tags, remove tags, and recaption photo.
 * 
 * @author Ananth Rao
 * @author Allon Fineziber
 *
 */
public class PhotoPanel extends JPanel implements ActionListener, ListSelectionListener {
	public MainApp parent;
	public Photo photo;
	public String albumName;
	
	public JButton recaptionBtn, backBtn;
	public JLabel display;
	
	public JTextField typeInput, valueInput;
	public JButton addTagBtn;
	
	public JButton removeTagBtn;
	public JList<Tag> tags;
	public DefaultListModel<Tag> listModel;
	
	public JLabel nameLabel, captionLabel, dateLabel, albumLabel;
	
	public StatusBar status;
	
	public PhotoPanel(MainApp parent) {
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
		panel.add(createMenuPanel(), BorderLayout.EAST);
		panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		this.add(panel, BorderLayout.CENTER);
		
		status = new StatusBar();
		JPanel statusBar = new JPanel(new BorderLayout());
		statusBar.setBorder(BorderFactory.createLoweredSoftBevelBorder());
		statusBar.add(status, BorderLayout.WEST);
		this.add(statusBar, BorderLayout.SOUTH);
	}
	
	public JPanel createMainPanel() {
		
		//Create the main panel and add all sub components
		display = new JLabel();
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(display, BorderLayout.CENTER);
		mainPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		
		return mainPanel;
	}
	
	public JPanel createListPanel() {
		JPanel listPanel = new JPanel(new GridLayout(1,1));
		listModel = new DefaultListModel<Tag>();
		setTagList();
		
		//Setting up the list and listmodel
		tags = new JList<Tag>(listModel);
		tags.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		tags.setSelectedIndex(-1);
		tags.setLayoutOrientation(JList.VERTICAL);
		tags.setVisibleRowCount(1);
		tags.setCellRenderer(new TagRenderer());
		tags.addListSelectionListener(this);
		
		//Adding a scrollpane and doing some styling
		JScrollPane scrollUsers = new JScrollPane(tags);
		listPanel.add(scrollUsers);
		listPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10),
				BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Tags"),
						BorderFactory.createEmptyBorder(10, 10, 10, 10))));
		return listPanel;
	}

	public JPanel createMenuPanel() {
		JPanel infoPanel = new JPanel(new GridLayout(0,1));
		
		nameLabel = new JLabel();
		JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		namePanel.add(new JLabel("Filename: "));
		namePanel.add(nameLabel);
		infoPanel.add(namePanel);
		
		captionLabel = new JLabel();
		JPanel captionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		captionPanel.add(new JLabel("Caption: "));
		captionPanel.add(captionLabel);
		infoPanel.add(captionPanel);
		
		dateLabel = new JLabel();
		JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		datePanel.add(new JLabel("Date taken: "));
		datePanel.add(dateLabel);
		infoPanel.add(datePanel);
		
		albumLabel = new JLabel();
		JPanel albumPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		albumPanel.add(new JLabel("In albums: "));
		albumPanel.add(albumLabel);
		infoPanel.add(albumPanel);

		removeTagBtn = new JButton("Delete Tag");
		removeTagBtn.addActionListener(this);
		
		recaptionBtn = new JButton("Change Caption");
		recaptionBtn.addActionListener(this);
		
		JPanel buttonsPanel = new JPanel(new GridLayout(0,1));
		buttonsPanel.add(recaptionBtn);
		buttonsPanel.add(removeTagBtn);
		
		
		typeInput = new JTextField(10);
		JPanel typePanel = new JPanel(new BorderLayout());
		typePanel.add(new JLabel("Type: "), BorderLayout.WEST);
		typePanel.add(typeInput, BorderLayout.EAST);
		
		valueInput = new JTextField(10);
		JPanel valuePanel = new JPanel(new BorderLayout());
		valuePanel.add(new JLabel("Value: "), BorderLayout.WEST);
		valuePanel.add(valueInput, BorderLayout.EAST);
		
		JPanel inputPanel = new JPanel(new GridLayout(0,1));
		inputPanel.add(typePanel);
		inputPanel.add(valuePanel);
		
		addTagBtn = new JButton("Add Tag");
		addTagBtn.addActionListener(this);
		
		JPanel addTagPanel = new JPanel(new BorderLayout());
		addTagPanel.add(inputPanel, BorderLayout.NORTH);
		addTagPanel.add(addTagBtn, BorderLayout.SOUTH);
		
		JPanel middle = new JPanel(new BorderLayout());
		middle.add(buttonsPanel, BorderLayout.NORTH);
		middle.add(createListPanel(), BorderLayout.CENTER);
		middle.add(addTagPanel, BorderLayout.SOUTH);
		
		backBtn = new JButton("Back to Album");
		backBtn.addActionListener(this);
		
		JPanel menuPanel = new JPanel(new BorderLayout());
		menuPanel.add(infoPanel, BorderLayout.NORTH);
		menuPanel.add(middle, BorderLayout.CENTER);
		menuPanel.add(backBtn, BorderLayout.SOUTH);
		menuPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10),
				BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Photo Info"),
						BorderFactory.createEmptyBorder(10, 10, 10, 10))));
		
		return menuPanel;
	}
	
	public void setTagList() {
		listModel.clear();
		if(photo != null) {
			for(Tag tag : photo.listTags()) {
				listModel.addElement(tag);
			}
		}
	}
	
	public void display() {
		if(photo != null) {
			String name = photo.getName();
			int nameLength = name.length();
			if(nameLength > 20) {
				nameLabel.setText(name.substring(0, 10) + "..." + name.substring(nameLength - 10, nameLength));
			} else {
				nameLabel.setText(name);
			}
			captionLabel.setText(photo.getCaption());
			dateLabel.setText(parent.formatDate(photo.getDate().getTime()));
			String albums = "";
			for(String alb : photo.albums) {
				albums += alb + "\n";
			}
			albumLabel.setText(albums);
			
			setTagList();
			ImageIcon icon = new ImageIcon(photo.getName());
			Image img = icon.getImage().getScaledInstance(display.getParent().getWidth(), display.getParent().getHeight(), Image.SCALE_FAST);
			display.setIcon(new ImageIcon(img));
		}
	}
	
	public void setPhotoAndAlbum(String fileName, String albumName) {
		photo = parent.getPhoto(fileName);
		this.albumName = albumName;
		
		display();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		if(source == backBtn) {
			parent.showPhotos(albumName);
		} else if(source == recaptionBtn) {
			String caption = (String)JOptionPane.showInputDialog(this, "What is the new caption of the photo\n" + photo.getName() + "?\n\n The previous caption was \n" + photo.getCaption(), "Recaption Photo", JOptionPane.QUESTION_MESSAGE, null, null, null);
			if(caption != null) {
				if(caption.equals("")) {
					error("The new caption cannot be empty.");
				} else {
					try {
						parent.recaptionPhoto(photo.getName(), caption);
						captionLabel.setText(photo.getCaption());
						message("Photo " + photo.getName() + " now has caption " + caption);
					} catch (PhotoException e1) {
						error(e1.getMessage());
					}
				}
			}
		} else if(source == addTagBtn) {
/*			JPanel mainPanel = new JPanel(new BorderLayout());
			JPanel inputPanel = new JPanel(new GridLayout(2,1));
			JTextField type = new JTextField(20);
			JPanel typePanel = new JPanel(new BorderLayout());
			typePanel.add(new JLabel("Type: "), BorderLayout.WEST);
			typePanel.add(type, BorderLayout.EAST);
			JTextField value = new JTextField(20);
			JPanel valuePanel = new JPanel(new BorderLayout());
			valuePanel.add(new JLabel("Value: "), BorderLayout.WEST);
			valuePanel.add(value, BorderLayout.EAST);
			inputPanel.add(typePanel);
			inputPanel.add(valuePanel);
			mainPanel.setBorder(BorderFactory.createTitledBorder("New Tag"));
			mainPanel.add(inputPanel, BorderLayout.CENTER);
			
			JLabel exitStatus = new JLabel();
			exitStatus.setVisible(false);
			mainPanel.add(exitStatus, BorderLayout.SOUTH);
			
			JOptionPane tag = new JOptionPane();
			tag.setMessage(mainPanel);
			tag.setMessageType(JOptionPane.PLAIN_MESSAGE);
			Object[] options = new Object[2];
			options[0] = "Add tag";
			options[1] = "Cancel";
			tag.setOptions(options);
			
			JDialog dialog = tag.createDialog(this, "Add New Tag");
			dialog.pack();
			dialog.setVisible(true);
*/
			String type = typeInput.getText();
			String value = valueInput.getText();
			
			if(type.isEmpty()) {
				error("Tag has to have a type");
				return;
			}
			if(value.isEmpty()) {
				error("Tag has to have a value");
				return;
			}
			Tag newTag = new Tag(type, value);
			try {
				parent.addTag(photo.getName(), newTag);
				setTagList();
			} catch(Exception e1) {
				error(e1.getMessage());
			}
		} else if(source == removeTagBtn) {
			if(tags.getSelectedIndex() != -1) {
				Tag tag = tags.getSelectedValue();
				try {
					parent.removeTag(photo.getName(), tag);
					setTagList();
					message("Tag " + tag.getType() + ":" + tag.getValue() + " was removed from the photo.");
				} catch(Exception exp) {
					error("Tag " + tag.getType() + ":" + tag.getValue() + " is not an existing tag of the photo.");
				}					
				}
			} else {
				error("No tag selected for removal.");
			}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
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