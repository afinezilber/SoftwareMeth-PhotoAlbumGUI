package guiview;

import java.awt.Component;

import javax.swing.*;

import cs213.photoAlbum.model.Album;

/**
 * Custom CellRenderer for displaying Album object data in JList
 *  
 * @author Ananth Rao
 * @author Allon Fineziber
 *
 */
public class AlbumRenderer extends DefaultListCellRenderer {
	public ImageIcon folderIcon;
	
	public AlbumRenderer() {
		super();
		folderIcon = new ImageIcon("img/folder.png");
		System.out.println(folderIcon.getIconHeight());
	}
	
	@Override
	public Component getListCellRendererComponent(
	    JList list, Object value, int index,
	    boolean isSelected, boolean cellHasFocus) {
		
		value = ((Album) value).getName();
		
		JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		
		label.setIcon(folderIcon);
		label.setVerticalTextPosition(JLabel.BOTTOM);
		label.setHorizontalTextPosition(JLabel.CENTER);
		
		return label;
	}
}
