package guiview;

import java.awt.Component;
import java.awt.Image;

import javax.swing.*;

import cs213.photoAlbum.model.Photo;
import cs213.photoAlbum.model.Tag;

/**
 * Custom CellRenderer for displaying Tag object data in JList
 *  
 * @author Ananth Rao
 * @author Allon Fineziber
 *
 */
public class TagRenderer extends DefaultListCellRenderer {
	
	public TagRenderer() {
		super();
	}
	
	@Override
	public Component getListCellRendererComponent(
	    JList list, Object value, int index,
	    boolean isSelected, boolean cellHasFocus) {
		
		Tag tag = ((Tag) value);
		String type = tag.getType();
		if(type.isEmpty()) {
			type = "ALL TYPES";
		}
		value = type + ":" + tag.getValue();
		
		return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	}
}
