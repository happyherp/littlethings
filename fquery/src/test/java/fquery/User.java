package fquery;

public class User {
	
	private String name;
	private Integer age;
	public User(String string, int i) {
		this.name = string;
		this.age = i;
	}
	public User() {
		// TODO Auto-generated constructor stub
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}

}
