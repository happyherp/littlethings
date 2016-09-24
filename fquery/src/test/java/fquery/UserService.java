package fquery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class UserService {

	Halde halde;
	
	Tokenizer<User> tokenizer = new UserTokenizer();

	Index<User, String> nameToUserIndex;
	Index<Post, String> nameToPostIndex;
		

	public UserService(Halde halde) {
		
		this.halde = halde;
		
		
		this.nameToUserIndex = new Index<>(tokenizer, User::getName, this.halde);
		this.nameToPostIndex = new Index<Post, String>(new SerializeTokenizer<Post>(Post.class), Post::getUsername, this.halde);
	}			
	
	public void addUser(User user){
		halde.read(String.format("<NEWUSER name='%s' age='%d' />", user.getName(), user.getAge()));
	}
	
	public List<User> getAll() {
		
		List<User> all = new ArrayList<>();
		halde.plow(this.tokenizer).forEachRemaining(all::add);
		
		return all;
		
	}
	
	public int count(){
		return halde.reduce(this.tokenizer, Reducer.counter());
	}
	
	public Collection<User> findByName(String name){
		return this.nameToUserIndex.get(name);
	}
	
	public void deleteByName(String name){
		this.halde.read(String.format("<DELUSER name='%s' />",name) );
	}

	public Collection<UserWithPost> joinUsersWithPosts() {
		
		Collection<UserWithPost> joined =  
				Join.<Collection<UserWithPost>,String,User,Post>join(nameToUserIndex, nameToPostIndex,this::combine)
				.stream().flatMap(Collection::stream)
				.collect(Collectors.toList());
		
		
		return joined;
	}
	
	private List<UserWithPost> combine(Collection<User> values1,
			Collection<Post> values2) {
		
		List<UserWithPost> crossproduct = new ArrayList<>();
		for (User user: values1){
			for (Post post: values2){
				crossproduct.add(new UserWithPost(user, post));
			}
		}						
		return crossproduct;
	}

	public List<User> under25() {
		
		
		
		
		
		return null;
	}
	


}
