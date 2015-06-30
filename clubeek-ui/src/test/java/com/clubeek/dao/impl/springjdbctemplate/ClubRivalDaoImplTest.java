package com.clubeek.dao.impl.springjdbctemplate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clubeek.dao.ClubRivalDao;
import com.clubeek.model.ClubRival;

/**
 * Class that tests ClubRivalDaoImpl.
 * 
 * @author vitfo
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-config.xml")
public class ClubRivalDaoImplTest {
    
    @Autowired
    ClubRivalDao clubRivalDao;
    
    @Before
    public void deleteAllClubRivals() {
        deleteAll(clubRivalDao);
    }
    
    /**
     * Tests insert, update, select, delete, getById, getAll.
     */
    @Test
    public void test() {
        assertTrue(clubRivalDao.getAllClubRivals().size() == 0);
        
        byte[] image = Tools.getImageAsBytes("src/test/java/com/clubeek/dao/impl/springjdbctemplate/logo.png");
        int imageLength = image.length;
        
        insertClubRival(clubRivalDao, "My club 01", "Ostrava", "http://www.myclub01.cz", image);
        assertTrue(clubRivalDao.getAllClubRivals().size() == 1);
        
        int rivalId = clubRivalDao.getAllClubRivals().get(0).getId();
        insertClubRival(clubRivalDao, "My club 02", "Prague", "http://www.myclub02.cz", null);
        insertClubRival(clubRivalDao, "My club 03", "Prague", "http://www.myclub03.cz", null);
        insertClubRival(clubRivalDao, "My club 04", "Prague", "http://www.myclub04.cz", null);
        assertTrue(clubRivalDao.getAllClubRivals().size() == 4);
        
        ClubRival cr = clubRivalDao.getClubRivalById(rivalId);
        assertNotNull(cr);
        assertTrue(cr.getName().equals("My club 01"));
        assertTrue(cr.getCity().equals("Ostrava"));
        assertTrue(cr.getWeb().equals("http://www.myclub01.cz"));
        assertTrue(cr.getIcon().length == imageLength);
        
        // update
        cr.setCity("Opava");
        cr.setIcon(null);
        clubRivalDao.updateRow(cr);
        ClubRival clr = clubRivalDao.getClubRivalById(rivalId);
        assertTrue(clr.getName().equals("My club 01"));
        assertTrue(clr.getCity().equals("Opava"));
        assertNull(clr.getIcon());
        
        // delete
        clubRivalDao.deleteRow(rivalId);
        assertTrue(clubRivalDao.getAllClubRivals().size() == 3);
    }
    
    @Test
    public void deleteRowsTest() {
        assertTrue(clubRivalDao.getAllClubRivals().size() == 0);
        
        insertClubRivals(clubRivalDao, 10);
        assertTrue(clubRivalDao.getAllClubRivals().size() == 10);
        List<ClubRival> rivalToDelete = clubRivalDao.getAllClubRivals().subList(3, 8);
        clubRivalDao.deleteRows(rivalToDelete);
        assertTrue(clubRivalDao.getAllClubRivals().size() == 5);
    }

    public void insertClubRival(ClubRivalDao clubRivalDao, String name, String city, String web, byte[] icon) {
        ClubRival cr = new ClubRival();
        
        cr.setName(name);
        cr.setCity(city);
        cr.setWeb(web);
        cr.setIcon(icon);
        
        clubRivalDao.insertRow(cr);
    }
    
    public void insertClubRivals(ClubRivalDao clubRivalDao, int numOfClubRivals) {
        for (int i = 0; i < numOfClubRivals; i++) {
            ClubRival cr = new ClubRival();
            cr.setName(UUID.randomUUID().toString());
            clubRivalDao.insertRow(cr);
        }
    }

    public void deleteAll(ClubRivalDao clubRivalDao) {
        for (ClubRival cr : clubRivalDao.getAllClubRivals()) {
            clubRivalDao.deleteRow(cr.getId());
        }
    }
}
