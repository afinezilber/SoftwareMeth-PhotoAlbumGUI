package guiview;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.JFrame;
import javax.swing.JPanel;

import cs213.photoAlbum.control.*;
import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.model.Tag;
import cs213.photoAlbum.util.AlbumException;
import cs213.photoAlbum.util.DateUtils;
import cs213.photoAlbum.util.DuplicateUserException;
import cs213.photoAlbum.util.PhotoException;
import cs213.photoAlbum.util.TagException;

/**
 * Central control for the screens of the application and access of control interface
 * 
 * @author Ananth Rao
 * @author Allon Fineziber
 *
 */
public class MainApp {
	public Control control;
	public LoginFrame loginFrame;
	public MainFrame mainFrame;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy-HH:MM:SS");
	public final Color red = new Color(200, 0, 0);
	public final Color black = new Color(0, 0, 0);
	
	public MainApp() {
		control = new Control();
		displayLogin();
	}
	
	public void displayLogin() {
		loginFrame = new LoginFrame("Photo Album - Login", this);
		
		loginFrame.setPreferredSize(new Dimension(500, 200));
		loginFrame.showGUI();
		

        //Display the window.
        loginFrame.pack();
		loginFrame.setLocationRelativeTo(null);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setVisible(true);
		mainFrame = new MainFrame(this);
	}
	
	public void adminMode() {
		mainFrame.setTitle("Photo Album - Admin");
		mainFrame.setSize(new Dimension(800, 600));
		mainFrame.setContentPane(new AdminPanel(this));
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
		loginFrame.setVisible(false);
	}
	
	public void userMode(String userID) throws Exception {
		control.login(userID);
		
		mainFrame.setTitle("Photo Album - " + userID);
		mainFrame.setSize(new Dimension(800, 600));
		
		JPanel mainPanel = new JPanel(new CardLayout());
		mainPanel.add(new UserPanel(this), "USER VIEW");
		mainPanel.add(new SearchPanel(this), "SEARCH VIEW");
		mainPanel.add(new AlbumPanel(this), "ALBUM VIEW");
		mainPanel.add(new PhotoPanel(this), "PHOTO VIEW");
		mainPanel.add(new SlideShowPanel(this), "SLIDESHOW VIEW");
		
		
		
		mainFrame.setContentPane(mainPanel);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
		loginFrame.setVisible(false);
	}

	public void addUser(String username, String userID) throws DuplicateUserException {
		control.addUser(userID, username);
	}
	
	public void deleteUser(String userID) throws FileNotFoundException {
		control.deleteUser(userID);			
	}
	
	public void addAlbum(String albumName) throws AlbumException {
		control.createAlbum(albumName);
	}
	
	public void renameAlbum(String oldName, String newName) throws AlbumException, PhotoException {
		control.createAlbum(newName);
		List<Photo> photos = control.listPhotos(oldName);
		for(Photo photo : photos) {
			control.addPhoto(photo.fileName, newName);
		}
		control.deleteAlbum(oldName);
	}
	
	public void deleteAlbum(String albumName) throws AlbumException {
		control.deleteAlbum(albumName);
	}
	
	public List<String> listAlbums() {
		List<Album> albums = new ArrayList<Album>(control.listAlbums());
		List<String> retList = new LinkedList<String>();
		
		for(Album a : albums) {
			retList.add(a.getName());
		}
		return retList;
	}
	
	public void addPhoto(String filename, String caption, String albumName) throws AlbumException, PhotoException, IOException {
		if(caption == null) {
			control.addPhoto(filename, albumName);
		} else {
			control.addPhoto(filename, caption, albumName);
		}
	}

	public void recaptionPhoto(String filename, String caption) throws PhotoException {
		control.recaptionPhoto(filename, caption);
	}
	
	public void movePhoto(String filename, String oldAlbum, String newAlbum) throws AlbumException, PhotoException {
		control.movePhoto(filename, oldAlbum, newAlbum);
	}
	
	public void removePhoto(String filename, String albumName) throws AlbumException, PhotoException {
		control.removePhoto(filename, albumName);
	}
	
	public void addTag(String filename, Tag tag) throws PhotoException, TagException {
		control.addTag(filename, tag);
	}
	
	public void removeTag(String filename, Tag tag) throws PhotoException, TagException {
		control.deleteTag(filename, tag);
	}
	
	/**
	 * Calls control searchByDate
	 * 
	 * @param String date1
	 * @param String date2
	 * @return List<Photo>
	 */
	public List<Photo> searchByDate(String date1, String date2) {
		Calendar start = DateUtils.extractDate(date1, false);
		Calendar end = DateUtils.extractDate(date2, false);
		
		if(start == null || end == null) {
			return null;
		}
		return control.getPhotosByDate(start, end);
	}

	/**
	 * Calls control searchByTag
	 * 
	 * @param List<Tag> tags
	 * @return List<Photo>
	 */
	public List<Photo> searchByTag(List<Tag> tags) {
		if(tags == null) {
			return null;
		}
		return control.getPhotosByTag(tags);
	}

	/**
	 * Calls control's logout
	 * Does some screen switching to login screen
	 */
	public void logout() {
		control.logout();
		mainFrame.setVisible(false);
		loginFrame.setVisible(true);
	}
	
	/**
	 * Shows the search screen
	 */
	public void showSearch() {
		Container mainPanel = mainFrame.getContentPane();
		CardLayout c = (CardLayout)mainPanel.getLayout();
		c.show(mainPanel, "SEARCH VIEW");
	}
	
	/**
	 * Shows the main top level user screen after login
	 * 
	 */
	public void showAlbums() {
		Container mainPanel = mainFrame.getContentPane();
		CardLayout c = (CardLayout)mainPanel.getLayout();
		c.show(mainPanel, "USER VIEW");
	}
	
	/**
	 * Shows the slideshow screen for a given album
	 * 
	 * @param Album album
	 */
	public void showSlideshow(Album album) {
		Container mainPanel = mainFrame.getContentPane();
		CardLayout c = (CardLayout)mainPanel.getLayout();
		c.show(mainPanel, "SLIDESHOW VIEW");
		for(Component card : mainPanel.getComponents()) {
			if(card.isVisible()) {
				((SlideShowPanel)card).setAlbum(album);
			}
		}
	}
	
	/**
	 * Shows the photo information screen for a given photo
	 * 
	 * @param String fileName
	 * @param String albumName
	 */
	public void showPhotoInfo(String filename, String albumName) {
		Container mainPanel = mainFrame.getContentPane();
		CardLayout c = (CardLayout)mainPanel.getLayout();
		c.show(mainPanel, "PHOTO VIEW");
		for(Component card : mainPanel.getComponents()) {
			if(card.isVisible()) {
				((PhotoPanel)card).setPhotoAndAlbum(filename, albumName);
			}
		}
	}
	
	/**
	 * Shows the album screen for a given album
	 * 
	 * @param String albumName
	 */
	public void showPhotos(String albumName) {
		Container mainPanel = mainFrame.getContentPane();
		CardLayout c = (CardLayout)mainPanel.getLayout();
		c.show(mainPanel, "ALBUM VIEW");
		for(Component card : mainPanel.getComponents()) {
			if(card.isVisible()) {
				((AlbumPanel)card).setAlbum(albumName);
			}
		}
	}
	
	/**
	 * Returns the Album associated with the name provided
	 * 
	 * @param String name
	 * @return Album
	 */
	public Album getAlbum(String name) {
		LinkedList<Album> albums = new LinkedList<Album>(control.listAlbums());
		ListIterator<Album> iter = albums.listIterator();
		while(iter.hasNext()) {
			Album temp = iter.next();
			if(temp.getName().equals(name)) {
				return temp;
			}
		}
		return null;
	}

	/**
	 * Returns the Photo associated with the name provided
	 * 
	 * @param String name
	 * @return Photo
	 */
	public Photo getPhoto(String name) {
		try {
			return control.listPhotoInfo(name);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Returns a String representation of a date formatted consistent with spec
	 * 
	 * @param Date date
	 * @return String
	 */
	public String formatDate(Date date) {
		return sdf.format(date);
	}
	
}
