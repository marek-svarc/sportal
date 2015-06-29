package com.clubeek.service.impl;

import com.clubeek.enums.UserRoleType;
import com.clubeek.model.User;
import com.clubeek.service.SecurityService;
import com.clubeek.ui.views.Navigation;
import org.springframework.stereotype.Service;

@Service("securityService")
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
	public boolean checkRole(UserRoleType r1, UserRoleType r2) {
		return Security.checkRole(r1, r2);
	}

	@Override
	public void authorize(UserRoleType role) {
		Security.authorize(role);		
	}

}
