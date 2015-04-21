package cs213.photoAlbum.model;

import java.io.Serializable;

public interface UserInterface extends Serializable {
	
	public void createAlbum(String name);
	public void deleteAlbum(String name);
	public void renameAlbum(String oldName, String newName);
	public Album getAlbum(String name);
	public Photo getPhoto(String filename);
	

}