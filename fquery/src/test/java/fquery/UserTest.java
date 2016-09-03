package fquery;

import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class UserTest {

	
	@Test
	public void testInsertIter(){
		
		StringBuilder builder = new StringBuilder();
		for(int i = 0;i<10;i++){
			builder.append("<NEWUSER name='Number_"+(i+99+"' age='"+(i+20))+"' />");
		}		
		
		Halde halde = new Halde();
		halde.read(builder.toString());
		
		UserService userquery = new UserService(halde);
		List<User> users = userquery.getAll();
		Assert.assertEquals(10, users.size());
		
		Assert.assertEquals(10, userquery.count());
		
		User u190 = userquery.findByName("Number_107").iterator().next();
		Assert.assertNotNull(u190);
		
		halde.read("<NEWUSER name='Carlos' age='28' />");
		Assert.assertNotNull(userquery.findByName("Carlos"));
		
		

	}
	
	@Test
	public void join(){
		
		Halde halde = new Halde();
		for(int i = 0;i<10;i++){
			String name = "Number_"+(i+99);
			halde.read("<NEWUSER name='"+name+"' age='"+(i+20)+"' />");
			halde.read(new Post(name,"I am "+name));
			halde.read(new Post(name,"I love pie"));
			
		}		
				
		UserService userquery = new UserService(halde);
		
		Collection<UserWithPost> posts = userquery.joinUsersWithPosts();
		
		Assert.assertEquals(20, posts.size());
		
		
		
	}
	
	@Test
	public void testDelete(){
		Halde halde = new Halde();
		
		UserService userquery = new UserService(halde);
		halde.read("<NEWUSER name='Carlos' age='28' />");
		Assert.assertNotNull(userquery.findByName("Carlos"));
		
		userquery.deleteByName("Carlos");
		Assert.assertNull(userquery.findByName("Carlos"));
	}
	

	
}
