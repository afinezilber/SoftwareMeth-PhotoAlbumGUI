package cs213.photoAlbum.model;

import java.io.FileNotFoundException;
import java.util.List;

import cs213.photoAlbum.util.DuplicateUserException;

/**
 * 
 * @author Ananth Rao
 * @author Allon Fineziber
 *
 */
public interface BackendInterface {
		
	/**
	 * This method accepts a String that serves as the username
	 * @param String username
	 * @param String userID
	 * @throws DuplicateUserException
	*/	
	public void createUser(String username, String userID) throws DuplicateUserException;
	
	/**
	 * This method accepts a String and then deletes the username
	 * @param username
	 * @throws FileNotFoundException
	 */
	public void deleteUser(String username) throws FileNotFoundException;
	
	/**
	 * This method will list all the users that were created
	 * @return
	 */
	public List<String> listUsers();
	
	/**
	 * This method read the data from a saved file and deserialize the contents
	 * @param username
	 * @return
	 * @throws FileNotFoundException
	 */
	public User readUser(String username) throws FileNotFoundException;
	
	/**
	 * This method will write data into a file and serialize the contents
	 * @param user
	 */
	public void writeUser(User user);
	
	/**
	 * This method deletes all users in the system
	 */
	public void clearData();
}