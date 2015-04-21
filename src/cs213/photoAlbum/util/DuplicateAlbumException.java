package cs213.photoAlbum.util;

/**
/**
 * Extension of Exception class specific to reporting an attempt to create a duplicate User
 * @author Ananth Rao
 * @author Allon Fineziber
 *
 */
public class DuplicateAlbumException extends Exception {
	
	/**
	 * Constructor
	 * @author Ananth Rao
	 * @author Allon Fineziber
	 */
	public DuplicateAlbumException() {
		super();
	}
	/**
	 * Constructor
	 * @author Ananth Rao
	 * @author Allon Fineziber
	 * @param message
	 */
	public DuplicateAlbumException(String message) {
		super(message);
	}
}