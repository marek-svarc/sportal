package com.clubeek.dao.impl.springjdbctemplate;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clubeek.dao.ClubDao;
import com.clubeek.dao.ClubMemberDao;
import com.clubeek.dao.UserDao;
import com.clubeek.enums.UserRoleType;
import com.clubeek.model.User;

/**
 * Class that tests UserDaoImpl.
 *
 * @author vitfo
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-config.xml")
public class UserDaoImplTest {
    
    @Autowired
    UserDao userDao;
    @Autowired
    ClubMemberDao clubMemberDao;
    @Autowired
    ClubDao clubDao;
    
    @Before
    public void deleteAllUsers() {
        deleteAll(userDao);
    }
    
    /**
     * Tests insert, delete, update, getByClubMemberId, getByName, getAll.
     */
    @Test
    public void test() {
        assertTrue(userDao.getAllUsers().size() == 0);
        
        // insert
        insertUser(userDao, clubMemberDao, "john", "smith", UserRoleType.CLUB_MANAGER);
        assertTrue(userDao.getAllUsers().size() == 1);
        
        // getById
        int idClubMember = userDao.getAllUsers().get(0).getClubMemberId();
        User u = userDao.getUserByClubMemberId(idClubMember);
        assertTrue(u.getPassword().equals("64b0155df7d473168bd3e32b742af5ce4e10bbb7"));
        
        // getByName
        insertUser(userDao, clubMemberDao, "abc", "abcdef", UserRoleType.EDITOR);
        User uu = userDao.getUserByName("john", false);
        assertTrue(uu.getUsername().equals("john"));
        assertTrue(uu.getUserRoleType().equals(UserRoleType.CLUB_MANAGER));
        
        // update
        uu.setUsername("johnysmith");
        uu.setPassword("1234");
        uu.setUserRoleType(UserRoleType.ADMINISTRATOR);
        userDao.updateRow(uu);
        
        User uuu = userDao.getUserByName("johnysmith", false);
        assertTrue(uuu.getPassword().equals("581209ac26527ec33461758a5a728ac05b8502ca"));
        assertTrue(uuu.getUserRoleType().equals(UserRoleType.ADMINISTRATOR));
    }

    @Test
    public void testGetAllAdministrators() {
        assertTrue(userDao.getAllUsers().size() == 0);
        
        insertUser(userDao, clubMemberDao, "john", "smith", UserRoleType.CLUB_MANAGER);
        insertUser(userDao, clubMemberDao, "peter", "1234", UserRoleType.EMPTY);
        insertUser(userDao, clubMemberDao, "david", "admin", UserRoleType.ADMINISTRATOR);
        insertUser(userDao, clubMemberDao, "franta", "aS45jTf", UserRoleType.EDITOR);
        insertUser(userDao, clubMemberDao, "halalo", "querty", UserRoleType.ADMINISTRATOR);
        
        assertTrue(userDao.getAllUsers().size() == 5);
        assertTrue(userDao.getAllAdministrators().size() == 2);
    }

    @Test
    public void deleteRowsTest() {
        insertUsers(userDao, clubMemberDao, 10);
        assertTrue(userDao.getAllUsers().size() == 10);
        List<User> usersToDelete = userDao.getAllUsers().subList(3, 8);
        userDao.deleteRows(usersToDelete);
        assertTrue(userDao.getAllUsers().size() == 5);
    }
    
    public void insertUser(UserDao userDao, ClubMemberDao clubMemberDao, String username, String password, UserRoleType userRoleType) {
        ClubMemberDaoImplTest clubMemberTest = new ClubMemberDaoImplTest();
        clubMemberTest.insertClubMember(
                clubMemberDao, 
                clubDao,
                UUID.randomUUID().toString(), 
                UUID.randomUUID().toString(), 
                new Date(1_000_000L));
        int clubMemberId = clubMemberDao.getAllClubMembers().get(0).getId();
        insertUser(userDao, username, password, userRoleType, clubMemberId);
    }
    
    public void insertUser(UserDao userDao, String username, String password, UserRoleType userRoleType, int clubMemberId) {
        User u = new User();
        
        u.setUsername(username);
        u.setPassword(password);
        u.setUserRoleType(userRoleType);
        u.setClubMemberId(clubMemberId);
        
        userDao.insertRow(u);
    }
    
    public void insertUsers(UserDao userDao, ClubMemberDao clubMemberDao, int numOfUsers) {
        for (int i = 0; i < numOfUsers; i++) {
            insertUser(
                    userDao, 
                    clubMemberDao, 
                    UUID.randomUUID().toString(), 
                    UUID.randomUUID().toString(), 
                    UserRoleType.EMPTY);
        }
    }

    public void deleteAll(UserDao userDao) {
        for (User u : userDao.getAllUsers()) {
            userDao.deleteRow(u.getId());
        }
    }
}
