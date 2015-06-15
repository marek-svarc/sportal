package com.clubeek.service.impl;

import com.clubeek.model.User;
import com.clubeek.model.User.Role;
import com.clubeek.service.SecurityService;
import com.clubeek.ui.views.Navigation;

public class SecurityServiceImpl implements SecurityService {

	@Override
	public User getUser() {
		return Security.getUser();
	}

	@Override
	public void login(Navigation navigation, String username, String password) {
		Security.login(navigation, username, password);		
	}

	@Override
	public void logout(Navigation navigation) {
		Security.logout(navigation);		
	}

	@Override
	public boolean checkRole(Role r1, Role r2) {
		return Security.checkRole(r1, r2);
	}

	@Override
	public void authorize(Role role) {
		Security.authorize(role);		
	}

}
