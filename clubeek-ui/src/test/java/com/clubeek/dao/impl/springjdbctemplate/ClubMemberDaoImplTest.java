package com.clubeek.dao.impl.springjdbctemplate;

import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clubeek.dao.CategoryDao;
import com.clubeek.dao.ClubDao;
import com.clubeek.dao.ClubMemberDao;
import com.clubeek.dao.ClubTeamDao;
import com.clubeek.dao.TeamTrainingDao;
import com.clubeek.enums.LicenceType;
import com.clubeek.model.ClubMember;

/**
 * Class that tests ClubMemberDaoImpl.
 *
 * @author vitfo
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-config.xml")
public class ClubMemberDaoImplTest {
    
    @Autowired
    ClubMemberDao clubMemberDao;
    @Autowired
    ClubTeamDao clubTeamDao;
    @Autowired
    TeamTrainingDao teamTrainingDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    ClubDao clubDao;
    
    @Before
    public void deleteAllClubMembers() {
        deleteAll(clubMemberDao);
    }
    
    /**
     * Test insert, update, delete, select.
     */
    @Test
    public void test() {
        assertTrue(clubMemberDao.getAllClubMembers().size() == 0);
        
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date(0L));
        cal.set(1970, 1, 1, 0, 0);
        insertClubMember(clubMemberDao, clubDao, "František", "Koudelka", cal.getTime());
        assertTrue(clubMemberDao.getAllClubMembers().size() == 1);
        
        int clubMemberId = clubMemberDao.getAllClubMembers().get(0).getId();
        insertClubMembers(clubMemberDao, clubDao, 5);
        assertTrue(clubMemberDao.getAllClubMembers().size() == 6);
        
        ClubMember cm = clubMemberDao.getClubMember(clubMemberId);
        assertTrue(cm.getName().equals("František"));
        assertTrue(cm.getSurname().equals("Koudelka"));
        assertTrue(cm.getBirthdate().getTime() == cal.getTimeInMillis());
        
        // update
        cm.setName("Jan");
        clubMemberDao.updateRow(cm);
        ClubMember clm = clubMemberDao.getClubMember(clubMemberId);
        assertTrue(clm.getName().equals("Jan"));
    }


    @Test
    public void testGetClubMembersByTeamId() {
        // TODO vitfo, created on 23. 6. 2015 - it needs t_team_member implementation
    }

    @Test
    public void testGetClubMembersByDateOfBirth() {
        Calendar cal01 = new GregorianCalendar();
        cal01.setTime(new Date(0L));
        cal01.set(1980, 2, 25, 0, 0);
        insertClubMember(clubMemberDao, clubDao, "Jiří", "Novák", cal01.getTime());
        
        Calendar cal02 = new GregorianCalendar();
        cal02.setTime(new Date(0L));
        cal02.set(1985, 5, 27, 0, 0);
        insertClubMember(clubMemberDao, clubDao, "František", "Koudelka", cal02.getTime());
        
        Calendar cal03 = new GregorianCalendar();
        cal03.setTime(new Date(0L));
        cal03.set(1990, 11, 29, 0, 0);
        insertClubMember(clubMemberDao, clubDao, "Petra", "Zelená", cal03.getTime());
        
        assertTrue(clubMemberDao.getAllClubMembers().size() == 3);
        assertTrue(clubMemberDao.getClubMembersByDateOfBirth(2000, 2010).size() == 0);
        assertTrue(clubMemberDao.getClubMembersByDateOfBirth(1990, 2010).size() == 1);
        assertTrue(clubMemberDao.getClubMembersByDateOfBirth(1984, 2010).size() == 2);
        assertTrue(clubMemberDao.getClubMembersByDateOfBirth(1970, 2010).size() == 3);
        assertTrue(clubMemberDao.getClubMembersByDateOfBirth(1984, 1987).size() == 1);
        assertTrue(clubMemberDao.getClubMembersByDateOfBirth(1984, 1990).size() == 2);
    }

    @Test
    public void testGetAllClubMembers() {
        assertTrue(clubMemberDao.getAllClubMembers().size() == 0);
        
        insertClubMembers(clubMemberDao, clubDao, 7);
        assertTrue(clubMemberDao.getAllClubMembers().size() == 7);
    }
    
    /**
     * Tests getClubMembersByTeamTrainingId and addClubMemberToTeamTraining.
     */
    @Test
    public void getClubMembersByTeamTrainingIdTest() {
        insertClubMembers(clubMemberDao, clubDao, 5);
        assertTrue(clubMemberDao.getAllClubMembers().size() == 5);
        
        TeamTrainingDaoImplTest teamTrainingTest = new TeamTrainingDaoImplTest();
        teamTrainingTest.insertTeamTraining(teamTrainingDao, clubTeamDao, categoryDao, clubDao, new Date(), new Date(), "Hřiště", "Comment");
        teamTrainingTest.insertTeamTraining(teamTrainingDao, clubTeamDao, categoryDao, clubDao, new Date(), new Date(), "Tělocvična", "Other comment");
        int teamTrainingId1 = teamTrainingDao.getAllTeamTrainings().get(0).getId();
        int teamTrainingId2 = teamTrainingDao.getAllTeamTrainings().get(1).getId();
        List<ClubMember> clubMembers = clubMemberDao.getAllClubMembers();
        for (int i = 0; i < clubMembers.size(); i++) {
            if (i < 3) {
                clubMemberDao.addClubMemberToTeamTraining(clubMembers.get(i).getId(), teamTrainingId1);
            } else {
                clubMemberDao.addClubMemberToTeamTraining(clubMembers.get(i).getId(), teamTrainingId2);
            }
        }
        
        assertTrue(clubMemberDao.getClubMembersByTeamTrainingId(teamTrainingId1).size() == 3);
        assertTrue(clubMemberDao.getClubMembersByTeamTrainingId(teamTrainingId2).size() == 2);
    }
    
    @Test
    public void deleteRowsTest() {
        assertTrue(clubMemberDao.getAllClubMembers().size() == 0);
        
        insertClubMembers(clubMemberDao, clubDao, 10);
        assertTrue(clubMemberDao.getAllClubMembers().size() == 10);
        List<ClubMember> clubMembersToDelete = clubMemberDao.getAllClubMembers().subList(4, 9);
        clubMemberDao.deleteRows(clubMembersToDelete);
        assertTrue(clubMemberDao.getAllClubMembers().size() == 5);
    }
    
    public void insertClubMember(ClubMemberDao clubMemberDao, ClubDao clubDao, String name, String surname, Date birthdate) {
        if (clubDao.getAllClubs().size() == 0) {
            ClubDaoImplTest clubTest = new ClubDaoImplTest();
            clubTest.insertClub(clubDao, LicenceType.PROFESSIONAL, "My title", "My comment", null);
        }
        int clubId = clubDao.getAllClubs().get(0).getId();
        
        ClubMember cm = new ClubMember();
        cm.setClubId(clubId);
        cm.setName(name);
        cm.setSurname(surname);
        cm.setBirthdate(birthdate);
        clubMemberDao.insertRow(cm);
    }
    
    public void insertClubMembers(ClubMemberDao clubMemberDao, ClubDao clubDao, int numOfClubMembers) {
        if (clubDao.getAllClubs().size() == 0) {
            ClubDaoImplTest clubTest = new ClubDaoImplTest();
            clubTest.insertClub(clubDao, LicenceType.PROFESSIONAL, "My title", "My comment", null);
        }
        int clubId = clubDao.getAllClubs().get(0).getId();
        
        for (int i = 0; i < numOfClubMembers; i++) {
            ClubMember cm = new ClubMember();
            cm.setClubId(clubId);
            cm.setName("John");
            cm.setSurname("Smith");
            clubMemberDao.insertRow(cm);
        }
    }

    public void deleteAll(ClubMemberDao clubMemberDao) {
        for (ClubMember cm : clubMemberDao.getAllClubMembers()) {
            clubMemberDao.deleteRow(cm.getId());
        }
    }
}
