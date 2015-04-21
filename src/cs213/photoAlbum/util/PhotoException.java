package cs213.photoAlbum.util;

/**
 * Extension of Exception class specific to exceptions related to restrictions on Photo
 * @author Ananth Rao
 * @author Allon Fineziber
 *
 */
public class PhotoException extends Exception {
	private String filename;
	private String albumname;
	
	/**
	 * Constructor
	 * @author Ananth Rao
	 * @author Allon Fineziber
	 */
	public PhotoException() {
		super();
	}
	/**
	 * Constructor
	 * @author Ananth Rao
	 * @author Allon Fineziber
	 * @param message
	 */
	public PhotoException(String message) {
		super(message);
	}
}