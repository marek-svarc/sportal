package com.clubeek.dao.impl.springjdbctemplate;

import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clubeek.dao.CategoryDao;
import com.clubeek.dao.ClubRivalDao;
import com.clubeek.dao.ClubTeamDao;
import com.clubeek.dao.TeamMatchDao;
import com.clubeek.model.TeamMatch;

/**
 * Class that tests TeamMatchDaoImpl.
 *
 * @author vitfo
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-config.xml")
public class TeamMatchDaoImplTest {
    
    @Autowired
    TeamMatchDao teamMatchDao;
    
    @Autowired
    ClubTeamDao clubTeamDao;
    
    @Autowired
    CategoryDao categoryDao;
    
    @Autowired
    ClubRivalDao clubRivalDao;
    
    @Before
    public void deleteAllTeamMatches() {
        deleteAll(teamMatchDao);
    }
    
    /**
     * Tests delete, insert, update, getAll.
     */
    @Test
    public void test() {
        assertTrue(teamMatchDao.getAllTeamMatches().size() == 0);
        
        // insert
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date(0L));
        cal.set(2016, 9, 11, 19, 45);
        insertTeamMatch(teamMatchDao, clubTeamDao, categoryDao, clubRivalDao, cal.getTime(), 5, 3, "Good match");
        assertTrue(teamMatchDao.getAllTeamMatches().size() == 1);
        TeamMatch tm = teamMatchDao.getAllTeamMatches().get(0);
        assertTrue(tm.getStart().getTime() == cal.getTimeInMillis());
        assertTrue(tm.getScorePos() == 5);
        assertTrue(tm.getScoreNeg() == 3);
        assertTrue(tm.getComment().equals("Good match"));
        
        // update
        Calendar cal2 = new GregorianCalendar();
        cal2.setTime(new Date(0L));
        cal2.set(2017, 2, 24, 17, 05);
        tm.setStart(cal2.getTime());
        tm.setScorePos(4);
        tm.setScoreNeg(7);
        tm.setComment("My comment");
        teamMatchDao.updateRow(tm);
        TeamMatch tm2 = teamMatchDao.getAllTeamMatches().get(0);
        assertTrue(tm2.getStart().getTime() == cal2.getTimeInMillis());
        assertTrue(tm2.getScorePos() == 4);
        assertTrue(tm2.getScoreNeg() == 7);
        assertTrue(tm2.getComment().equals("My comment"));
        
        insertTeamMatch(teamMatchDao, clubTeamDao, categoryDao, clubRivalDao, new Date(), 2, 2, "Abcd");
        assertTrue(teamMatchDao.getAllTeamMatches().size() == 2);
    }
    
    @Test
    public void deleteRowsTest() {
        assertTrue(teamMatchDao.getAllTeamMatches().size() == 0);
        insertTeamMatches(teamMatchDao, clubTeamDao, categoryDao, clubRivalDao, 10);
        assertTrue(teamMatchDao.getAllTeamMatches().size() == 10);
        teamMatchDao.deleteRows(teamMatchDao.getAllTeamMatches().subList(2, 7));
        assertTrue(teamMatchDao.getAllTeamMatches().size() == 5);
    }

    public void insertTeamMatch(TeamMatchDao teamMatchDao, ClubTeamDao clubTeamDao, CategoryDao categoryDao, ClubRivalDao clubRivalDao, Date start, int scorePos, int scoreNeg, String comment) {
        ClubTeamDaoImplTest clubTeamTest = new ClubTeamDaoImplTest();
        clubTeamTest.insertClubTeam(clubTeamDao, categoryDao, true, "My club team", true, 9);
        int clubTeamId = clubTeamDao.getAllClubTeams().get(0).getId();
        
        ClubRivalDaoImplTest clubRivalTest = new ClubRivalDaoImplTest();
        clubRivalTest.insertClubRivals(clubRivalDao, 1);
        int clubRivalId = clubRivalDao.getAllClubRivals().get(0).getId();
        
        TeamMatch t = new TeamMatch();
        t.setStart(start);
        t.setScorePos(scorePos);
        t.setScoreNeg(scoreNeg);
        t.setComment(comment);
        t.setClubTeamId(clubTeamId);
        t.setClubRivalId(clubRivalId);
        teamMatchDao.insertRow(t);
    }
    
    public void insertTeamMatches(TeamMatchDao teamMatchDao, ClubTeamDao clubTeamDao, CategoryDao categoryDao, ClubRivalDao clubRivalDao, int numOfTeamMatches) {
        for (int i = 0; i < numOfTeamMatches; i++) {
            insertTeamMatch(teamMatchDao, clubTeamDao, categoryDao, clubRivalDao, new Date(), 0, 0, "");
        }
    }

    public void deleteAll(TeamMatchDao teamMatchDao) {
        System.out.println("Working");
        System.out.println(teamMatchDao.getAllTeamMatches());
        for (TeamMatch tm : teamMatchDao.getAllTeamMatches()) {
            teamMatchDao.deleteRow(tm.getId());
        }
    }
}
