package cs213.photoAlbum.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cs213.photoAlbum.util.AlbumException;
import cs213.photoAlbum.util.PhotoException;

/**
 * User model class that represents a user in the photo album system
 * @author Ananth Rao
 * @author Allon Fineziber
 *
 */
public class User implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2071366819957257894L;

	/**
	 * ID of user
	 */
	public String userID;
	
	/**
	 * Username of user
	 */
	public String userName;
	
	/**
	 * Album of user
	 */
	private HashMap<String, Album> albumList;
	
	/**
	 * Photos of user
	 */
	public LinkedList<Photo> photos;
	
	/**
	 * Constructor
	 * @param userID
	 * @param userName
	 */
	public User(String userID, String userName) {
		this.userID = userID;
		this.userName = userName;
		albumList = new HashMap<String, Album>();
		photos = new LinkedList<Photo>();
	}
	
	/**
	 * Sets the userName
	 * @param userName
	 */
	public void userName(String userName) {
		this.userName = userName;
	}
	
	/**
	 * Returns the userID
	 * @return
	 */
	public String getID() {
		return this.userID;
	}
	
	/**
	 * Returns the userName
	 * @return
	 */
	public String getUserName() {
		return this.userName;
	}
	
	/**
	 * Returns the albums set in a user
	 * @return 
	 */
	public Collection<Album> listAlbums() {
		return albumList.values();
	}
	
	/**
	 * Adds a new album under a user
	 * @param album
	 */
	public void addAlbum(Album album) throws AlbumException {
		
		if(album == null) {
			return;
		}
		String name = album.getName();
		if(albumList.get(name) == null) {
			albumList.put(name, album);
		} else {
			throw new AlbumException("album exists for user " + userID + ":\n" + name);
		}
	}

	/**
	 * Removes an album from album list by name
	 * @param name
	 * @throws AlbumException
	 */
	public void deleteAlbum(String name) throws AlbumException {
		if (albumList.containsKey(name)) {
			albumList.remove(name);
		} else {
			throw new AlbumException("album does not exist for user " + userID + ":\n" + name);
		}
	}
	
	/**
	 * Renames an album name from its old name to its new name
	 * @param oldName
	 * @param newName
	 */
	public void renameAlbum(String oldName, String newName) {
		if (albumList.containsKey(oldName))
			albumList.get(oldName).setName(newName);
	}
	
	/**
	 * Returns the album specified by parameter name
	 * @param name
	 * @return
	 */
	public Album getAlbum(String name) {
		return albumList.get(name);
	}

	/**
	 * Adds photo specified by filename to album specified by parameter albumname
	 * @param filename
	 * @param dateAndTime
	 * @param caption
	 * @param albumname
	 */
	public void addPhoto(String filename, Calendar dateAndTime, String caption, String albumname) {
		Album album = albumList.get(albumname);
		if(album == null) {
			return;
		}
		Photo photo = new Photo(filename, dateAndTime, caption, albumname);
		if(photos.contains(photo)) {
			photo = photos.get(photos.indexOf(photo));
			photo.addedToAlbum(albumname);
			album.addPhoto(photo);
		} else {
			photos.add(photo);
			album.addPhoto(photo);
		}

	}

	/**
	 * Moves photo specified by filename from album with name oldAlbum to album with name newAlbum
	 * @param filename
	 * @param oldAlbum
	 * @param newAlbum
	 * @throws AlbumException
	 * @throws PhotoException
	 */
	public void movePhoto(String filename, String oldAlbum, String newAlbum) throws AlbumException, PhotoException {
		Album album1 = albumList.get(oldAlbum);
		if(album1 == null) {
			throw new AlbumException("Album " + oldAlbum + " does not exist");
		}
		
		Album album2 = albumList.get(newAlbum);
		if(album2 == null) {
			throw new AlbumException("Album " + newAlbum + " does not exist");
		}
		
		if(album1.contains(filename)) {
			if(album2.contains(filename)) {
				return;
			}
			Photo photo = album1.photos.get(filename);
			album1.photos.remove(filename);
			album2.photos.put(filename, photo);
		} else {
			throw new PhotoException("Photo " + filename + " does not exist in " + oldAlbum);
		}
	}
	
	/**
	 * Removes photo with name filename from album with name albumname
	 * @param filename
	 * @param albumname
	 * @throws PhotoException
	 * @throws AlbumException
	 */
	public void removePhoto(String filename, String albumname) throws PhotoException, AlbumException {
		Album album = albumList.get(albumname);
		if(album == null) {
			throw new AlbumException("Album <albumName> does not exist");
		}
		Photo photo = album.photos.get(filename);
		
		album.deletePhoto(filename);
		photo.albums.remove(albumname);
		
		if(photo.albums.isEmpty()) {
			photos.remove(photo);
		}
	}
	
	/**
	 * Returns Photo object specified by filename, or null if it does not exist
	 * @param filename
	 * @return
	 */
	public Photo getPhoto(String filename) {
		
		Photo photo = new Photo(filename, null, null);
		int index = photos.indexOf(photo);
		if(index == -1) {
			return null;
		}
		return photos.get(index);
	}

}
