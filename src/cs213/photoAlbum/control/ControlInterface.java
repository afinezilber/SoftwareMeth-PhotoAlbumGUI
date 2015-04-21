/**
 * Control Interface for interactions involving manipulating model data
 * 
 * @author Ananth Rao
 * @author Allon Fineziber
 *
 */
package cs213.photoAlbum.control;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import cs213.photoAlbum.model.*;
import cs213.photoAlbum.util.AlbumException;
import cs213.photoAlbum.util.DuplicateUserException;
import cs213.photoAlbum.util.PhotoException;
import cs213.photoAlbum.util.TagException;

public interface ControlInterface {
	
	/**
	 *  Returns name of currently logged-in user 
	 *  
	 *  @author Ananth Rao
	 *  @author Allon Fineziber
	 *
	 */
	public String getUserName();

	/**
	 *  Returns id of currently logged-in user 
	 *  
	 *  @author Ananth Rao
	 *  @author Allon Fineziber
	 *
	 */
	public String getUserID();

	/**
	 *  Calls backend method that lists users in system 
	 *  
	 *  @author Ananth Rao
	 *  @author Allon Fineziber
	 *
	 */
	public List<String> listUsers();
	
	/**
	 * Does typechecking for input arguments,
	 * Calls backend method that adds a new user to the system 
	 * 
	 * @author Ananth Rao
	 * @author Allon Fineziber
	 * 
	 * @param String userID
	 * @param String username
	 * @throws DuplicateUserException
	 */
	public void addUser(String userID, String username) throws DuplicateUserException;
	
	/**
	 * Does typechecking for input arguments,
	 * Calls backend method that deletes user from system

	 * @author Ananth Rao
	 * @author Allon Fineziber
	 * 
	 * @param String userID
	 * @throws FileNotFoundException
	 */
	public void deleteUser(String userID) throws FileNotFoundException;

	
	/**
	 * Calls backend clear method to delete all users from the system
	 */
	public void clearUsers();
	
	/**
	 * Does typechecking for input arguments,
	 * Calls backend method to serve up user data if user exists
	 * 
	 * @author Ananth Rao
	 * @author Allon Fineziber
	 * 
	 * @param String userID
	 * @param String username
	 * @throws FileNotFoundException
	 * 
	 * @return Returns true if the login was successful. Returns false otherwise.
	 */
	public void login(String userID) throws FileNotFoundException;
	

	/**
	 * Does typechecking for input arguments
	 * Calls method in global User user that adds new album with name name
	 * 
	 * @author Ananth Rao
	 * @author Allon Fineziber
	 * 
	 * @param String name
	 * @throws AlbumException 
	 */
	public void createAlbum(String name) throws AlbumException;
	
	/**
	 * Calls backend method to serve up user Albums
	 * 
	 * @author Ananth Rao
	 * @author Allon Fineziber
	 * 
	 */
	public Collection<Album> listAlbums();

	/**
	 * Does typechecking for input arguments
	 * Calls method in global User user that deletes album with name name
	 * 
	 * @author Ananth Rao
	 * @author Allon Fineziber
	 * 
	 * @param String name
	 * @throws AlbumException
	 */
	public void deleteAlbum(String name) throws AlbumException;

	/**
	 * Lists photos in Album albumName of User
	 * 
	 * @author Ananth Rao
	 * @author Allon Fineziber
	 * 
	 * @param String albumName
	 * @return List<Photo> photos
	 * 
	 * Returns list of Photo objects
	 * @throws AlbumException 
	 * 
	 */
	public List<Photo> listPhotos(String albumName) throws AlbumException;

	/**
	 * Calls global User user method to get Album with name albumName
	 * Calls Album method to add Photo with filename filename and caption caption
	 * This is the method to use when a photo is being added to the user's system for the first time
	 * 
	 * @author Ananth Rao
	 * @author Allon Fineziber
	 * 
	 * @param String filename
	 * @param String caption
	 * @param String albumName
	 * @throws AlbumException 
	 * @throws PhotoException 
	 * @throws IOException 
	 */
	public void addPhoto(String filename, String caption, String albumName) throws AlbumException, PhotoException, IOException;

	/**
	 * Calls global User user method to get Album with name albumName
	 * Calls Album method to add Photo with filename filename
	 * 
	 * @author Ananth Rao
	 * @author Allon Fineziber
	 * 
	 * @param String filename
	 * @param String albumName
	 * @return 
	 * @throws AlbumException 
	 * @throws PhotoException 
	 */
	public String addPhoto(String filename, String albumName) throws AlbumException, PhotoException;

	/**
	 * Calls global User user method to get Album with name albumName
	 * Calls Album method to delete Photo with filename filename from 
	 * Album with albumName
	 * 
	 * @author Ananth Rao
	 * @author Allon Fineziber
	 * 
	 * @param String filename
	 * @param String albumName
	 * @throws AlbumException 
	 * @throws PhotoException 
	 */
	public void removePhoto(String filename, String albumName) throws AlbumException, PhotoException;

	/**
	 * Calls global User user method to get Album with name oldAlbum,
	 * Calls Album method to delete Photo with filename filename from oldAlbum,
	 * Calls global User user method to get Album with name newAlbum,
	 * Calls Album method to add Photo with filename filename to newAlbum
	 * 
	 * @author Ananth Rao
	 * @author Allon Fineziber
	 * 
	 * @param String filename
	 * @param String oldAlbum
	 * @param String newAlbum
	 * @throws AlbumException 
	 * @throws PhotoException 
	 */
	public void movePhoto(String filename, String oldAlbum, String newAlbum) throws AlbumException, PhotoException;

	/**
	 * Calls global User user method to get Photo of name filename
	 * Adds tag to Photo
	 * 
	 * @author Ananth Rao
	 * @author Allon Fineziber
	 * 
	 * @param String filename
	 * @param Tag tag
	 * @throws PhotoException 
	 * @throws TagException 
	 */
	public void addTag(String filename, Tag tag) throws PhotoException, TagException;

	/**
	 * Calls global User user method to get Photo of name filename
	 * Adds tag to Photo with type type and value value
	 * 
	 * @author Ananth Rao
	 * @author Allon Fineziber
	 * 
	 * @param String filename
	 * @param Tag tag
	 * @throws TagException 
	 * @throws PhotoException 
	 */
	public void deleteTag(String filename, Tag tag) throws PhotoException, TagException;

	/**
	 * Calls global User user method to get Photo of name filename
	 * Adds tag to Photo with type type and value value
	 * 
	 * @author Ananth Rao
	 * @author Allon Fineziber
	 * 
	 * @param String filename
	 * @param String type
	 * @param String value
	 * @return 
	 * @throws PhotoException 
	 */
	public Photo listPhotoInfo(String filename) throws PhotoException;

	/**
	 * Calls global User user method to get Photo of name filename
	 * with Calendar dateAndTime between Calendar start and Calendar end
	 * 
	 * @author Ananth Rao
	 * @author Allon Fineziber
	 * 
	 * @param Calendar start
	 * @param Calendar end
	 */
	public List<Photo> getPhotosByDate(Calendar start, Calendar end);

	/**
	 * Calls global User user method to get Photo objects
	 * with all of the tags in tags
	 * 
	 * @author Ananth Rao
	 * @author Allon Fineziber
	 * 
	 * @param List<Tag> tags
	 */
	public List<Photo> getPhotosByTag(List<Tag> tags);

	/**
	 * Calls backend method to write data of global User user
	 * 
	 * @author Ananth Rao
	 * @author Allon Fineziber
	 * 
	 */
	public void logout();

}