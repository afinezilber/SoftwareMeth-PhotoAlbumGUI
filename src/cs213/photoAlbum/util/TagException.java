package cs213.photoAlbum.util;

import cs213.photoAlbum.model.Tag;

/**
 * Extension of Exception class specific to exceptions related to restrictions on Tag
 * @author Ananth Rao
 * @author Allon Fineziber
 *
 */
public class TagException extends Exception {
	
	/**
	 * Constructor
	 * @author Ananth Rao
	 * @author Allon Fineziber
	 */
	public TagException() {
		super();
	}
	
	/**
	 * Constructor
	 * @author Ananth Rao
	 * @author Allon Fineziber
	 * @param message
	 */
	public TagException(String message) {
		super(message);
	}
	
}