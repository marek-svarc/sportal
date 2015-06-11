package com.clubeek.dao.impl.ownframework;

import java.util.List;

import com.clubeek.dao.UserDao;
import com.clubeek.db.RepUser;
import com.clubeek.model.User;

public class UserDaoImpl implements UserDao {

	@Override
	public List<User> getAllAdministrators() {
		return RepUser.selectAllAdministrators(new RepUser.TableColumn[]{RepUser.TableColumn.ID});
	}

	@Override
	public void insertUser(User user) {
		RepUser.insert(user);		
	}

	@Override
	public User getUserByName(String name, boolean dependencies) {
		return RepUser.selectByName(name, dependencies, null);
	}

	@Override
	public List<User> getAllUsers() {
		return RepUser.selectAll(new RepUser.TableColumn[]{RepUser.TableColumn.ID, RepUser.TableColumn.NAME,
	            RepUser.TableColumn.PERMISSIONS, RepUser.TableColumn.CLUB_MEMBER_ID});
	}

	@Override
	public RepUser getRepUserInstance() {
		return RepUser.getInstance();
	}

	@Override
	public User getUserByClubMemberId(int id) {
		return RepUser.selectByClubMemberId(id, new RepUser.TableColumn[] {
                RepUser.TableColumn.ID, RepUser.TableColumn.CLUB_MEMBER_ID });
	}

}
