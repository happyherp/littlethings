package mypack;

import java.util.List;

public class UserService {
	
	UserDAO dao;
	PostDAO postDAO;
	
	public UserService(UserDAO dao, PostDAO postDAO) {
		this.dao = dao;
		this.postDAO = postDAO;
	}

	public UserService() {
		// TODO Auto-generated constructor stub
	}

	public void deleteUser(User user){
		postDAO.deleteByAuthor(user);
		dao.delete(user);
	}

	public void saveUser(User mark) {
		// TODO Auto-generated method stub
		
	}

	public void writePost(User mark, Post post) {
		// TODO Auto-generated method stub
		
	}

	public Object findByName(String string) {
		// TODO Auto-generated method stub
		return null;
	}

}
