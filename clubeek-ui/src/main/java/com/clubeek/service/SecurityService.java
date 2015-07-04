package com.clubeek.service;

import com.clubeek.enums.UserRoleType;
import com.clubeek.model.User;
import com.clubeek.ui.views.Navigation;

public interface SecurityService {

	public User getUser();
	
	public void login(Navigation navigation, String username, String password);
	
	public void logout(Navigation navigation);
	
	public boolean checkRole(UserRoleType r1, UserRoleType r2);
	
	public void authorize(UserRoleType role);
}
