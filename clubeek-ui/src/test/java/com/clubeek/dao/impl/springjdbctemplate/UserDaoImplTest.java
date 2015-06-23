package com.clubeek.dao.impl.springjdbctemplate;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clubeek.dao.UserDao;
import com.clubeek.enums.UserRoleType;
import com.clubeek.model.User;

/**
 * Class that tests UserDaoImpl.
 *
 * @author vitfo
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("test-config.xml")
public class UserDaoImplTest {
    
    @Autowired
    UserDao userDao;
    
    @Before
    public void deleteAllUsers() {
        // TODO vitfo, created on 23. 6. 2015 
//        deleteAll(userDao);
    }
    
    /**
     * Tests insert, delete, update.
     */
    @Test
    public void test() {
        // TODO vitfo, created on 23. 6. 2015 
    }

//    @Test
//    public void testUpdateRow() {
//        fail("Not yet implemented");
//    }
//
//    @Test
//    public void testInsertRow() {
//        fail("Not yet implemented");
//    }
//
//    @Test
//    public void testDeleteRow() {
//        fail("Not yet implemented");
//    }
//
//    @Test
//    public void testExchangeRows() {
//        fail("Not yet implemented");
//    }
//
//    @Test
//    public void testGetAllAdministrators() {
//        fail("Not yet implemented");
//    }
//
//    @Test
//    public void testInsertUser() {
//        fail("Not yet implemented");
//    }
//
//    @Test
//    public void testGetUserByName() {
//        fail("Not yet implemented");
//    }
//
//    @Test
//    public void testGetAllUsers() {
//        fail("Not yet implemented");
//    }
//
//    @Test
//    public void testGetUserByClubMemberId() {
//        fail("Not yet implemented");
//    }
    
    public void insertUser(UserDao userDao, String username, String password, UserRoleType userRoleType, int clubMemberId) {
        User u = new User();
        
        u.setUsername(username);
        u.setPassword(password);
        u.setUserRoleType(userRoleType);
        u.setClubMemberId(clubMemberId);
        
        userDao.insertRow(u);
    }

    public void deleteAll(UserDao userDao) {
        for (User u : userDao.getAllUsers()) {
            userDao.deleteRow(u.getId());
        }
    }
}
