package cs213.photoAlbum.model;

import java.io.Serializable;

public interface AlbumInterface extends Serializable {
	
	public void addPhoto(String filename);
	public void setCaption(String filename, String caption);
	

}