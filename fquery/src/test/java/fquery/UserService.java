package fquery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

public class UserService {

	Halde halde;
	
	Tokenizer<User> inserttokenizer = new UserTokenizer();

	Flatmapping<RawData, User> userinsertmap;
	Flatmapping<RawData, Post> postmap;
	Flatmapping<RawData, Userdeletion> userdeletemap;

	Index<User, String> nameToUser;
	Index<Post, String> nameToPostIndex;
	Index<User, Integer> userAgeIndex;
	Index<Userdeletion, String> userdelNameIndex;
	
	
	Filter<User> ageEvenFilter;

	private ChangingList<User> currentUsers;

	private Index<User, String> nameToInsertIndex;


		

	public UserService(Halde halde) {
		
		this.halde = halde;
		this.userinsertmap = Tokenizer.doMap(halde, inserttokenizer);
		this.nameToInsertIndex = new Index<>(userinsertmap, User::getName);			
		this.userdeletemap = Tokenizer.doMap(halde, new UserDeleteTokenizer());
		this.userdelNameIndex = new Index<>(this.userdeletemap, Userdeletion::getName);
		
		this.currentUsers = new Filter<User>(
								new Join<>(this.nameToInsertIndex, 
													   this.userdelNameIndex, 
													   this::createCurrentUser),
								u->u!=null);
		this.nameToUser = new Index<>(currentUsers, User::getName);	
		
		
		this.postmap = Tokenizer.doMap(halde, new SerializeTokenizer<Post>(Post.class));
		this.nameToPostIndex = new Index<Post, String>(this.postmap, Post::getUsername);
		
		
		this.userAgeIndex = new Index<>(this.currentUsers, User::getAge);
		this.ageEvenFilter = new Filter<User>(this.currentUsers, u -> u.getAge() % 2 == 0);
	}			
	
	public void addUser(User user){
		halde.read(String.format("<NEWUSER name='%s' age='%d' />", user.getName(), user.getAge()));
	}
	
	public List<User> getAll() {
		return Lists.newArrayList(currentUsers.iterator());
		
	}
	
	public int count(){
		return Reducer.reduce(currentUsers,Reducer.counter());
	}
	
	public Collection<User> findByName(String name){
		return this.nameToUser.get(name);
	}
	
	public void deleteByName(String name){
		this.halde.read(String.format("<DELUSER name='%s' />",name) );
	}

	public Collection<UserWithPost> joinUsersWithPosts() {		
		Collection<UserWithPost> joined =  
				Join.<Collection<UserWithPost>,String,User,Post>matchJoin(
						nameToUser, nameToPostIndex,this::combine)
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
	
	private User createCurrentUser(Collection<User> insertions, Collection<Userdeletion> deletions){
		
		if(insertions.size() > 2){
			throw new RuntimeException("More thatn one insertion for same user. ");
		}
		
		if (insertions.size() == 1){
			if (deletions.size() == 0){
				return insertions.iterator().next();
			}else{
				return null;
			}
		}
		
		
		return null;
	}

	public Collection<User> under25() {
		return userAgeIndex.lowerThan(25);
	}

	public List<User> evenAge() {		
		return Lists.newArrayList(ageEvenFilter.iterator());				
	}
	


}
