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
import com.clubeek.dao.ClubTeamDao;
import com.clubeek.dao.TeamTrainingDao;
import com.clubeek.model.TeamTraining;

/**
 * Class that tests TeamTrainingDaoImpl.
 * 
 * @author vitfo
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-config.xml")
public class TeamTrainingDaoImplTest {
    
    @Autowired
    TeamTrainingDao teamTrainingDao;
    @Autowired
    ClubTeamDao clubTeamDao;
    @Autowired
    CategoryDao categoryDao;
    
    @Before
    public void deleteAllTeamTrainings() {
        deleteAll(teamTrainingDao);
    }
    
    /**
     * Tests delete, update, insert, getAll.
     */
    @Test
    public void test() {
        assertTrue(teamTrainingDao.getAllTeamTrainings().size() == 0);
        
        Calendar startTime = new GregorianCalendar();
        startTime.setTime(new Date(0L));
        startTime.set(2015, 9, 11, 15, 30);
        Calendar endTime = new GregorianCalendar();
        endTime.setTime(new Date(0L));
        endTime.set(2015, 9, 11, 17, 00);
        
        // insert
        insertTeamTraining(teamTrainingDao, clubTeamDao, categoryDao, startTime.getTime(), endTime.getTime(), "Tělocvična", "Vede František Koudelka");
        assertTrue(teamTrainingDao.getAllTeamTrainings().size() == 1);
        
        // get all
        TeamTraining tt = teamTrainingDao.getAllTeamTrainings().get(0);
        assertTrue(tt.getStart().getTime() == startTime.getTimeInMillis());
        assertTrue(tt.getEnd().getTime() == endTime.getTimeInMillis());
        assertTrue(tt.getPlace().equals("Tělocvična"));
        assertTrue(tt.getComment().equals("Vede František Koudelka"));
        
        // delete
        teamTrainingDao.deleteRow(tt.getId());
        assertTrue(teamTrainingDao.getAllTeamTrainings().size() == 0);
        
        // update
        insertTeamTraining(teamTrainingDao, clubTeamDao, categoryDao, startTime.getTime(), endTime.getTime(), "Tělocvična", "Vede František Koudelka");
        TeamTraining t = teamTrainingDao.getAllTeamTrainings().get(0);
        
        Calendar startTime2 = new GregorianCalendar();
        startTime2.setTime(new Date(0L));
        startTime2.set(2016, 8, 1, 18, 45);
        Calendar endTime2 = new GregorianCalendar();
        endTime2.setTime(new Date(0L));
        endTime2.set(2016, 8, 1, 20, 15);
        t.setStart(startTime2.getTime());
        t.setEnd(endTime2.getTime());
        t.setPlace("Hřiště");
        t.setComment("Jó třešně zrály");
        
        teamTrainingDao.updateRow(t);
        TeamTraining ut = teamTrainingDao.getAllTeamTrainings().get(0);
        assertTrue(ut.getStart().getTime() == startTime2.getTimeInMillis());
        assertTrue(ut.getEnd().getTime() == endTime2.getTimeInMillis());
        assertTrue(ut.getPlace().equals("Hřiště"));
        assertTrue(ut.getComment().equals("Jó třešně zrály"));
        
    }
    
    public void insertTeamTraining(TeamTrainingDao teamTrainingDao, ClubTeamDao clubTeamDao, CategoryDao categoryDao, Date start, Date end, String place, String comment) {
        ClubTeamDaoImplTest clubTeamTest = new ClubTeamDaoImplTest();
        clubTeamTest.insertClubTeam(clubTeamDao, categoryDao, true, "My test club", true, 0);
        
        TeamTraining tt = new TeamTraining();
        tt.setStart(start);
        tt.setEnd(end);
        tt.setPlace(place);
        tt.setComment(comment);
        tt.setClubTeamId(clubTeamDao.getActiveClubTeams().get(0).getId());
        teamTrainingDao.insertRow(tt);
    }

    public void deleteAll(TeamTrainingDao teamTrainingDao) {
        for (TeamTraining tt : teamTrainingDao.getAllTeamTrainings()) {
            teamTrainingDao.deleteRow(tt.getId());
        }
    }
}
