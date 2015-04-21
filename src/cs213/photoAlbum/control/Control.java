/**
 * Control Class for interactions involving manipulating model data
 * Implements ControlInterface
 * 
 * @author Ananth Rao
 * @author Allon Fineziber
 *
 */


package cs213.photoAlbum.control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.BackendInterface;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.model.Tag;
import cs213.photoAlbum.model.Backend;
import cs213.photoAlbum.model.User;
import cs213.photoAlbum.util.AlbumException;
import cs213.photoAlbum.util.DateUtils;
import cs213.photoAlbum.util.DuplicateUserException;
import cs213.photoAlbum.util.PhotoException;
import cs213.photoAlbum.util.TagException;


public class Control implements ControlInterface {
	
	/**
	 * 
	 */
	public User user;
	
	/**
	 * 
	 */
	public BackendInterface backend;

	/**
	 * 
	 */
	public Control() {
		backend = new Backend();
	}
	
	/**
	 * 
	 */
	public String getUserName() {
		if(user == null) {
			return null;
		}
		return user.userName;
	}
	
	/**
	 * 
	 */
	public String getUserID() {
		if(user == null) {
			return null;
		}
		return user.userID;
	}

	/**
	 * 
	 */
	@Override
	public List<String> listUsers() {
		
		return backend.listUsers();
	}

	/**
	 * 
	 */
	@Override
	public void addUser(String userID, String username) throws DuplicateUserException {
		
		backend.createUser(userID, username);
	}

	/**
	 * 
	 */
	@Override
	public void deleteUser(String userID) throws FileNotFoundException {
		
		backend.deleteUser(userID);
	}
	
	/**
	 * 
	 */
	@Override
	public void clearUsers() {
		backend.clearData();
	}

	/**
	 * 
	 */
	@Override
	public void login(String userID) throws FileNotFoundException {
		
		user = backend.readUser(userID);			
		return;
	}


	/**
	 * 
	 */
	@Override
	public void createAlbum(String name) throws AlbumException {
		
		if(user == null) {
			return;
		} else {
			
			user.addAlbum(new Album(name));
		}
	}

	/**
	 * 
	 */
	@Override
	public Collection<Album> listAlbums() {
		if(user == null) {
			return null;
		}
		
		return user.listAlbums();
	}

	/**
	 * 
	 */
	@Override
	public void deleteAlbum(String name) throws AlbumException {
		
		if(user == null) {
			return;
		}
		user.deleteAlbum(name);
	}

	/**
	 * 
	 */
	@Override
	public List<Photo> listPhotos(String albumName) throws AlbumException {
		
		if(user == null) {
			return null;
		}
		
		if(albumName == null) {
			return null;
		}
		Album album = user.getAlbum(albumName);
		if(album == null) {
			throw new AlbumException("Album " + albumName + " does not exist");
		}
		return new LinkedList<Photo>(album.listPhotos());
	}

	public void recaptionPhoto(String filename, String caption) throws PhotoException {
		if(user == null) {
			return;
		}
		
		Photo photo = user.getPhoto(filename);
		if(photo == null) {
			throw new PhotoException("Photo " + filename + "is not in system.");
		} else {
			photo.setCaption(caption);
		}
		
	}
	
	/**
	 * 
	 */
	@Override
	public void addPhoto(String filename, String caption, String albumName) throws AlbumException, PhotoException, FileNotFoundException, IOException {
		
		if(user == null) {
			return;
		}
		
		Album album = user.getAlbum(albumName);
		if(album == null) {
			throw new AlbumException("Album " + albumName + " does not exist");
		}
		if(album.contains(filename)) {
			throw new PhotoException("Photo  + " + filename + " already exists in album " + albumName);
		}
		
		Photo temp = new Photo(filename, null, null);
		Calendar dateAndTime = null;
		if(!user.photos.contains(temp)) {
			File photoFile = new File(filename);
			if(photoFile.exists()) {
				Path path = Paths.get(filename);
				BasicFileAttributes attributes = null;

				attributes = Files.readAttributes(path, BasicFileAttributes.class);
				
				FileTime creationTime = attributes.creationTime();
				dateAndTime = DateUtils.extractDate(creationTime.toString(), true);
				
				user.addPhoto(filename, dateAndTime, caption, albumName);
			} else {
				throw new FileNotFoundException("File " + filename + " does not exist");
			}
		}
	}

	/**
	 * 
	 */
	@Override
	public String addPhoto(String filename, String albumName) throws AlbumException, PhotoException {
		
		if(user == null) {
			return null;
		}
		
		
		Album album = user.getAlbum(albumName);
		if(album == null) {
			throw new AlbumException("Album " + albumName + " does not exist");
		}
		if(album.contains(filename)) {
			throw new PhotoException("Photo " + filename + "already exists in album" + albumName);
		}
		
		Photo temp = new Photo(filename, null, null);
		if(user.photos.contains(temp)) {
			user.addPhoto(filename, null, null, albumName);
		} else {
			throw new PhotoException("Error: Must specify a caption when first adding a photo to system");
		}
		return albumName;		
	}

	/**
	 * 
	 */
	@Override
	public void removePhoto(String filename, String albumname) throws AlbumException, PhotoException {
		
		if(user == null) {
			return;
		}
		
		Album album = user.getAlbum(albumname);
		if(album == null) {
			throw new AlbumException("Album " + albumname + " does not exist");
		}
		
		Photo photo = user.getPhoto(filename);
		if((photo == null) || (album.contains(filename) == false)) {
			throw new PhotoException("Photo " + filename + " is not in album " + albumname);
		}
		
		user.removePhoto(filename, albumname);
	}

	/**
	 * 
	 */
	@Override
	public void movePhoto(String filename, String oldAlbum, String newAlbum) throws AlbumException, PhotoException {
		
		if(user == null) {
			return;
		}
		
		
		Album oldA = user.getAlbum(oldAlbum);
		if(oldA == null) {
			throw new AlbumException("Album " + oldAlbum + " does not exist");
		}		

		Album newA = user.getAlbum(newAlbum);
		if(newA == null) {
			throw new AlbumException("Album " + newAlbum + " does not exist");
		}

		Photo photo = user.getPhoto(filename);
		if((photo == null) || (oldA.contains(filename) == false)) {
			throw new PhotoException("Photo " + filename + " does not exist in " + oldAlbum);
		}
		
		user.movePhoto(filename, oldAlbum, newAlbum);
	}

	/**
	 * 
	 */
	@Override
	public void addTag(String filename, Tag tag) throws PhotoException, TagException {
		
		if(user == null) {
			return;
		}
		
		Photo photo = user.getPhoto(filename);
		if(photo == null) {
			throw new PhotoException("Photo " + filename + " does not exist");
		}
		
		photo.addTag(tag);
	}

	/**
	 * 
	 */
	@Override
	public void deleteTag(String filename, Tag tag) throws PhotoException, TagException {
		
		if(user == null) {
			return;
		}
		
		Photo photo = user.getPhoto(filename);
		if(photo == null) {
			throw new PhotoException("Photo " + filename + " does not exist");
		}
		
		photo.deleteTag(tag);
	}

	/**
	 * 
	 */
	@Override
	public Photo listPhotoInfo(String filename) throws PhotoException {
		
		if(user == null) {
			return null;
		}
		Photo photo = user.getPhoto(filename);
		if(photo == null) {
			throw new PhotoException("Photo does not exist");
		}
		return photo;
	}

	/**
	 * 
	 */
	@Override
	public List<Photo> getPhotosByDate(Calendar start,
			Calendar end) {
		
		if(user == null) {
			return null;
		}
		
		LinkedList<Photo> photos = new LinkedList<Photo>(user.photos);

		ListIterator<Photo> iterator = photos.listIterator();
		while(iterator.hasNext()) {
			Photo photo = iterator.next();
			Calendar photoDate = photo.getDate();
			if((photoDate.compareTo(start) < 0) || (photoDate.compareTo(end) > 0)) {
				iterator.remove();
			}
		}
		
		return photos;
	}

	/**
	 * 
	 */
	@Override
	public List<Photo> getPhotosByTag(List<Tag> tags) {
		
		
		if(user == null) {
			return null;
		}
		
		LinkedList<Photo> photos = new LinkedList<Photo>(user.photos);
		if(photos.isEmpty()) {
			return null;
		}
		
		ListIterator<Photo> iterator = photos.listIterator();
		while(iterator.hasNext()) {
			Photo photo = iterator.next();
			for(Tag tag : tags) {
				if(!tag.getType().isEmpty()) {
					if(!photo.hasTag(tag)) {
						iterator.remove();
						break;
					}					
				} else {
					List<Tag> photosTags = photo.listTags();
					boolean contains = true;
					for(Tag photoTag : photosTags) {
						if(!photoTag.getValue().equals(tag.getValue())) {
							contains = false;
							break;
						}
					}
					if(!contains) {
						iterator.remove();
						break;
					}
				}
			}
		}
		
		return photos;
	}

	/**
	 * 
	 */
	@Override
	public void logout() {
		backend.writeUser(user);
		
	}

}
