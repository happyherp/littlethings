package fquery;

import java.io.Serializable;

public class Post implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public Post(String username, String text) {
		super();
		this.username = username;
		this.text = text;
	}
	String username;
	String text;


	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
