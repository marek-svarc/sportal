package com.clubeek.dao.impl.springjdbctemplate;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clubeek.dao.CategoryDao;
import com.clubeek.dao.ClubDao;
import com.clubeek.dao.ClubTeamDao;
import com.clubeek.enums.LicenceType;
import com.clubeek.enums.SportType;
import com.clubeek.model.ClubTeam;

/**
 * Class that tests ClubTeamDaoImpl.
 *
 * @author vitfo
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-config.xml")
public class ClubTeamDaoImplTest {
    
    @Autowired
    ClubTeamDao clubTeamDao;
    
    @Autowired
    CategoryDao categoryDao;
    
    @Autowired
    ClubDao clubDao;
    
    @Before
    public void deleteAllClubTeams() {
        deleteAll(clubTeamDao);
    }
    
    /**
     * Tests insert, update, delete, select.
     */
    @Test
    public void test() {
        assertTrue(clubTeamDao.getAllClubTeams().size() == 0);
        
        insertClubTeam(clubTeamDao, categoryDao, clubDao, true, "My club team", SportType.FLOORBALL, true, 10);
        assertTrue(clubTeamDao.getAllClubTeams().size() == 1);
        ClubTeam ct = clubTeamDao.getAllClubTeams().get(0);
        assertTrue(ct.getActive());
        assertTrue(ct.getName().equals("My club team"));
        assertTrue(ct.getSportType().equals(SportType.FLOORBALL));
        
        insertClubTeam(clubTeamDao, categoryDao, clubDao, "My club team", SportType.BEACH_VOLLEYBALL, true, 10);
        insertClubTeam(clubTeamDao, categoryDao, clubDao, "My club team", SportType.BEACH_VOLLEYBALL, false, 10);
        assertTrue(clubTeamDao.getAllClubTeams().size() == 3);
        assertTrue(clubTeamDao.getActiveClubTeams().size() == 2);
        
        // Get id of activer club team.
        int clubTeamId = clubTeamDao.getActiveClubTeams().get(0).getId();
        // Get active club team.
        ClubTeam clubTeam = clubTeamDao.getClubTeamById(clubTeamId);
        // Update active club team to not active.
        clubTeam.setActive(false);
        clubTeamDao.updateRow(clubTeam);
        // Check the assertion.
        assertTrue(clubTeamDao.getAllClubTeams().size() == 3);
        assertTrue(clubTeamDao.getActiveClubTeams().size() == 1);
    }

    @Test
    public void deleteRowsTest() {
        assertTrue(clubTeamDao.getAllClubTeams().size() == 0);
        
        insertClubTeams(clubTeamDao, clubDao, 10);
        assertTrue(clubTeamDao.getAllClubTeams().size() == 10);
        List<ClubTeam> clubTeamsToDelete = clubTeamDao.getAllClubTeams().subList(4, 9);
        clubTeamDao.deleteRows(clubTeamsToDelete);
        assertTrue(clubTeamDao.getAllClubTeams().size() == 5);
    }

    @Test
    public void testGetActiveAndAllClubTeams() {
        insertClubTeam(clubTeamDao, categoryDao, clubDao, true, "Club team 1", SportType.BASKETBALL, true, 7);
        insertClubTeam(clubTeamDao, categoryDao, clubDao, true, "Club team 2", SportType.BASKETBALL, true, 7);
        insertClubTeam(clubTeamDao, categoryDao, clubDao, true, "Club team 3", SportType.BASKETBALL, false, 7);
        insertClubTeam(clubTeamDao, categoryDao, clubDao, true, "Club team 4", SportType.BASKETBALL, false, 9);
        insertClubTeam(clubTeamDao, categoryDao, clubDao, true, "Club team 5", SportType.BASKETBALL, true, 11);
        
        assertTrue(clubTeamDao.getActiveClubTeams().size() == 3);
        assertTrue(clubTeamDao.getAllClubTeams().size() == 5);
    }

    public void insertClubTeam(ClubTeamDao clubTeamDao, CategoryDao categoryDao, ClubDao clubDao, String name, SportType sportType, boolean active, int sorting) {
        ClubDaoImplTest clubTest = new ClubDaoImplTest();
        clubTest.insertClub(clubDao, LicenceType.FREE, "Title", "Comment", null);
        int clubId = clubDao.getAllClubs().get(0).getId();
        
        ClubTeam ct = new ClubTeam();
        ct.setClubId(clubId);
        ct.setActive(active);
        ct.setName(name);
        ct.setSportType(sportType);
        ct.setSorting(sorting);
        
        int categoryId = categoryDao.getAllCategories().get(0).getId();
        ct.setCategoryId(categoryId);
        clubTeamDao.insertRow(ct);
    }
    
    public void insertClubTeam(ClubTeamDao clubTeamDao, CategoryDao categoryDao, ClubDao clubDao, boolean insertCategory, String name, SportType sportType, boolean active, int sorting) {
        if (clubDao.getAllClubs().size() == 0) {
            ClubDaoImplTest clubTest = new ClubDaoImplTest();
            clubTest.insertClub(clubDao, LicenceType.FREE, "Title", "Comment", null);
        }
        int clubId = clubDao.getAllClubs().get(0).getId();
        
        ClubTeam ct = new ClubTeam();
        ct.setClubId(clubId);
        ct.setActive(active);
        ct.setName(name);
        ct.setSportType(sportType);
        ct.setSorting(sorting);
        
        if (insertCategory) {
            CategoryDaoImplTest categoryTest = new CategoryDaoImplTest();
            categoryTest.insertCategory(categoryDao, "My category", true);
        }
        int categoryId = categoryDao.getAllCategories().get(0).getId();
        ct.setCategoryId(categoryId);
        clubTeamDao.insertRow(ct);
    }
    
    public void insertClubTeam(ClubTeamDao clubTeamDao, ClubDao clubDao, String name, SportType sportType) {
        if (clubDao.getAllClubs().size() == 0) {
            ClubDaoImplTest clubTest = new ClubDaoImplTest();
            clubTest.insertClub(clubDao, LicenceType.FREE, "Title", "Comment", null);
        }
        int clubId = clubDao.getAllClubs().get(0).getId();
        
        ClubTeam ct = new ClubTeam();
        ct.setName(name);
        ct.setSportType(sportType);
        ct.setClubId(clubId);
        clubTeamDao.insertRow(ct);
    }
    
    public void insertClubTeams(ClubTeamDao clubTeamDao, ClubDao clubDao, int numOfClubTeams) {
        for (int i = 0; i < numOfClubTeams; i++) {
            insertClubTeam(
                    clubTeamDao, 
                    clubDao,
                    UUID.randomUUID().toString(),
                    SportType.VOLLEYBALL);
        }
    }
    
    public void deleteAll(ClubTeamDao clubTeamDao) {
        for (ClubTeam ct : clubTeamDao.getAllClubTeams()) {
            clubTeamDao.deleteRow(ct.getId());
        }
    }
}
