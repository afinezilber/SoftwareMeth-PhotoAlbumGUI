package guiview;

import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.*;

import cs213.photoAlbum.model.Album;
import cs213.photoAlbum.model.Photo;

/**
 * Custom CellRenderer for displaying Photo object data in JList
 *  
 * @author Ananth Rao
 * @author Allon Fineziber
 *
 */
public class PhotoRenderer extends DefaultListCellRenderer {
	
	public PhotoRenderer() {
		super();
	}
	
	@Override
	public Component getListCellRendererComponent(
	    JList list, Object value, int index,
	    boolean isSelected, boolean cellHasFocus) {
		
		Photo photo = ((Photo) value);
		value = photo.getCaption() + " - " + photo.getName();
		
		JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		
		Image photoImg = new ImageIcon((String) photo.getName()).getImage();
		ImageIcon photoImgIcon = new ImageIcon(photoImg.getScaledInstance(200, 100, Image.SCALE_FAST));
		
		label.setIcon(photoImgIcon);
		label.setVerticalTextPosition(JLabel.BOTTOM);
		label.setHorizontalTextPosition(JLabel.CENTER);
		
		return label;
	}
}
