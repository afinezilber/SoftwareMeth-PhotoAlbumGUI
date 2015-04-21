package cs213.photoAlbum.model;

import java.io.Serializable;
import java.util.Calendar;

public interface PhotoInterface extends Serializable {

	/*
	This method will accept a string that will then add a caption to a photo
	@param Sring caption
	*/
	public void addCaption(String caption);
	
	/*
	This method will accept two strings and pass them as the type and value for the tag
	@param String type
	@param String value
	*/
	public void addTag(String type, String value);
	
	/*
	This method accepts two strings and will remove a tag from a photo
	@param String type
	@param String value
	*/ 
	public void deleteTag(String type, String value);
	
	/*
	This method will return the current date 
	@return 
	*/
	public Calendar getDate();
	
}