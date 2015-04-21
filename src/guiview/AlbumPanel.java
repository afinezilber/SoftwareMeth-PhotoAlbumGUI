package guiview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.util.AlbumException;
import cs213.photoAlbum.util.PhotoException;

/**
 * Album display screen, shows all photos in album and allows album level operations like recaptioning photos,
 * moving photos between albums, and deleting photos from album
 * 
 * @author Ananth Rao
 * @author Allon Fineziber
 *
 */
public class AlbumPanel extends JPanel implements ActionListener, ListSelectionListener {
	
	public MainApp parent;
	
    public String albumName = null;
	
	//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	public JButton openPhotoBtn, removePhotoBtn, recaptionBtn, movePhotoBtn, homeBtn, slideshowBtn;

	public JButton uploadBtn;
	public JLabel photoText;
	public JTextField photoCaptionText;
	public JButton addPhotoBtn;

	public JList<Photo> photos;
	public DefaultListModel<Photo> listModel;
	
	public JPanel photoInfoPanel;
	public JLabel photoNameLabel;
	public JLabel photoAlbumsLabel;
	public JLabel photoCaptionLabel;
	public JLabel photoDateLabel;
	public JLabel photoTagsLabel;

	public StatusBar status;
	
	public Color red = new Color(200, 0, 0);
	public Color black = new Color(0, 0, 0);
	
	public AlbumPanel(MainApp parent) {
		super(new BorderLayout());
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

	public void setPhotoList() {
		listModel.clear();
		if(albumName != null) {
			try {
				for(Photo photo : parent.control.listPhotos(albumName)) {
					listModel.addElement(photo);
					
				}
			} catch (AlbumException e) {
				// TODO Auto-generated catch block
				error(e.getMessage());
			}
		}
	}
	
	public JPanel createListPanel() {
		JPanel listPanel = new JPanel(new GridLayout(1,1));
		listModel = new DefaultListModel<Photo>();
		setPhotoList();
		
		//Setting up the list and listmodel
		photos = new JList<Photo>(listModel);
		photos.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		photos.setSelectedIndex(-1);
		photos.setLayoutOrientation(JList.VERTICAL);
		photos.setVisibleRowCount(1);
		photos.setCellRenderer(new PhotoRenderer());
		photos.addListSelectionListener(this);
		
		//Adding a scrollpane and doing some styling
		JScrollPane scrollUsers = new JScrollPane(photos);
		listPanel.add(scrollUsers);
		listPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10),
				BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Album"),
						BorderFactory.createEmptyBorder(10, 10, 10, 10))));
		return listPanel;
	}
	
	public JPanel createInfoDisplayPanel() {
		photoInfoPanel = new JPanel(new BorderLayout());
		photoInfoPanel.setBorder(BorderFactory.createTitledBorder("Current Album"));
		
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(0,1));

		JLabel nameLabel = new JLabel("Album name: ");
		nameLabel.setAlignmentX(LEFT_ALIGNMENT);
		photoNameLabel = new JLabel();
		photoNameLabel.setAlignmentX(LEFT_ALIGNMENT);
		JPanel namePanel = new JPanel();
		namePanel.add(nameLabel);
		namePanel.add(photoNameLabel);
		infoPanel.add(namePanel);
		
		JLabel numLabel = new JLabel("Number of photos: ");
		photoAlbumsLabel = new JLabel();
		JPanel numPanel = new JPanel();
		numPanel.add(numLabel);
		numPanel.add(photoAlbumsLabel);
		infoPanel.add(numPanel);
		
		JLabel captionLabel = new JLabel("Date range: ");
		photoCaptionLabel = new JLabel();
		JPanel captionPanel = new JPanel();
		captionPanel.add(captionLabel);
		captionPanel.add(photoCaptionLabel);
		infoPanel.add(captionPanel);
		
		JLabel rangeLabel = new JLabel("Date range: ");
		photoDateLabel = new JLabel();
		JPanel rangePanel = new JPanel();
		rangePanel.add(rangeLabel);
		rangePanel.add(photoDateLabel);
		infoPanel.add(rangePanel);		
		
		JLabel tagsLabel = new JLabel("Date range: ");
		photoTagsLabel = new JLabel();
		JPanel tagsPanel = new JPanel();
		tagsPanel.add(tagsLabel);
		tagsPanel.add(photoTagsLabel);
		infoPanel.add(tagsPanel);		

		photoInfoPanel.add(new JScrollPane(infoPanel), BorderLayout.CENTER);
		photoInfoPanel.setVisible(false);
		
		return photoInfoPanel;
	}
	
	public JPanel createSideMenu() {
		openPhotoBtn = new JButton("Open Photo");
		openPhotoBtn.addActionListener(this);
		
		recaptionBtn = new JButton("Recaption Photo");
		recaptionBtn.addActionListener(this);
		
		movePhotoBtn = new JButton("Move Photo");
		movePhotoBtn.addActionListener(this);

		removePhotoBtn = new JButton("Delete Photo");
		removePhotoBtn.addActionListener(this);

		JPanel top = new JPanel(new GridLayout(0,1));
		top.add(openPhotoBtn);
		top.add(recaptionBtn);
		top.add(movePhotoBtn);
		top.add(removePhotoBtn);
		
		JPanel middle = new JPanel(new BorderLayout());
		middle.add(new JPanel(), BorderLayout.NORTH);
		middle.add(new JPanel(), BorderLayout.SOUTH);

		homeBtn = new JButton("Back to Albums");
		homeBtn.addActionListener(this);
		
		slideshowBtn = new JButton("Slideshow");
		slideshowBtn.addActionListener(this);

		JPanel bottom = new JPanel(new BorderLayout());
		bottom.add(slideshowBtn, BorderLayout.NORTH);
		bottom.add(homeBtn, BorderLayout.SOUTH);
		//filler
		//Adding a sidemenu with the delete and logout buttons
		JPanel sideMenu = new JPanel(new BorderLayout());
		sideMenu.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
		
		sideMenu.add(top, BorderLayout.NORTH);		
		sideMenu.add(middle, BorderLayout.CENTER);
		sideMenu.add(bottom, BorderLayout.SOUTH);
		
		return sideMenu;
	}
	
	public JPanel createMainPanel() {
		
		//Create the main panel and add all sub components
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(createInfoDisplayPanel(), BorderLayout.NORTH);
		mainPanel.add(createListPanel(), BorderLayout.CENTER);
		mainPanel.add(createSideMenu(), BorderLayout.EAST);
		
		return mainPanel;
	}
	
	public JPanel createAddPanel() {
		uploadBtn = new JButton(new ImageIcon((new ImageIcon("img/uploadImg.png").getImage().getScaledInstance(20, 20, Image.SCALE_FAST))));
		uploadBtn.addActionListener(this);
		
		photoText = new JLabel();
		
		photoCaptionText = new JTextField(10);
		
		addPhotoBtn = new JButton("Add Photo");
		addPhotoBtn.addActionListener(this);
		addPhotoBtn.setMargin(new Insets(10, 10, 10, 10));
		
		JPanel filenamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		filenamePanel.add(new JLabel("Filename:"));
		filenamePanel.add(photoText);
		
		JPanel captionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		captionPanel.add(new JLabel("Caption"));
		captionPanel.add(photoCaptionText);
		
		JPanel insertPanel = new JPanel(new BorderLayout());
		insertPanel.add(filenamePanel, BorderLayout.NORTH);
		insertPanel.add(captionPanel, BorderLayout.SOUTH);
		
		JPanel sidePanel = new JPanel(new BorderLayout());
		sidePanel.add(uploadBtn, BorderLayout.CENTER);
		sidePanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 20));
		
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		bottomPanel.add(addPhotoBtn);
				
		JPanel addPanel = new JPanel(new BorderLayout());
		addPanel.add(insertPanel, BorderLayout.CENTER);
		addPanel.add(sidePanel, BorderLayout.WEST);
		addPanel.add(bottomPanel, BorderLayout.EAST);
		addPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createLoweredBevelBorder(),
						BorderFactory.createCompoundBorder(
							BorderFactory.createEmptyBorder(5, 5, 5, 5),
							BorderFactory.createTitledBorder("Add New Photo"))),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		
		return addPanel;
	}
	
	public void setAlbum(String albumName) {
		this.albumName = albumName;
		setPhotoList();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		if(source == homeBtn) {
			parent.showAlbums();
		} else if(source == addPhotoBtn) {
			if(photoText.getText().isEmpty()) {
				error("Photo filename must be specified to add photo.");
			} else {
				String filename = photoText.getText();
				if(!photoCaptionText.isEditable()) {
					try {
						parent.addPhoto(filename, null, albumName);
						setPhotoList();
					} catch (Exception e1) {
						error("Photo with filename " + filename + " already exists in album " + albumName + ".");
					}
				} else {
					if(photoCaptionText.getText().isEmpty()) {
						
					} else {
						try {
							parent.addPhoto(filename, photoCaptionText.getText(), albumName);
							setPhotoList();
						} catch (Exception e1) {
							error("Photo with filename " + filename + " already exists in album " + albumName + ".");
						}
					}
				}
			}
		} else if(source == removePhotoBtn) {
			if(photos.getSelectedIndex() != -1) {
				Photo photo = photos.getSelectedValue();
				String photoName = photo.getName();
				String[] options = {"Delete","Cancel"};
				int returnVal = JOptionPane.showOptionDialog(this, "Are you sure you want to delete photo \n" + photoName + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
				if(returnVal ==  0) {
					try {
						parent.removePhoto(photoName, albumName);
						setPhotoList();
						message(photoName + " was removed from the album.");
					} catch(Exception exp) {
						error(albumName + " is not an existing photo in the album.");
					}					
				}
			} else {
				error("No photo selected for removal.");
			}
		} else if(source == movePhotoBtn) {
			if(photos.getSelectedIndex() != -1) {
				Photo photo = photos.getSelectedValue();
				String photoName = photo.getName();
				List<String> albumNames = parent.listAlbums();
				albumNames.remove(albumName);
				String[] choices = albumNames.toArray(new String[0]);
				String newAlbum = (String)JOptionPane.showInputDialog(this, "Where would you like to move the photo\n" + photoName + "?", "Move Photo", JOptionPane.QUESTION_MESSAGE, null, choices, null);
				if(newAlbum != null) {
					if(newAlbum.equals("")) {
						error("Must specify valid location to move photo.");
					} else {
						try {
							parent.movePhoto(photoName, albumName, newAlbum);
							setPhotoList();
							message("Photo " + photoName + " is now in album " + newAlbum);
						} catch (Exception e1) {
							error(e1.getMessage());
						}
					}
				}
			} else {
				error("No photo selected for moving.");
			}
			
		} else if(source == recaptionBtn) {
			if(photos.getSelectedIndex() != -1) {
				Photo photo = photos.getSelectedValue();
				String photoName = photo.getName();
				String[] options = {"Recaption","Cancel"};
				String caption = (String)JOptionPane.showInputDialog(this, "What is the new caption of the photo\n" + photoName + "?\n\n The previous caption was \n" + photo.getCaption(), "Recaption Photo", JOptionPane.QUESTION_MESSAGE, null, null, null);
				if(caption != null) {
					if(caption.equals("")) {
						error("The new caption cannot be empty.");
					} else {
						try {
							parent.recaptionPhoto(photoName, caption);
							setPhotoList();
							message("Photo " + photoName + " now has caption " + caption);
						} catch (PhotoException e1) {
							error(e1.getMessage());
						}
					}
				}
			} else {
				error("No photo selected for recaptioning.");
			}
			
		} else if(source == openPhotoBtn) {
			if(photos.getSelectedIndex() != -1) {
				Photo photo = photos.getSelectedValue();
				String photoName = photo.getName();
				parent.showPhotoInfo(photoName, albumName);
			} else {
				error("No photo selected for opening.");
			}
		} else if(source == slideshowBtn) {
			parent.showSlideshow(parent.getAlbum(albumName));
		} else if(source == uploadBtn) {
			JFileChooser chooser = new JFileChooser();
		    FileNameExtensionFilter filter = new FileNameExtensionFilter(
		            "JPG,PNG,BMP & GIF Images", "jpg", "jpeg", "png", "bmp", "gif");
	        chooser.setFileFilter(filter);
	        chooser.setAcceptAllFileFilterUsed(false);

	        int retVal = chooser.showDialog(this,"Add");
			
	        if (retVal == JFileChooser.APPROVE_OPTION) {
	            File file = chooser.getSelectedFile();
	            String name = file.getPath();
	            photoText.setText(name);
	            Photo photo = parent.getPhoto(name);
	            if(photo != null) {
	            	photoCaptionText.setText(photo.getCaption());
	            	photoCaptionText.setEditable(false);
	            } else {
		            if(photoCaptionText.getText().equals("")) {
		            	photoCaptionText.setText(file.getName());
		            }
	            }
	        }
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	

	/**
	 * Utility for putting error to the statusbar
	 * @param message
	 */
	public void error(String message) {
		status.setForeground(red);
		status.setText(message);
	}
	
	/**
	 * Utility for putting messages to the statusbar
	 * @param message
	 */
	public void message(String message) {
		status.setForeground(black);
		status.setText(message);
	}

}
