package com.clubeek.service;

import com.clubeek.model.User;
import com.clubeek.model.User.Role;
import com.clubeek.ui.views.Navigation;

public interface SecurityService {

	public User getUser();
	
	public void login(Navigation navigation, String username, String password);
	
	public void logout(Navigation navigation);
	
	public boolean checkRole(Role r1, Role r2);
	
	public void authorize(Role role);
}
