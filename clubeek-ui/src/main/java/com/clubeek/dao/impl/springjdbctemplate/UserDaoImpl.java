package com.clubeek.dao.impl.springjdbctemplate;

import java.util.List;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

import com.clubeek.dao.UserDao;
import com.clubeek.dao.impl.springjdbctemplate.mappers.UserMapper;
import com.clubeek.enums.UserRoleType;
import com.clubeek.model.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl extends DaoImpl implements UserDao {

    @Override
    public void updateRow(User object) {
        template.getJdbcOperations().update("update t_user set "
                + "username = ?, "
                + "password = ?, "
                + "user_role_type = ?, "
                + "club_member_id = ? "
                + "where id = ?"
                , new Object[] {
                        object.getUsername(),
                        object.GetHashPassword(), 
                        object.getUserRoleType().ordinal(),
                        object.getClubMemberId(),
                        object.getId()
                });
    }

    @Override
    public void insertRow(User object) {
        template.getJdbcOperations().update("insert into t_user ("
                + "username, "
                + "password, "
                + "user_role_type, "
                + "club_member_id) values (?, ?, ?, ?)"
                , new Object[] {
                        object.getUsername(),
                        object.GetHashPassword(), 
                        object.getUserRoleType().ordinal(),
                        object.getClubMemberId()
                });        
    }

    @Override
    public void deleteRow(int id) {
        template.getJdbcOperations().update("delete from t_user where id = ?", new Object[] {id});        
    }

    @Override
    public void exchangeRows(int idA, int idB) {
        throw new UnsupportedOperationException();        
    }

    @Override
    public List<User> getAllAdministrators() {
        return template.getJdbcOperations().query(
                "select * from t_user where user_role_type = ?", 
                new Object[] {UserRoleType.ADMINISTRATOR.ordinal()},
                new UserMapper());
    }

    @Override
    public void insertUser(User user) {
        // TODO vitfo, created on 29. 6. 2015 - redundant
        insertRow(user);        
    }

    // TODO vitfo, created on 29. 6. 2015 - dependencies (fills clubMember)
    @Override
    public User getUserByName(String name, boolean dependencies) {
        return template.getJdbcOperations().queryForObject(
                "select * from t_user where username = ?", new Object[] {name}, new UserMapper());
    }

    @Override
    public List<User> getAllUsers() {
        return template.query("select * from t_user", new UserMapper());
    }

    @Override
    public User getUserByClubMemberId(int id) {
        return template.getJdbcOperations().queryForObject(
                "select * from t_user where club_member_id = ?", 
                new Object[] {id},
                new UserMapper());
    }

    @Override
    public void deleteRows(List<User> objects) {
        SqlParameterSource[] sources = new SqlParameterSourceUtils().createBatch(objects.toArray());
        template.batchUpdate("delete from t_user where id = :id", sources);        
    }

}
