package fquery;

public class HighscoreEntry {

	private User user;
	private int posts;
	
	public HighscoreEntry(User next, int size) {
		this.user = next;
		this.posts = size;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public int getPosts() {
		return posts;
	}
	public void setPosts(int posts) {
		this.posts = posts;
	}
	
	public String toString(){
		return this.user.getName()+":"+this.getPosts();
	}
	
}
