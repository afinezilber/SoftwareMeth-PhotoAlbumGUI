package guiview;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.*;

import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Photo;

/**
 * Slideshow screen, self explanatory.
 * Allows viewing individual photos and cycling through in sequence
 * 
 * @author Ananth Rao
 * @author Allon Fineziber
 *
 */
public class SlideShowPanel extends JPanel implements ActionListener {
	public MainApp parent;
	
	public JButton backBtn, prevBtn, nextBtn;
	public JLabel display;
	
	public Album album = null;
	
	public int index = 0;
	public LinkedList<Photo> photos;
	
	public JLabel nameLabel, captionLabel, dateLabel;

	public StatusBar status;
	
	
	public SlideShowPanel(MainApp parent) {
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
		panel.add(createMenuPanel(), BorderLayout.SOUTH);
		panel.add(createTopPanel(), BorderLayout.NORTH);
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
		photos = new LinkedList<Photo>();
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(display, BorderLayout.CENTER);
		mainPanel.setBorder(BorderFactory.createTitledBorder("Slideshow"));
		
		return mainPanel;
	}
	
	public JPanel createMenuPanel() {
		nameLabel = new JLabel();
		captionLabel = new JLabel();
		dateLabel = new JLabel();
		
		JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		namePanel.add(new JLabel("Filename: "));
		namePanel.add(nameLabel);
		
		JPanel captionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		captionPanel.add(new JLabel("Caption: "));
		captionPanel.add(captionLabel);
		
		JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		datePanel.add(new JLabel("Date taken: "));
		datePanel.add(dateLabel);

		JPanel infoPanel = new JPanel(new GridLayout(0,1));
		infoPanel.add(namePanel);
		infoPanel.add(captionPanel);
		infoPanel.add(datePanel);
		
		prevBtn = new JButton("Previous");
		prevBtn.addActionListener(this);
		
		nextBtn = new JButton("Next");
		nextBtn.addActionListener(this);
		
		JPanel menuPanel = new JPanel(new BorderLayout());
		menuPanel.add(prevBtn, BorderLayout.WEST);
		menuPanel.add(nextBtn, BorderLayout.EAST);
		menuPanel.add(infoPanel, BorderLayout.CENTER);
		
		return menuPanel;
	}
	
	public JPanel createTopPanel() {
		backBtn = new JButton("Back to album");
		backBtn.addActionListener(this);
		
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		topPanel.add(backBtn);
		
		return topPanel;
	}
	
	public void display() {
		Photo photo = photos.get(index);
		Image img = new ImageIcon(photo.getName()).getImage();
		img = img.getScaledInstance(display.getWidth(), display.getHeight(), Image.SCALE_SMOOTH);
		
		display.setIcon(new ImageIcon(img));
		nameLabel.setText(photo.getName());
		captionLabel.setText(photo.getCaption());
		dateLabel.setText(parent.formatDate(photo.getDate().getTime()));
		
		if(index == 0) {
			prevBtn.setEnabled(false);
		} else {
			prevBtn.setEnabled(true);
		}

		if(index == photos.size()-1) {
			nextBtn.setEnabled(false);
		} else {
			nextBtn.setEnabled(true);
		}
	}
	
	public void setAlbum(Album album) {
		// TODO Auto-generated method stub
		this.album = album;
		photos.clear();
		photos.addAll(album.listPhotos());
		index = 0;
		display();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		if(source == backBtn) {
			parent.showPhotos(album.getName());
		} else if(source == prevBtn) {
			index--;
			display();
		} else if(source == nextBtn) {
			index++;
			display();
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
