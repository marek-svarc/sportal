package com.clubeek.dao.impl.springjdbctemplate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clubeek.dao.ClubDao;
import com.clubeek.dao.ClubMemberDao;
import com.clubeek.dao.ClubTeamDao;
import com.clubeek.dao.ClubUrlDao;
import com.clubeek.enums.LicenceType;
import com.clubeek.model.ClubUrl;

/**
* Class that tests ClubUrlDaoImpl.
* 
* @author vitfo
*
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-config.xml")
public class ClubUrlDaoImplTest {
    
    @Autowired
    ClubUrlDao clubUrlDao;
    @Autowired
    ClubDao clubDao;
    @Autowired
    ClubTeamDao clubTeamDao;
    @Autowired
    ClubMemberDao clubMemberDao;
    
    @Before
    public void deleteAllClubs() {
        deleteAll(clubUrlDao);
    }
    
    /**
     * Tests getAll, delete, insert, update.
     */
    @Test
    public void test() {
        assertTrue(clubUrlDao.getAllClubUrls().size() == 0);
        
        // insert
        insertClubUrl(clubUrlDao, clubDao, "mojeUrl");
        assertTrue(clubUrlDao.getAllClubUrls().size() == 1);
        ClubUrl cu = clubUrlDao.getAllClubUrls().get(0);
        assertTrue(cu.getUrl().equals("mojeUrl"));
        
        // update
        cu.setUrl("http://abc.efg");
        clubUrlDao.updateClubUrl(cu);
        ClubUrl cu2 = clubUrlDao.getAllClubUrls().get(0);
        assertTrue(cu2.getUrl().equals("http://abc.efg"));
        
        // delete
        clubUrlDao.deleteClubUrl(cu2.getId());
        assertTrue(clubUrlDao.getAllClubUrls().size() == 0);
    }

    /**
     * Tests getAllClubUrls, getClubId
     */
    @Test
    public void testGetClubIdAndAllClubUrls() {
        assertTrue(clubUrlDao.getAllClubUrls().size() == 0);
        ClubDaoImplTest clubTest = new ClubDaoImplTest();
        clubTest.deleteAll(clubDao, clubTeamDao, clubMemberDao);
        assertTrue(clubDao.getAllClubs().size() == 0);
        
        // insert one
        insertClubUrl(clubUrlDao, clubDao, "mojeUrl");
        assertTrue(clubUrlDao.getAllClubUrls().size() == 1);
        int clubId = clubUrlDao.getAllClubUrls().get(0).getClubId();
        
        // insert second (different club id)
        insertClubUrl(clubUrlDao, clubDao, "http://123.456");
        assertTrue(clubUrlDao.getAllClubUrls().size() == 2);
        
        // insert third (same club id)
        insertClubUrl(clubUrlDao, clubDao, "mojeDalsiUrl", clubId);
        assertTrue(clubUrlDao.getAllClubUrls().size() == 3);
        
        // get by club id
        Integer i = clubUrlDao.getClubId("mojeDalsiUrl");
        assertNotNull(i);
        assertTrue(i == clubId);
        Integer ii = clubUrlDao.getClubId("notExistingUrl");
        assertNull(ii);
        
        // get all urls
        List<ClubUrl> urls = clubUrlDao.getAllClubUrls(clubId);
        assertTrue(urls.size() == 2);
    }
    
    public void insertClubUrl(ClubUrlDao clubUrlDao, ClubDao clubDao, String url, int clubId) {
        ClubUrl cu = new ClubUrl();
        cu.setUrl(url);
        cu.setClubId(clubId);
        clubUrlDao.addClubUrl(cu);
    }
    
    public void insertClubUrl(ClubUrlDao clubUrlDao, ClubDao clubDao, String url) {
        ClubDaoImplTest clubTest = new ClubDaoImplTest();
        clubTest.insertClub(clubDao, LicenceType.PROFESSIONAL, null, null, null);
        Integer clubId = null;
        if (clubDao.getAllClubs().size() <= 1) {
            clubId = clubDao.getAllClubs().get(0).getId();
        } else {
            clubId = clubDao.getAllClubs().get(1).getId();
        }
        
        insertClubUrl(clubUrlDao, clubDao, url, clubId);
    }

    public void deleteAll(ClubUrlDao clubUrlDao) {
        for (ClubUrl cu : clubUrlDao.getAllClubUrls()) {
            clubUrlDao.deleteClubUrl(cu.getId());
        }
    }
}
