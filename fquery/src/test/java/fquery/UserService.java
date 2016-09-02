package fquery;

import java.util.ArrayList;
import java.util.List;

public class UserService {

	private Halde halde;
	
	private Tokenizer<User> tokenizer = new UserTokenizer();

	private Index<User, String> nameIndex;
		

	public UserService(Halde halde) {
		
		this.halde = halde;
		
		
		this.nameIndex = new Index<>(tokenizer, User::getName, this.halde);
	}				
	
	public List<User> getAll() {
		
		List<User> all = new ArrayList<>();
		halde.plow(this.tokenizer).forEachRemaining(all::add);
		
		return all;
		
	}
	
	public int count(){
		return halde.reduce(this.tokenizer, Reducer.counter());
	}
	
	public User findByName(String name){
		return this.nameIndex.get(name);
	}
	
	public void deleteByName(String name){
		this.halde.read(String.format("<DELUSER name='%s' />",name) );
	}

}
