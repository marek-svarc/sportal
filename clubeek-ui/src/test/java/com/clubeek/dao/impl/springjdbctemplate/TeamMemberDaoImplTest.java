package com.clubeek.dao.impl.springjdbctemplate;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clubeek.dao.CategoryDao;
import com.clubeek.dao.ClubMemberDao;
import com.clubeek.dao.ClubTeamDao;
import com.clubeek.dao.TeamMemberDao;
import com.clubeek.model.TeamMember;

/**
* Class that tests TeamMemberDaoImpl.
* 
* @author vitfo
*
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("test-config.xml")
public class TeamMemberDaoImplTest {
    
    @Autowired
    TeamMemberDao teamMemberDao;
    @Autowired
    ClubMemberDao clubMemberDao;
    @Autowired
    ClubTeamDao clubTeamDao;
    @Autowired
    CategoryDao categoryDao;
    
    private ClubMemberDaoImplTest clubMemberTest = new ClubMemberDaoImplTest();
    private ClubTeamDaoImplTest clubTeamTest = new ClubTeamDaoImplTest();
    
    @Before
    public void deleteAllTeamMembers() {
        clubMemberTest.deleteAll(clubMemberDao);
        clubTeamTest.deleteAll(clubTeamDao);
    }
    
    /**
     * Tests insert, update, delete, getById.
     */
    @Test
    public void test() {
        insertTeamMember(clubMemberDao, clubTeamDao, categoryDao);
        int idClubTeam = clubTeamDao.getAllClubTeams().get(0).getId();
        assertTrue(teamMemberDao.getTeamMembersByTeamId(idClubTeam).size() == 1);
        
        TeamMember tm = teamMemberDao.getTeamMembersByTeamId(idClubTeam).get(0);
        int idTeamMember = tm.getId();
        teamMemberDao.deleteRow(idTeamMember);
        assertTrue(teamMemberDao.getTeamMembersByTeamId(idClubTeam).size() == 0);
    }

    @Test
    public void testUpdateRow() {
        // TODO vitfo, created on 23. 6. 2015 
    }

    @Test
    public void testUpdate() {
        // TODO vitfo, created on 23. 6. 2015 
    }

    public void insertTeamMember(ClubMemberDao clubMemberDao, ClubTeamDao clubTeamDao, CategoryDao categoryDao) {
        clubMemberTest.insertClubMembers(clubMemberDao, 1);
        int idClubMember = clubMemberDao.getAllClubMembers().get(0).getId();
        
        clubTeamTest.insertClubTeam(clubTeamDao, categoryDao, true, "My club team", true, 10);
        int idClubTeam = clubTeamDao.getAllClubTeams().get(0).getId();
        
        TeamMember tm = new TeamMember();
        tm.setClubMemberId(idClubMember);
        tm.setClubTeamId(idClubTeam);
        teamMemberDao.insertRow(tm);
    }
}
