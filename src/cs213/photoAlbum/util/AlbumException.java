package cs213.photoAlbum.util;

/**
 * Extension of Exception class specific to exceptions related to restrictions on Album
 * @author Ananth Rao
 * @author Allon Fineziber
 *
 */
public class AlbumException extends Exception {
	
	/**
	 * Constructor
	 * @author Ananth Rao
	 * @author Allon Fineziber
	 */
	public AlbumException() {
		super();
	}
	
	/**
	 * Constructor
	 * @author Ananth Rao
	 * @author Allon Fineziber
	 * @param message
	 */
	public AlbumException(String message) {
		super(message);
	}
}