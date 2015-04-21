package cs213.photoAlbum.model;

import cs213.photoAlbum.util.*;

import java.io.Serializable;
import java.util.*;

/**
 * Model class representing an album that holds photos
 * @author Ananth Rao
 * @author Allon Fineziber
 *
 */
public class Album implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 429839659822479878L;

	/**
	 * Name of the album
	 */
	public String name;
	
	/**
	 * Hashmap holding all of the photo objects in the album, indexed by filename
	 */
	public HashMap<String, Photo> photos;
	
	/**
	 * Number of photos
	 */
	public int numPhotos;
	
	/**
	 * Constructor
	 * @param name
	 */
	public Album(String name) {
		this.name = name;
		photos = new HashMap<String, Photo>();
		numPhotos = 0;
	}
	
	/**
	 * Sets album name
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns name of album
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Adds a photo the album
	 * @param photo
	 */
	public void addPhoto(Photo photo) {
		
		photos.put(photo.getName(), photo);
		numPhotos++;
	}
	
	/**
	 * Deletes a photo from an album
	 * @param name
	 * @throws PhotoException 
	 */
	public void deletePhoto(String filename) throws PhotoException {
		if (photos.containsKey(filename)) {
			photos.remove(filename);
			numPhotos--;
		} else {
			throw new PhotoException("Photo " + filename + "is not in album " + name);
		}
	}
	
	/**
	 * Returns true if the album contains the photo identified by String filename
	 * @param filename
	 * @return
	 */
	public boolean contains(String filename) {
		return photos.containsKey(filename);
	}
	
	/**
	 * Returns a Collection<Photo> view of the photos in the album
	 * @return
	 */
	public Collection<Photo> listPhotos() {
		return photos.values();
	}
	
	/**
	 * Sets caption of photo identified by String filename
	 * @param filename
	 * @param caption
	 */
	public void setCaption(String filename, String caption) {
		
		Photo photo = photos.get(filename);
		photo.setCaption(caption);
	}
	
	/**
	 * Return a String representation of Album object
	 */
	@Override
	public String toString() {
		return "name=" + name + "; numPhotos=" + numPhotos + "; photos=" + photos.toString();
	}
}