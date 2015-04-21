package cs213.photoAlbum.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cs213.photoAlbum.util.DuplicateUserException;

/**
 * Backend class for interacting with file system and storing data for session persistence.
 * Implements BackendInterface
 * @author Ananth Rao
 * @author Allon Fineziber
 *
 */
public class Backend implements BackendInterface {
	
	/**
	 * List of userIDs for easy listing of users in the system
	 */
	public List<String> users;
	
	/**
	 * String representing relative path for storage of data
	 */
	private String dataPath;

	/**
	 * Constructor
	 */
	public Backend() {
		dataPath = "data";
		users = new ArrayList<String>();
		File dataDir = new File(dataPath);
		if(dataDir.isDirectory()) {
			FilenameFilter filter = new DataFileFilter();
			for(String name : dataDir.list(filter)) {
				users.add(name.replace(".dat", ""));
			}
		} else {
			dataDir.mkdir();
		}
	}
	
	/**
	 * Creates a user in the system and creates a data file for storage
	 */
	@Override
	public void createUser(String userID, String username) throws DuplicateUserException {
		if((username == null) || (userID == null)) {
			return;
		}

		if(users.contains(userID)) {
			try {
				User user = readUser(userID);
				throw new DuplicateUserException("user " + userID + " already exists with name " + username);
			} catch (FileNotFoundException e) {
				;
			}
		} else {
			writeUser(new User(userID, username));
			users.add(userID);
		}
	}

	/**
	 * Removes a user from the system and deletes the data file associated with the user
	 * @param userID
	 * @throws FileNotFoundException
	 */
	@Override
	public void deleteUser(String userID) throws FileNotFoundException {
		
		if(userID == null) {
			throw new FileNotFoundException();
		}
		File deleteFile = new File(dataPath + "/" + userID + ".dat");
		if(deleteFile.exists()) {
			try {
				deleteFile.delete();
				users.remove(userID);
			} catch(Exception e) {
				;
			}			
		} else {
			throw new FileNotFoundException();
		}
	}

	/**
	 * Lists users in the system
	 * @return List<String>
	 */
	@Override
	public List<String> listUsers() {
		
		return users;
	}

	/**
	 * Loads a user's data from storage file
	 * @param userID
	 * @throws FileNotFoundException
	 */
	@Override
	public User readUser(String userID) throws FileNotFoundException {
		
		if(userID == null) {
			return null;
		}
		User user = null;
		try
		{
			FileInputStream fileIn = new FileInputStream(dataPath + "/" + userID + ".dat");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			user = (User) in.readObject();
			in.close();
			fileIn.close();
			return user;
		} catch(FileNotFoundException e) {
			throw e;
		} catch(IOException e) {
			return null;
		} catch(ClassNotFoundException e) {
			return null;
		}
	}

	/**
	 * Stores a user's data to storage file
	 * @param user
	 */
	@Override
	public void writeUser(User user) {
		
		if(user == null) {
			return;
		}
		try
		{
			FileOutputStream fileOut = new FileOutputStream(dataPath + "/" + user.userID + ".dat");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(user);
			out.close();
			fileOut.close();
		} catch(IOException e) {
			return;
		}
	}

	/**
	 * Deletes all users from the system
	 */
	@Override
	public void clearData() {
		
		for(String userID : users) {
			File deleteFile = new File(dataPath + "/" + userID + ".dat");
			if(deleteFile.exists()) {
				try {
					deleteFile.delete();
					users.remove(userID);
				} catch(Exception e) {
					;
				}
			}
		}
	}
	
	/**
	 * Utility class for filtering through files in data folder
	 * Implements FilenameFilter interface
	 *
	 */
	class DataFileFilter implements FilenameFilter {
		Pattern p;
		
		/**
		 * Constructor
		 */
		public DataFileFilter() {
			String pattern = ".*.dat";
			p = Pattern.compile(pattern);			
		}

		/**
		 * Defines whether a file is accepted by the filter or not listed
		 * @param dir
		 * @param nam
		 */
		public boolean accept(File dir, String name) {
			Matcher m = p.matcher(name);
			return(m.find());
		}
	};
}
