package cs213.photoAlbum.simpleview;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cs213.photoAlbum.control.ControlInterface;
import cs213.photoAlbum.control.Control;
import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.model.Tag;
import cs213.photoAlbum.util.AlbumException;
import cs213.photoAlbum.util.DateUtils;
import cs213.photoAlbum.util.DuplicateUserException;
import cs213.photoAlbum.util.PhotoException;

/**
 * Main view class, simple command line view
 * 
 * @author Ananth Rao
 * @author Allon Fineziber
 *
 */
public class CmdView {
	
	/**
	 * Static Scanner object to read inputs
	 */
	private static Scanner stdin = new Scanner(System.in);
	/**
	 * Static Control object for interface to model
	 */
	private static ControlInterface control = new Control();
	
	/**
	 * SimpleDateFormat object for reading and outputting dates in desired format
	 */
	private static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy-HH:MM:SS");
	
	/**
	 * Interactive Mode, to be invoked when a user is logged in
	 */
	public static void interactiveMode() {
		while(true) {
			String input = stdin.nextLine();
			StringTokenizer tk = new StringTokenizer(input);
			if(!tk.hasMoreElements()) {
				continue;
			}
			String command = tk.nextToken();
			int count = tk.countTokens();
				switch(command) {
				case "createAlbum":
					if(count != 1) {
						wrongNumArgs("createAlbum");
					} else {
						createAlbum(input);
					}
					break;
				case "deleteAlbum":
					if(count != 1) {
						wrongNumArgs("deleteAlbum");
					} else {
						deleteAlbum(input);
					}
					break;
				case "listAlbums":
					if(count != 0) {
						wrongNumArgs("listAlbums");
					} else {
						listAlbums();
					}
					break;
				case "listPhotos":
					if(count != 1) {
						wrongNumArgs("listPhotos");
					} else {
						listPhotos(input);
					}
					break;
				case "addPhoto":
					if(count < 3) {
						wrongNumArgs("addPhoto");
					} else {
						addPhoto(input);
					}
					break;
				case "movePhoto":
					if(count < 4) {
						wrongNumArgs("movePhoto");
					} else {
						movePhoto(input);
					}
					break;
				case "removePhoto":
					if(count < 2) {
						wrongNumArgs("removePhoto");
					} else {
						removePhoto(input);
					}
					break;
				case "addTag":
					if(count < 2) {
						wrongNumArgs("addTag");
					} else {
						addTag(input);
					}
					break;
				case "deleteTag":
					if(count < 2) {
						wrongNumArgs("deleteTag");
					} else {
						LinkedList<String> args = extractArgs(input);
						LinkedList<Tag> tags = extractTag(input);
						if(!tags.isEmpty() && !args.isEmpty()) {
							String filename = args.getFirst();
							Tag tag = tags.getFirst();
							try {
								control.deleteTag(filename, tag);
								System.out.println("Deleted tag: " + filename + " " + tag.type + ": " + tag.value);
							} catch(Exception e) {
								System.out.println(e.getMessage());
							}
						} else {
							System.out.println("Error: Invalid arguments for command \"deleteTag\"");
						}
					}
					break;
				case "listPhotoInfo":
					if(count != 1) {
						wrongNumArgs("listPhotoInfo");
					} else {
						LinkedList<String> args = extractArgs(input);
						if(!args.isEmpty()) {
							String filename = args.getFirst();
							try {
								Photo photo = control.listPhotoInfo(filename);
								

								System.out.println("Photo file name: " + photo.getName());
								String albumString = photo.albums.toString();
								System.out.println("Album: " + albumString.substring(1, albumString.length()-1));
								System.out.println("Date: " + sdf.format(photo.getDate().getTime()));
								System.out.println("Caption: " + photo.getCaption());
								System.out.println("Tags: ");
								Collections.sort(photo.tags);
								for(Tag tag : photo.tags) {
									System.out.println(tag);
								}
							} catch(Exception e) {
								System.out.println("Photo " + filename + "does not exist");
							}
						} else {
							System.out.println("Error: Invalid arguments. Filename must be inside quotes \"\"");			
						}
					}
					break;
				case "getPhotosByDate":
					if(count != 2) {
						wrongNumArgs("getPhotosByDate");
					} else {
						String dateString = tk.nextToken();
						Calendar start = DateUtils.extractDate(dateString, false);

						if(start == null) {
							System.out.println("Error: Invalid Date " + dateString);
							break;
						}

						dateString = tk.nextToken();
						Calendar end = DateUtils.extractDate(dateString, false);

						if(end == null) {
							System.out.println("Error: Invalid Date " + dateString);
							break;
						}

						List<Photo> photos  = control.getPhotosByDate(start, end);
						System.out.println("Photos for user " + control.getUserID() + " in range " + start.toString() + " to " + end.toString() + ": ");
						for(Photo photo : photos) {
							System.out.print(photo.getCaption() + " - Album: ");
							System.out.print(photo.albums.get(0));
							for(String albumname : photo.albums.subList(1, photo.albums.size())) {
								System.out.print(", ");
								System.out.print(albumname);
							}
							
							System.out.println(" - Date: " + sdf.format(photo.getDate().getTime()));
						}
						
					}
					break;
				case "getPhotosByTag":
					if(count < 1) {
						wrongNumArgs("getPhotosByTag");
					} else {
						LinkedList<Tag> tags = extractTag(input);

						List<Photo> photos  = control.getPhotosByTag(tags);
						System.out.println("Photos for user " + control.getUserID() + " with tags " + input.split(" ", 2)[1]);
						for(Photo photo : photos) {
							System.out.print(photo.getCaption() + " - Album: ");
							System.out.print(photo.albums.get(0));
							for(String albumname : photo.albums.subList(1, photo.albums.size())) {
								System.out.print(", ");
								System.out.print(albumname);
							}
							System.out.println();
						}
					}
					break;
				case "logout":
					control.logout();
					return;
				case "-h":
					help();
					break;
				default:
					System.out.println("Error: What are you trying to do? You must type a valid command. Please type \"-h\" for more information.");
					break;
				}

		}
	}

	public static void createAlbum(String input) {
		LinkedList<String> albums = extractArgs(input);
		if(!albums.isEmpty()) {
			try {
				String albumName = albums.getFirst();
				control.createAlbum(albumName);
				System.out.println("created album for user " + control.getUserID() + ":\n" + albumName);
			} catch(AlbumException e) {
				System.out.println(e.getMessage());
			}
		} else {
			System.out.println("Error: Invalid argument. Album name must be inside quotes \"<albumName>\"");
		}
	}
	
	public static void deleteAlbum(String input) {
		LinkedList<String> albums = extractArgs(input);
		if(!albums.isEmpty()) {
			try {
				String albumName = albums.getFirst();
				control.deleteAlbum(albumName);
				System.out.println("deleted album from user " + control.getUserID() + ":\n" + albumName);
			} catch(AlbumException e) {
				System.out.println(e.getMessage());
			}
		} else {
			System.out.println("Error: Invalid argument. Album name must be inside quotes \"<albumName>\"");
		}
	}
	
	public static void listAlbums() {
		Collection<Album> albums = control.listAlbums();
		if(albums.isEmpty()) {
			System.out.println("No albums exist for user " + control.getUserID());
		} else {
			System.out.println("Albums for user " + control.getUserID());
			for(Album album : albums) {
				System.out.print(album.getName() + " number of photos: " + album.numPhotos);
				LinkedList<Photo> photos = new LinkedList<Photo>(album.listPhotos());
				Collections.sort(photos);
				if(!photos.isEmpty()) {
					System.out.println(", " + sdf.format(photos.getFirst().getDate().getTime()) + " - " + sdf.format(photos.getLast().getDate().getTime()));								
				} else {
					System.out.println();
				}
			}							
		}
	}
	
	public static void listPhotos(String albumname) {
		LinkedList<String> albums = extractArgs(albumname);
		if(!albums.isEmpty()) {
			try {
				String albumName = albums.getFirst();
				LinkedList<Photo> photos = (LinkedList<Photo>)control.listPhotos(albumName);
				System.out.println("Photos for album " + albumName + ": ");
				for(Photo photo : photos) {
					System.out.println(photo.getName() + " - " + sdf.format(photo.getDate().getTime()));
				}
			} catch(AlbumException e) {
				System.out.println(e.getMessage());
			}
		} else {
			System.out.println("Error: Invalid argument. Album name must be inside quotes \"<albumName>\"");
		}
	}
	
	public static void addPhoto(String input) {
		LinkedList<String> args = extractArgs(input);
		if(args.size() > 1) {
			String filename = args.remove();
			try {
				String album = null;
				String caption = null;
				if(args.size() == 1) {
					album = args.getFirst();
					caption = control.addPhoto(filename, album);
					System.out.println("Added photo " + filename + ":\n" +  caption + " - Album: " + album);
				} else if(args.size() == 2) {
					caption = args.getFirst();
					album = args.getLast();
					control.addPhoto(filename, caption, album);
					System.out.println("Added photo " + filename + ":\n" +  caption + " - Album: " + album);
				} else {
					System.out.println("Error: Invalid arguments. Filename, album name and caption must be inside quotes \"\"");
				}
			} catch(AlbumException e) {
				System.out.println(e.getMessage());
			} catch(PhotoException e) {
				System.out.println(e.getMessage());
			} catch(FileNotFoundException e) {
				System.out.println("File " + filename + " does not exist");							
			} catch(IOException e) {
				System.out.println("Error: Sorry, this file could not be read. Please try again.");
			}
			
		} else {
			System.out.println("Error: Invalid arguments. Filename, album name and caption must be inside quotes \"\"");
		}
	}
	
	public static void movePhoto(String input) {
		LinkedList<String> args = extractArgs(input);
		if(args.size() == 3) {
			String filename = args.remove();
			String oldAlbum = args.remove();
			String newAlbum = args.remove();
			try {
				control.movePhoto(filename, oldAlbum, newAlbum);
				System.out.println("Moved photo " + filename + ": " + filename + " - From album " + oldAlbum  + "to album " + newAlbum);
			} catch(AlbumException e) {
				System.out.println(e.getMessage());
			} catch(PhotoException e) {
				System.out.println(e.getMessage());
			}							
		} else {
			System.out.println("Error: Invalid arguments. Filename and album name must be inside quotes \"\"");
		}
	}
	
	public static void removePhoto(String input) {
		LinkedList<String> args = extractArgs(input);
		if(args.size() == 2) {
			String filename = args.remove();
			String albumname = args.remove();
			try {
				control.removePhoto(filename, albumname);
				System.out.println("Removed photo: " + filename + " - From album " + albumname);
			} catch(AlbumException e) {
				System.out.println(e.getMessage());
			} catch(PhotoException e) {
				System.out.println(e.getMessage());
			}							
		}
	}
	
	public static void addTag(String input) {
		LinkedList<String> args = extractArgs(input);
		LinkedList<Tag> tags = extractTag(input);
		if(!tags.isEmpty() && !args.isEmpty()) {
			String filename = args.getFirst();
			Tag tag = tags.getFirst();
			try {
				control.addTag(filename, tag);
				System.out.println("Added tag: " + filename + " " + tag.type + ": " + tag.value);
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
		} else {
			System.out.println("Error: Invalid arguments for command \"addTag\"");
		}
	}
	
	/**
	 * Extracts tags from a given input string using regular expressions
	 * @param input
	 * @return
	 */
	public static LinkedList<Tag> extractTag(String input) {
		LinkedList<Tag> tags = new LinkedList<Tag>();
		Pattern r = Pattern.compile("(?<=[ ,])([0-9a-zA-Z]*:\"[^\"]*\")");
		Matcher m = r.matcher(input);
		
		while(m.find()) {
			StringTokenizer tk = new StringTokenizer(m.group(), ":");
			if(tk.countTokens() != 2) {
				return null;
			} else {
				String type = tk.nextToken();
				String value = tk.nextToken();
				value = value.substring(1, value.length()-1);
				Tag tag = new Tag(type, value);
				tags.add(tag);
			}
		}
		return tags;
	}
	
	/**
	 * Extracts fields like albumname and caption that have spaces in them and are inside quotes
	 * @param input
	 * @return
	 */
	public static LinkedList<String> extractArgs(String input) {
		LinkedList<String> args = new LinkedList<String>();
		Pattern r = Pattern.compile("(?<=[ ,])\"[^\"]*\"");
		Matcher m = r.matcher(input);
		
		while(m.find()) {
			String value = m.group();
			value = value.substring(1, value.length()-1);
			args.add(value);
		}
		return args;
	}
	
	/**
	 * Error reporter method for incorrect number of arguments for given command
	 * @param command
	 */
	public static void wrongNumArgs(String command) {
		System.out.println("Error: Wrong number of arguments for command \"" + command + "\"");
	}
	
	public static void listUsers() {
		List<String> output = control.listUsers();
		for(String user : output) {
			System.out.println(user);
		}		
	}
	
	public static void addUser(String userID, String username) throws DuplicateUserException {
		control.addUser(userID, username);
		System.out.println("created user " + userID + " with name " + username);
	}
	
	public static void deleteUser(String userID) throws FileNotFoundException {
		control.deleteUser(userID);
		System.out.println("deleted user " + userID);
	}
	
	public static void login(String userID) throws FileNotFoundException {
		control.login(userID);
	}
	
	/**
	 * Entry point
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length == 0) {
			System.out.println("Error: Not enough arguments. Use -h for more usage information.");
			return;
		}
		switch(args[0]) {
		case "listusers":
			listUsers();
			break;
		case "adduser":
			if(args.length == 3) {
				try {
					addUser(args[1], args[2]);
				} catch(Exception e) {
					System.out.println(e.getMessage());
				}
			} else {
				wrongNumArgs("adduser");
			}
			break;
		case "deleteuser":
			if(args.length == 2) {
				String userID = args[1];
				try {
					deleteUser(args[1]);
				} catch(FileNotFoundException e) {
					System.out.println("user " + userID + " does not exist");
				}
			} else {
				wrongNumArgs("deleteuser");
			}			
			break;
		case "login":
			if(args.length == 2) {
				String userID = args[1];
				try {
					login(args[1]);
					interactiveMode();					
				} catch(FileNotFoundException e) {
					System.out.println("user " + userID + "does not exist");
				}
			} else {
				wrongNumArgs("login");
			}
			break;
		case "clearUsers":
			control.clearUsers();
			System.out.println("All users cleared from system");
			break;
		case "-h":
			help();
			break;
		default:
			System.out.println("Error: Invalid command. Use -h for more usage information.");
			break;
		}
	}
	
	/**
	 * Provides help information, similar to man page
	 */
	public static void help() {
		System.out.println("This program has two modes, admin mode and interactive mode.");
		System.out.println("The commmands and behaviors for each of the two modes follow.");
		System.out.println();
		System.out.println("ADMIN MODE:");
		System.out.println("This is the mode used for administrative tasks like managing users.");
		System.out.println();
		System.out.println("commands:");
		System.out.println(" - listusers \t-- lists all users in the system");
		System.out.println("\t Usage: listusers");
		System.out.println();
		System.out.println();
		System.out.println(" - adduser \t-- adds a new user to the system");
		System.out.println("\t Usage: adduser <user id> \"<user name>\"");
		System.out.println("\t NOTE: the quotation marks are necessary in order to have spaces within the user's name");
		System.out.println();
		System.out.println();
		System.out.println(" - deleteuser \t-- delete a user to the system");
		System.out.println("\t Usage: deleteuser <user id>");
		System.out.println();
		System.out.println();
		System.out.println(" - login \t-- logs in to a user's account");
		System.out.println("\t Usage: login <user id>");
		System.out.println();
		System.out.println();
		System.out.println("INTERACTIVE MODE:");
		System.out.println("This is the mode used within a user's account for managing their photos.");
		System.out.println();
		System.out.println("commands:");
		System.out.println(" - listAlbums \t-- lists all albums in the user's account");
		System.out.println("\t Usage: listAlbums");
		System.out.println();
		System.out.println();
		System.out.println(" - createAlbum \t-- creates a new album in the user's account");
		System.out.println("\t Usage: createAlbum \"<name>\"");
		System.out.println("\t NOTE: the quotation marks are necessary in order to have spaces within the album name");
		System.out.println();
		System.out.println();
		System.out.println(" - deleteAlbum \t-- delete the specified album from the user's account");
		System.out.println("\t Usage: deleteAlbum \"<name>\"");
		System.out.println("\t NOTE: the quotation marks are necessary in order to have spaces within the album name");
		System.out.println();
		System.out.println();
		System.out.println(" - addPhoto \t-- adds the specified photo to the specified album");
		System.out.println("\t Usage: addPhoto \"<photo filename>\" \"<caption>\" \"<album name>\"");
		System.out.println("\t NOTE: the quotation marks are necessary in order to have spaces within the input fields");
		System.out.println();
		System.out.println();
		System.out.println(" - movePhoto \t-- moves the specified photo from first album specified to the second");
		System.out.println("\t Usage: movePhoto \"<photo filename>\" \"<old album name>\" \"<new album name>\"");
		System.out.println("\t NOTE: the quotation marks are necessary in order to have spaces within the input fields");
		System.out.println();
		System.out.println();
		System.out.println(" - removePhoto \t-- removes the specified photo from the specified album");
		System.out.println("\t Usage: removePhoto \"<photo filename>\" \"<album name>\"");
		System.out.println("\t NOTE: the quotation marks are necessary in order to have spaces within the input fields");
		System.out.println();
		System.out.println();
		System.out.println(" - addTag \t-- adds the specified tag to the specified photo");
		System.out.println("\t Usage: addTag \"<photo filename>\" <tagType>:\"<tagValue>\"");
		System.out.println("\t NOTE: the quotation marks are necessary in order to have spaces within the input fields");
		System.out.println();
		System.out.println();
		System.out.println(" - deleteTag \t-- removes the specified tag from the specified photo");
		System.out.println("\t Usage: deleteTag \"<photo filename>\" <tagType>:\"<tagValue>\"");
		System.out.println("\t NOTE: the quotation marks are necessary in order to have spaces within the input fields");
		System.out.println();
		System.out.println();
		System.out.println(" - listPhotoInfo \t-- lists all the saved information about the specified photo");
		System.out.println("\t Usage: listPhotoInfo \"<photo filename>\"");
		System.out.println("\t NOTE: the quotation marks are necessary in order to have spaces within the input fields");
		System.out.println();
		System.out.println();
		System.out.println(" - getPhotosByDate \t-- retrieves all photos taken within a given range of dates, in chronological order");
		System.out.println("\t Usage: getPhotosByDate <start date> <end date>");
		System.out.println("\t NOTE: dates are to be specified in MM/DD/YYYY-HH:MM:SS format, using a 24-hour clock");
		System.out.println();
		System.out.println();
		System.out.println(" - getPhotosByTag \t-- retrieves all photos that have all of the tags specified");
		System.out.println("\t Usage: getPhotosByTag [<tagType>:]\"<tagValue>\" [,[<tagType>:]\"<tagValue>\"]...date>");
		System.out.println("\t NOTE: bracketed values are optional");
		System.out.println();
		System.out.println();
	}
}
