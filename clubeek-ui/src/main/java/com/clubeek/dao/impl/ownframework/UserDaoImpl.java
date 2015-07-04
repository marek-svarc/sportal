package com.clubeek.dao.impl.ownframework;

import java.util.List;

import com.clubeek.dao.UserDao;
import com.clubeek.dao.impl.ownframework.rep.RepUser;
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
	public User getUserByClubMemberId(int id) {
		return RepUser.selectByClubMemberId(id, new RepUser.TableColumn[] {
                RepUser.TableColumn.ID, RepUser.TableColumn.CLUB_MEMBER_ID });
	}

    @Override
    public void updateRow(User object) {
        RepUser.update(object);
    }

    @Override
    public void insertRow(User object) {
        RepUser.insert(object);        
    }

    @Override
    public void deleteRow(int id) {
        RepUser.delete(id);        
    }

    @Override
    public void exchangeRows(int idA, int idB) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void deleteRows(List<User> objects) {
        throw new UnsupportedOperationException("Not supported.");
    }

}
