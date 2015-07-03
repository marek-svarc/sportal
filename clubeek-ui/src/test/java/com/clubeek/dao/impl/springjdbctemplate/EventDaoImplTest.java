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

import com.clubeek.dao.EventDao;
import com.clubeek.model.Event;

/**
 * Class that tests EvenDaoImpl.
 * 
 * @author vitfo
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-config.xml")
public class EventDaoImplTest {
    
    @Autowired
    EventDao eventDao;
    
    @Before
    public void deleteAllEvents() {
        deleteAll(eventDao);
    }

    /**
     * Tests insert, update, delete, getAll.
     */
    @Test
    public void test() {
        assertTrue(eventDao.getAllEvents().size() == 0);
        
        // insert
        Calendar start = new GregorianCalendar();
        start.setTime(new Date(0L));
        start.set(2015,  5, 19, 15, 45);
        Calendar finish = new GregorianCalendar();
        finish.setTime(new Date(0L));
        finish.set(2015,  6, 20, 10, 30);
        insertEvent(
                eventDao, 
                start.getTime(), 
                finish.getTime(), 
                "My caption", 
                "My place", 
                "My description", 
                true, 
                null, 
                null);
        assertTrue(eventDao.getAllEvents().size() == 1);
        
        Event a = eventDao.getAllEvents().get(0);
        assertTrue(a.getStart().getTime() == start.getTimeInMillis());
        assertTrue(a.getFinish().getTime() == finish.getTimeInMillis());
        assertTrue(a.getCaption().equals("My caption"));
        assertTrue(a.getPlace().equals("My place"));
        assertTrue(a.getDescription().equals("My description"));
        assertTrue(a.getSignParticipation());
        assertNull(a.getClubTeamId());
        assertNull(a.getCategoryId());
        
        // update
        Calendar start2 = new GregorianCalendar();
        start2.setTime(new Date(0L));
        start2.set(2016,  7, 1, 20, 00);
        Calendar finish2 = new GregorianCalendar();
        finish2.setTime(new Date(0L));
        finish2.set(2016,  9, 2, 12, 15);
        a.setStart(start2.getTime());
        a.setFinish(finish2.getTime());
        a.setCaption("caption");
        a.setPlace("place");
        a.setDescription("description");
        a.setSignParticipation(false);
        eventDao.updateRow(a);
        
        Event a2 = eventDao.getAllEvents().get(0);
        assertTrue(a2.getStart().getTime() == start2.getTimeInMillis());
        assertTrue(a2.getFinish().getTime() == finish2.getTimeInMillis());
        assertTrue(a2.getCaption().equals("caption"));
        assertTrue(a2.getPlace().equals("place"));
        assertTrue(a2.getDescription().equals("description"));
        assertFalse(a2.getSignParticipation());
        
        insertEvent(
                eventDao, 
                new Date(), 
                null, 
                "My caption", 
                null, 
                null, 
                false, 
                null, 
                null);
        assertTrue(eventDao.getAllEvents().size() == 2);
        
        // delete
        deleteAll(eventDao);
        assertTrue(eventDao.getAllEvents().size() == 0);
    }
    
    @Test
    public void testDeleteRows() {
        assertTrue(eventDao.getAllEvents().size() == 0);
        insertEvents(eventDao, 10);
        assertTrue(eventDao.getAllEvents().size() == 10);
        eventDao.deleteRows(eventDao.getAllEvents().subList(3, 8));
        assertTrue(eventDao.getAllEvents().size() == 5);
    }

    public void deleteAll(EventDao eventDao) {
        for (Event a : eventDao.getAllEvents()) {
            eventDao.deleteRow(a.getId());
        }
    }
    
    public void insertEvent(EventDao eventDao, Date start, Date finish, String caption, String place, String description, Boolean signParticipation, Integer clubTeamId, Integer categoryId) {
        Event a = new Event();
        a.setStart(start);
        a.setStart(start);
        a.setFinish(finish);
        a.setCaption(caption);
        a.setPlace(place);
        a.setDescription(description);
        a.setSignParticipation(signParticipation);
        a.setClubTeamId(clubTeamId);
        a.setCategoryId(categoryId);
        eventDao.insertRow(a);
    }
    
    public void insertEvents(EventDao eventDao, int numOfEvents) {
        for (int i = 0; i < numOfEvents; i++) {
            insertEvent(
                    eventDao, 
                    new Date(), 
                    null, 
                    "", 
                    null, 
                    null, 
                    false, 
                    null, 
                    null);
        }
    }
}
