package cs213.photoAlbum.model;

import java.io.Serializable;

/**
 * Class that represents a tag consisting of type and value
 * Useful for holding information
 * @author Ananth Rao
 * @author Allon Fineziber
 *
 */
public class Tag implements Comparable<Tag>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9012429078954385239L;
	
	/**
	 * Type of tag
	 */
	public String type;
	
	/**
	 * Value of tag
	 */
	public String value;
	
	/**
	 * Constructor
	 * @param type
	 * @param value
	 */
	public Tag(String type, String value) {
		this.type = type;
		this.value = value;
	}
	
	/**
	 * Sets type for tag
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * Sets value to tag
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * Returns the tag type
	 * @return
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Returns tag value
	 * @param value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * 
	 */
	@Override
	public int compareTo(Tag tag) {
		
		if(tag == null) {
			return 0;			
		}
		
		if(tag.getType().equalsIgnoreCase("location")) {
			if(!this.type.equalsIgnoreCase("location")) {
				return 1;
			}
		} else if(this.type.equalsIgnoreCase("location")) {
			return -1;
		} else if(tag.getType().equalsIgnoreCase("person")) {
			if(!this.type.equalsIgnoreCase("person")) {
				return 1;
			}
		} else if(this.type.equalsIgnoreCase("person")) {
				return -1;
		}
		
		return this.value.compareToIgnoreCase(tag.getValue());
	}
	
	/**
	 * 
	 */
	@Override
	public boolean equals(Object other) {
		if(other instanceof Tag) {
			return ((type.equals(((Tag) other).type))&&(value.equals(((Tag) other).value)));
		}
		return false;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return type + ":" + value;
	}

}