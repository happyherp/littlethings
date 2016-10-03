package fquery;

import java.util.Collection;
import java.util.List;
import java.util.Random;

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
		
		Assert.assertEquals(10, userquery.countUsers());
		
		CachedReduction<User, Integer> cachedCount = new CachedReduction<>(
				Reducer.counter(), 
				userquery.userinsertmap);
		
		Assert.assertEquals(10, (int) cachedCount.getResult());

		User u190 = userquery.findByName("Number_107").iterator().next();
		Assert.assertNotNull(u190);
		
		userquery.addUser(new User("Carlos", 28));
		Assert.assertNotNull(userquery.findByName("Carlos"));
		Assert.assertEquals(11, (int) cachedCount.getResult());
		
		List<User> evenAge = userquery.evenAge();
		Assert.assertEquals(6, evenAge.size());
		
		Collection<User> under25 = userquery.underAge(25);
		Assert.assertEquals(5, under25.size());

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
		Assert.assertEquals(0,userquery.userdeletemap.stream().count());		
		halde.read("<NEWUSER name='Carlos' age='28' />");
		Assert.assertNotNull(userquery.findByName("Carlos"));
		
		Assert.assertEquals(0,userquery.userdeletemap.stream().count());
		userquery.deleteByName("Carlos");
		Assert.assertEquals(1,userquery.userdeletemap.stream().count());
		Assert.assertNull(userquery.findByName("Carlos"));
	}
	
	@Test
	public void testMostWrites(){
		Halde halde = new Halde();
		UserService service = new UserService(halde);
		
		Random random = new Random(4455);
		
		int totalUsers = 2000;
		
		for (int i = 0;i<totalUsers;i++){
			User user = new User();
			user.setName("User#"+i);
			user.setAge(i+10);
			service.addUser(user);
			
			for (int j = random.nextInt(100); j > 0;j-- ){
				Post post = new Post(user.getName(), "I am post number "+j);
				service.addPost(post);
			}
		}
		
		Assert.assertEquals(totalUsers, service.countUsers());
		Assert.assertTrue(service.countPosts() > totalUsers*45);
		Assert.assertTrue(service.countPosts() < totalUsers*55);
		
		List<HighscoreEntry> top10Users = service.getTop10Posters();
		Assert.assertEquals(10, top10Users.size());
		
		for (int i = 1;i<top10Users.size();i++){
			Assert.assertTrue(top10Users.get(i-1).getPosts() >= top10Users.get(i).getPosts());
		}
		 
	}
	
	

	
}
