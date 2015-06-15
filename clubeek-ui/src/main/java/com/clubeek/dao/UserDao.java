package com.clubeek.dao;

import java.util.List;

import com.clubeek.dao.impl.ownframework.rep.RepUser;
import com.clubeek.model.Article;
import com.clubeek.model.User;

public interface UserDao extends Dao<User> {

	public List<User> getAllAdministrators();
	
	public void insertUser(User user);
	
	public User getUserByName(String name, boolean dependencies);
	
	public List<User> getAllUsers();
	
	public User getUserByClubMemberId(int id);
}
