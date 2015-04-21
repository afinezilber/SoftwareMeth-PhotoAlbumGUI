package cs213.photoAlbum.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import cs213.photoAlbum.util.TagException;

/**
 * 
 * @author Ananth Rao
 * @author Allon Fineziber
 *
 */
public class Photo implements Comparable<Object>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2313927903106634641L;
	
	/**
	 * 
	 */
	public String fileName;
	
	/**
	 * 
	 */
	public String caption;
	
	/**
	 * 
	 */
	public Calendar dateAndTime;
	
	/**
	 * 
	 */
	public LinkedList<Tag> tags;
	
	/**
	 * 
	 */
	public List<String> albums;
	
	/**
	 * 
	 * @param fileName
	 * @param date
	 * @param caption
	 */
	public Photo(String fileName, Calendar date, String caption) {
		this.fileName = fileName;
		dateAndTime = date;
		this.caption = caption;
		tags = new LinkedList<Tag>();
		albums = new ArrayList<String>();
	}
	
	/**
	 * 
	 * @param fileName
	 * @param date
	 * @param caption
	 * @param album
	 */
	public Photo(String fileName, Calendar date, String caption, String album) {
		this.fileName = fileName;
		dateAndTime = date;
		this.caption = caption;
		tags = new LinkedList<Tag>();
		albums = new ArrayList<String>();
		albums.add(album);
	}
	
	/**
	 * This method accepts a string that it then adds as a caption to this photo
	 * @param caption
	 */
	public void setCaption(String caption) {
		
		this.caption = caption;
	}

	/**
	 * Adds String parameter album to list of albums this photo belongs to.
	 * @param albumname
	 */
	public void addedToAlbum(String albumname) {
		albums.add(albumname);
	}
	
	/**
	 * Removes String parameter album to list of albums this photo belongs to.
	 * @param albumname
	 */
	public void deletedFromAlbum(String albumname) {
		albums.remove(albumname);
	}
	
	/**
	 * Adds tag to list of tags associated with this photo
	 * @param tag
	 * @throws TagException
	 */
	public void addTag(Tag tag) throws TagException {
		
		if(tags.contains(tag)) {
			throw new TagException("Tag already exists for " + fileName + " " + tag.type + ":" + tag.value);
		} else {
			if(tag.type.equalsIgnoreCase("location")) {
				if(!tags.isEmpty()) {
					Tag firstTag = tags.getFirst(); 
					if(firstTag.type.equalsIgnoreCase("location")) {
						throw new TagException("Error: Duplicate location tag. Existing location tag: " + firstTag.type + ":" + firstTag.value);
					}					
				}
				
				tags.addFirst(tag);
				
			} else {
				tags.add(tag);
			}
		}
	}

	/**
	 * 
	 * @param tag
	 * @throws TagException
	 */
	public void deleteTag(Tag tag) throws TagException {
		
		if(tags.contains(tag)) {
			tags.remove(tag);
		} else {
			throw new TagException("Tag does not exist for " + fileName + " " + tag.type + ":" + tag.value);
		}
	}

	/**
	 * 
	 * @return
	 */
	public Calendar getDate() {
		
		return dateAndTime;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return fileName;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCaption() {
		return caption;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Tag> listTags() {
		return tags;
	}
	
	/**
	 * 
	 * @param tag
	 * @return
	 */
	public boolean hasTag(Tag tag) {
		return tags.contains(tag);
	}
	
	/**
	 * 
	 */
	@Override
	public boolean equals(Object arg0) {
		if(arg0 instanceof Photo) {
			Photo temp = (Photo) arg0;
			return this.fileName.equals(temp.getName());
		}
		return false;
	}
	
	/**
	 * 
	 */
	@Override
	public int compareTo(Object arg0) {
		
		if(arg0 instanceof Photo) {
			Photo temp = (Photo) arg0;
			return this.dateAndTime.compareTo(temp.getDate());
		}
		return 0;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "filename=" + fileName + "; date=" + dateAndTime.getTime() + "; tags=" + tags.toString();
	}
}