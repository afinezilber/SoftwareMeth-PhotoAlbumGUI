package cs213.photoAlbum.util;

/**
 * Extension of Exception class specific to reporting an attempt to create a duplicate User
 * @author Ananth Rao
 * @author Allon Fineziber
 *
 */
public class DuplicateUserException extends Exception {
	
	/**
	 * Constructor
	 */
	public DuplicateUserException() {
		super();
	}
	
	/**
	 * Constructor
	 * @param message
	 */
	public DuplicateUserException(String message) {
		super(message);
	}

}