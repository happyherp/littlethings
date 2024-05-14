package mypack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

public class UserServiceTest {
	
	
	@Test
	public void testDeleteMock(){
		UserDAO daoMock = EasyMock.createMock(UserDAO.class);
		PostDAO postDaoMock = EasyMock.createStrictMock(PostDAO.class);
		UserService service = new UserService(daoMock, postDaoMock);
		
		User mark = new User();
		daoMock.delete(mark);
		EasyMock.expectLastCall().once();
		
		List<Post> marksPosts = Collections.singletonList(new Post());
		EasyMock.expect(postDaoMock.findByAuthor(mark)).andReturn(marksPosts);
		postDaoMock.delete(marksPosts.get(0));
		EasyMock.expectLastCall();
		
		EasyMock.replay(daoMock, postDaoMock);
		
		service.deleteUser(mark);
		
		EasyMock.verify(daoMock, postDaoMock);
	}
	
	@Test
	public void testDelete(){
		UserService service = new UserService();
		
		User mark = new User();
		mark.setName("Mark");
		service.saveUser(mark);		
		
		Assert.assertNotNull(service.findByName("Mark"));
		
		service.writePost(mark, new Post("First!"));
		
		service.deleteUser(mark);
		Assert.assertNull(service.findByName("Mark"));
	}

}
