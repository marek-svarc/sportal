package com.clubeek.dao.impl.springjdbctemplate;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clubeek.dao.ClubSettingDao;
import com.clubeek.model.ClubSetting;

/**
* Class that tests ClubSettingDaoImpl.
* 
* @author vitfo
*
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-config.xml")
public class ClubSettingDaoImplTest {
    
    @Autowired
    ClubSettingDao clubSettingDao;
    
    @Before
    public void deleteAllClubSettings() {
        deleteAll(clubSettingDao);
    }
    
    /**
     * Tests getClubSettingById, updateClubSetting, getAll, delete, insert.
     */
    @Test
    public void test() {
        assertTrue(clubSettingDao.getAllClubSettings().size() == 0);
        
        // insert
        insertClubSetting(clubSettingDao, "My title", "My comment", Tools.getImageAsBytes("src/test/java/com/clubeek/dao/impl/springjdbctemplate/logo.png"));
        assertTrue(clubSettingDao.getAllClubSettings().size() == 1);
        
        // getById
        int idClubSetting = clubSettingDao.getAllClubSettings().get(0).getId();
        insertClubSetting(clubSettingDao, "My other title", "My other comment", Tools.getImageAsBytes("src/test/java/com/clubeek/dao/impl/springjdbctemplate/logo2.png"));
        assertTrue(clubSettingDao.getAllClubSettings().size() == 2);
        
        ClubSetting cs = clubSettingDao.getClubSettingById(idClubSetting);
        assertTrue(cs.getTitle().equals("My title"));
        assertTrue(cs.getComment().equals("My comment"));
        assertTrue(cs.getLogo().length == Tools.getImageAsBytes("src/test/java/com/clubeek/dao/impl/springjdbctemplate/logo.png").length);
        
        // update
        cs.setTitle("Abcd");
        cs.setComment("Bla bla bla");
        cs.setLogo(Tools.getImageAsBytes("src/test/java/com/clubeek/dao/impl/springjdbctemplate/logo2.png"));
        clubSettingDao.updateClubSetting(cs);
        
        ClubSetting cs2 = clubSettingDao.getClubSettingById(idClubSetting);
        assertTrue(cs2.getTitle().equals("Abcd"));
        assertTrue(cs2.getComment().equals("Bla bla bla"));
        assertTrue(cs2.getLogo().length == Tools.getImageAsBytes("src/test/java/com/clubeek/dao/impl/springjdbctemplate/logo2.png").length);
    }

    public void insertClubSetting(ClubSettingDao clubSettingDao, String title, String comment, byte[] logo) {
        ClubSetting cs = new ClubSetting();
        cs.setTitle(title);
        cs.setComment(comment);
        cs.setLogo(logo);
        clubSettingDao.addClubSetting(cs);
    }
    
    public void deleteAll(ClubSettingDao clubSettingDao) {
        for (ClubSetting cs : clubSettingDao.getAllClubSettings()) {
            clubSettingDao.deleteClubSetting(cs.getId());
        }
    }
}
