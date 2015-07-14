package com.clubeek.dao.impl.springjdbctemplate;

import static org.junit.Assert.*;

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
import com.clubeek.dao.ClubDao;
import com.clubeek.dao.ClubRivalDao;
import com.clubeek.dao.ClubTeamDao;
import com.clubeek.dao.TeamMatchDao;
import com.clubeek.enums.MatchType;
import com.clubeek.enums.SportType;
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
    
    @Autowired
    ClubDao clubDao;
    
    @Before
    public void deleteAllTeamMatches() {
        deleteAll(teamMatchDao);
    }
    
    /**
     * Tests getAllTeamMatchesForClubTeam and getAllPublishableTeamMatchesForClubTeam.
     */
    @Test
    public void testGetAllTeamMatches() {
        assertTrue(teamMatchDao.getAllTeamMatches().size() == 0);
        
        int clubTeamId = getValidClubTeamId(clubDao, clubTeamDao);
        
        insertTeamMatches(teamMatchDao, 4, false, clubTeamId);
        insertTeamMatches(teamMatchDao, 7, true, clubTeamId);
        
        assertTrue(teamMatchDao.getAllTeamMatches().size() == 11);
        assertTrue(teamMatchDao.getAllTeamMatchesForClubTeam(clubTeamId).size() == 11);
        assertTrue(teamMatchDao.getAllPublishableTeamMatchesForClubTeam(clubTeamId).size() == 7);
    }
    
    @Test
    public void testGetAllPublishableHomeTeamMatchesForClubTeam() {
        assertTrue(teamMatchDao.getAllTeamMatches().size() == 0);
        
        int clubTeamId = getValidClubTeamId(clubDao, clubTeamDao);
        
        // old match not home, not publishable
        insertTeamMatchSimple(teamMatchDao, false, MatchType.MATCH, new Date(1_000_000L), false, clubTeamId);
        
        // old match home, not publishable
        insertTeamMatchSimple(teamMatchDao, true, MatchType.MATCH, new Date(1_000_000L), false, clubTeamId);
        
        // old match not home, publishable
        insertTeamMatchSimple(teamMatchDao, false, MatchType.MATCH, new Date(1_000_000L), true, clubTeamId);
        
        // new match not home, not publishable
        insertTeamMatchSimple(teamMatchDao, false, MatchType.MATCH, new Date(), false, clubTeamId);
        
        // new match home, not publishable
        insertTeamMatchSimple(teamMatchDao, true, MatchType.MATCH, new Date(), false, clubTeamId);
        
        // new match not home, publishable
        insertTeamMatchSimple(teamMatchDao, false, MatchType.MATCH, new Date(), true, clubTeamId);
        
        assertTrue(teamMatchDao.getAllTeamMatches().size() == 6);
        assertTrue(teamMatchDao.getAllPublishableTeamMatchesForClubTeam(clubTeamId).size() == 2);
        assertTrue(teamMatchDao.getAllPublishableHomeTeamMatchesForClubTeam(clubTeamId, 1).size() == 1);
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
        insertTeamMatch(clubDao, teamMatchDao, clubTeamDao, clubRivalDao,
                true, 
                MatchType.CUP, 
                cal.getTime(), 
                "Good match",
                "Rival comment",
                true,
                1,
                3,
                "Score detail");
        assertTrue(teamMatchDao.getAllTeamMatches().size() == 1);
        TeamMatch tm = teamMatchDao.getAllTeamMatches().get(0);
        assertTrue(tm.isHomeMatch());
        assertTrue(tm.getMatchType().equals(MatchType.CUP));
        assertTrue(tm.getStart().getTime() == cal.getTimeInMillis());
        assertTrue(tm.getComment().equals("Good match"));
        assertTrue(tm.getClubRivalComment().equals("Rival comment"));
        assertTrue(tm.getPublish());
        assertTrue(tm.getScoreA() == 1);
        assertTrue(tm.getScoreB() == 3);
        assertTrue(tm.getScoreDetail().equals("Score detail"));
        
        // update
        Calendar cal2 = new GregorianCalendar();
        cal2.setTime(new Date(0L));
        cal2.set(2017, 2, 24, 17, 05);
        tm.setHomeMatch(false);
        tm.setMatchType(MatchType.CHAMPIONSHIP);
        tm.setStart(cal2.getTime());
        tm.setComment("My comment");
        tm.setClubRivalComment("My rival comment");
        tm.setPublish(false);
        tm.setScoreA(11);
        tm.setScoreB(7);
        tm.setScoreDetail("My score detail");
        teamMatchDao.updateRow(tm);
        TeamMatch tm2 = teamMatchDao.getAllTeamMatches().get(0);
        assertFalse(tm.isHomeMatch());
        assertTrue(tm.getMatchType().equals(MatchType.CHAMPIONSHIP));
        assertTrue(tm2.getStart().getTime() == cal2.getTimeInMillis());
        assertTrue(tm2.getComment().equals("My comment"));
        assertTrue(tm.getClubRivalComment().equals("My rival comment"));
        assertFalse(tm.getPublish());
        assertTrue(tm.getScoreA() == 11);
        assertTrue(tm.getScoreB() == 7);
        assertTrue(tm.getScoreDetail().equals("My score detail"));
        
        insertTeamMatchSimple(clubDao, teamMatchDao, clubTeamDao, true, MatchType.FRIENDLY_MATCH, new Date(), false);
        assertTrue(teamMatchDao.getAllTeamMatches().size() == 2);
    }
    
    @Test
    public void deleteRowsTest() {
        assertTrue(teamMatchDao.getAllTeamMatches().size() == 0);
        insertTeamMatches(clubDao, teamMatchDao, clubTeamDao, 10);
        assertTrue(teamMatchDao.getAllTeamMatches().size() == 10);
        teamMatchDao.deleteRows(teamMatchDao.getAllTeamMatches().subList(2, 7));
        assertTrue(teamMatchDao.getAllTeamMatches().size() == 5);
    }
    
    public void insertTeamMatchSimple(
            TeamMatchDao teamMatchDao,
            boolean isHomeMatch, 
            MatchType matchType, 
            Date start,
            boolean publish,
            int clubTeamId) {
        
        TeamMatch t = new TeamMatch();
        t.setHomeMatch(isHomeMatch);
        t.setMatchType(matchType);
        t.setStart(start);
        t.setPublish(publish);
        t.setClubTeamId(clubTeamId);
        teamMatchDao.insertRow(t);
    }

    public void insertTeamMatchSimple(
            ClubDao clubDao, 
            TeamMatchDao teamMatchDao,
            ClubTeamDao clubTeamDao, 
            boolean isHomeMatch, 
            MatchType matchType, 
            Date start,
            boolean publish) {
        ClubTeamDaoImplTest clubTeamTest = new ClubTeamDaoImplTest();
        clubTeamTest.insertClubTeam(clubTeamDao, categoryDao, clubDao, true, "My club team", SportType.BASEBALL, true, 9);
        int clubTeamId = clubTeamDao.getAllClubTeams().get(0).getId();
        
        TeamMatch t = new TeamMatch();
        t.setHomeMatch(isHomeMatch);
        t.setMatchType(matchType);
        t.setStart(start);
        t.setPublish(publish);
        t.setClubTeamId(clubTeamId);
        teamMatchDao.insertRow(t);
    }
    
    public void insertTeamMatch(
            ClubDao clubDao,
            TeamMatchDao teamMatchDao, 
            ClubTeamDao clubTeamDao, 
            ClubRivalDao clubRivalDao, 
            boolean isHomeMatch,
            MatchType matchType,
            Date start, 
            String comment,
            String clubRivalComment,
            boolean publish,
            int scoreA,
            int scoreB,
            String scoreDetail) {
        ClubTeamDaoImplTest clubTeamTest = new ClubTeamDaoImplTest();
        clubTeamTest.insertClubTeam(clubTeamDao, clubDao, "My club team", SportType.FOOTBALL);
        int clubTeamId = clubTeamDao.getAllClubTeams().get(0).getId();
        
        ClubRivalDaoImplTest clubRivalTest = new ClubRivalDaoImplTest();
        clubRivalTest.insertClubRivals(clubRivalDao, 1);
        int clubRivalId = clubRivalDao.getAllClubRivals().get(0).getId();
        
        TeamMatch t = new TeamMatch();
        t.setHomeMatch(isHomeMatch);
        t.setMatchType(matchType);
        t.setStart(start);
        t.setComment(comment);
        t.setClubRivalComment(clubRivalComment);
        t.setPublish(publish);
        t.setScoreA(scoreA);
        t.setScoreB(scoreB);
        t.setScoreDetail(scoreDetail);
        t.setClubTeamId(clubTeamId);
        t.setClubRivalId(clubRivalId);
        teamMatchDao.insertRow(t);
    }
    
    public void insertTeamMatches(ClubDao clubDao, TeamMatchDao teamMatchDao, ClubTeamDao clubTeamDao, int numOfTeamMatches) {
        for (int i = 0; i < numOfTeamMatches; i++) {
            insertTeamMatchSimple(clubDao, teamMatchDao, clubTeamDao, true, MatchType.MATCH, new Date(), false);
        }
    }
    
    public void insertTeamMatches(TeamMatchDao teamMatchDao, int numOfTeamMatches, boolean publish, int clubTeamId) {
        for (int i = 0; i < numOfTeamMatches; i++) {
            insertTeamMatchSimple(teamMatchDao, false, MatchType.FRIENDLY_MATCH, new Date(), publish, clubTeamId);
        }
    }
    
    public int getValidClubTeamId(ClubDao clubDao, ClubTeamDao clubTeamDao) {
        ClubTeamDaoImplTest clubTeamTest = new ClubTeamDaoImplTest();
        return clubTeamTest.getValidClubTeamId(clubTeamDao, clubDao);
    }

    public void deleteAll(TeamMatchDao teamMatchDao) {
        for (TeamMatch tm : teamMatchDao.getAllTeamMatches()) {
            teamMatchDao.deleteRow(tm.getId());
        }
    }
}
