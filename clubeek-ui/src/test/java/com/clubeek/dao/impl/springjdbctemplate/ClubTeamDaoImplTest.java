package com.clubeek.dao.impl.springjdbctemplate;

import static org.junit.Assert.*;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clubeek.dao.CategoryDao;
import com.clubeek.dao.ClubTeamDao;
import com.clubeek.model.Category;
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
        
        insertClubTeam(clubTeamDao, categoryDao, true, "My club team", true, 10);
        assertTrue(clubTeamDao.getAllClubTeams().size() == 1);
        
        insertClubTeam(clubTeamDao, categoryDao, "My club team", true, 10);
        insertClubTeam(clubTeamDao, categoryDao, "My club team", false, 10);
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
        
        insertClubTeams(clubTeamDao, 10);
        assertTrue(clubTeamDao.getAllClubTeams().size() == 10);
        List<ClubTeam> clubTeamsToDelete = clubTeamDao.getAllClubTeams().subList(4, 9);
        clubTeamDao.deleteRows(clubTeamsToDelete);
        assertTrue(clubTeamDao.getAllClubTeams().size() == 5);
    }

    @Test
    public void testGetActiveAndAllClubTeams() {
        insertClubTeam(clubTeamDao, categoryDao, true, "Club team 1", true, 7);
        insertClubTeam(clubTeamDao, categoryDao, true, "Club team 2", true, 7);
        insertClubTeam(clubTeamDao, categoryDao, true, "Club team 3", false, 7);
        insertClubTeam(clubTeamDao, categoryDao, true, "Club team 4", false, 9);
        insertClubTeam(clubTeamDao, categoryDao, true, "Club team 5", true, 11);
        
        assertTrue(clubTeamDao.getActiveClubTeams().size() == 3);
        assertTrue(clubTeamDao.getAllClubTeams().size() == 5);
    }

    public void insertClubTeam(ClubTeamDao clubTeamDao, CategoryDao categoryDao, String name, boolean active, int sorting) {
        ClubTeam ct = new ClubTeam();
        ct.setActive(active);
        ct.setName(name);
        ct.setSorting(sorting);
        
        int categoryId = categoryDao.getAllCategories().get(0).getId();
        ct.setCategoryId(categoryId);
        clubTeamDao.insertRow(ct);
    }
    
    public void insertClubTeam(ClubTeamDao clubTeamDao, CategoryDao categoryDao, boolean insertCategory, String name, boolean active, int sorting) {
        ClubTeam ct = new ClubTeam();
        ct.setActive(active);
        ct.setName(name);
        ct.setSorting(sorting);
        
        if (insertCategory) {
            CategoryDaoImplTest categoryTest = new CategoryDaoImplTest();
            categoryTest.insertCategory(categoryDao, "My category", true);
        }
        int categoryId = categoryDao.getAllCategories().get(0).getId();
        ct.setCategoryId(categoryId);
        clubTeamDao.insertRow(ct);
    }
    
    public void insertClubTeam(ClubTeamDao clubTeamDao, String name) {
        ClubTeam ct = new ClubTeam();
        ct.setName(name);
        clubTeamDao.insertRow(ct);
    }
    
    public void insertClubTeams(ClubTeamDao clubTeamDao, int numOfClubTeams) {
        for (int i = 0; i < numOfClubTeams; i++) {
            insertClubTeam(
                    clubTeamDao, 
                    UUID.randomUUID().toString());
        }
    }
    
    public void deleteAll(ClubTeamDao clubTeamDao) {
        for (ClubTeam ct : clubTeamDao.getAllClubTeams()) {
            clubTeamDao.deleteRow(ct.getId());
        }
    }
}
