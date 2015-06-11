package com.clubeek.dao;

import java.util.List;

import com.clubeek.db.RepUser;
import com.clubeek.model.User;

public interface UserDao {

//	List<User> admins = RepUser.selectAllAdministrators(new RepUser.TableColumn[]{RepUser.TableColumn.ID});
	public List<User> getAllAdministrators();
	
	public void insertUser(User user);
	
	public User getUserByName(String name, boolean dependencies);
	
	public List<User> getAllUsers();
	
	// TODO vitfo, created on 11. 6. 2015 - nahradit
	public RepUser getRepUserInstance();
	
	public User getUserByClubMemberId(int id);
}
