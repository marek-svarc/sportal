package com.clubeek.dao.impl.springjdbctemplate;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clubeek.dao.ClubDao;
import com.clubeek.model.Club;

/**
* Class that tests ClubDaoImpl.
* 
* @author vitfo
*
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-config.xml")
public class ClubDaoImplTest {
    
    @Autowired
    ClubDao clubDao;
    
    @Before
    public void deleteAllClubs() {
        deleteAll(clubDao);
    }
    
    /**
     * Tests getClubById, updateClub, getAll, delete, insert.
     */
    @Test
    public void test() {
        assertTrue(clubDao.getAllClubs().size() == 0);
        
        // insert
        insertClub(clubDao, "My title", "My comment", Tools.getImageAsBytes("src/test/java/com/clubeek/dao/impl/springjdbctemplate/logo.png"));
        assertTrue(clubDao.getAllClubs().size() == 1);
        
        // getById
        int idClub = clubDao.getAllClubs().get(0).getId();
        insertClub(clubDao, "My other title", "My other comment", Tools.getImageAsBytes("src/test/java/com/clubeek/dao/impl/springjdbctemplate/logo2.png"));
        assertTrue(clubDao.getAllClubs().size() == 2);
        
        Club cs = clubDao.getClubById(idClub);
        assertTrue(cs.getTitle().equals("My title"));
        assertTrue(cs.getComment().equals("My comment"));
        assertTrue(cs.getLogo().length == Tools.getImageAsBytes("src/test/java/com/clubeek/dao/impl/springjdbctemplate/logo.png").length);
        
        // update
        cs.setTitle("Abcd");
        cs.setComment("Bla bla bla");
        cs.setLogo(Tools.getImageAsBytes("src/test/java/com/clubeek/dao/impl/springjdbctemplate/logo2.png"));
        clubDao.updateClub(cs);
        
        Club cs2 = clubDao.getClubById(idClub);
        assertTrue(cs2.getTitle().equals("Abcd"));
        assertTrue(cs2.getComment().equals("Bla bla bla"));
        assertTrue(cs2.getLogo().length == Tools.getImageAsBytes("src/test/java/com/clubeek/dao/impl/springjdbctemplate/logo2.png").length);
    }

    public void insertClub(ClubDao clubDao, String title, String comment, byte[] logo) {
        Club cs = new Club();
        cs.setTitle(title);
        cs.setComment(comment);
        cs.setLogo(logo);
        clubDao.addClub(cs);
    }
    
    public void deleteAll(ClubDao clubDao) {
        for (Club cs : clubDao.getAllClubs()) {
            clubDao.deleteClub(cs.getId());
        }
    }
}
