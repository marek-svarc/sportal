package com.clubeek.dao.impl.springjdbctemplate;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clubeek.dao.ClubTeamDao;
import com.clubeek.model.ClubTeam;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("test-config.xml")
public class ClubTeamDaoImplTest {
    
    @Autowired
    ClubTeamDao clubTeamDao;
    
    @Test
    public void test() {
//        insertClubTeam(false);
        
    }

//    @Test
//    public void testUpdateRow() {
//        fail("Not yet implemented");
//    }
//
//    @Test
//    public void testInsertRow() {
//        fail("Not yet implemented");
//    }
//
//    @Test
//    public void testDeleteRow() {
//        fail("Not yet implemented");
//    }
//
//    @Test
//    public void testExchangeRows() {
//        fail("Not yet implemented");
//    }
//
//    @Test
//    public void testGetClubTeamById() {
//        fail("Not yet implemented");
//    }
//
//    @Test
//    public void testGetActiveClubTeams() {
//        fail("Not yet implemented");
//    }
//
//    @Test
//    public void testGetAllClubTeams() {
//        fail("Not yet implemented");
//    }

    private void insertClubTeam(boolean active) {
        ClubTeam ct = new ClubTeam();
        ct.setActive(active);
        ct.setName("My club");
        ct.setSorting(5);
        System.out.println("Working: " + ct.getCategoryId());
        clubTeamDao.insertRow(ct);
    }
    
    private void deleteAll() {
        for (ClubTeam ct : clubTeamDao.getAllClubTeams()) {
            clubTeamDao.deleteRow(ct.getId());
            System.out.println("Working: " + ct.getId());
        }
    }
}
