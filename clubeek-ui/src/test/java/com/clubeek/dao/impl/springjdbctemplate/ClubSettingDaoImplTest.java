package com.clubeek.dao.impl.springjdbctemplate;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clubeek.dao.ClubSettingDao;

/**
* Class that tests ClubSettingDaoImpl.
* 
* @author vitfo
*
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("test-config.xml")
public class ClubSettingDaoImplTest {
    
    @Autowired
    ClubSettingDao clubSettingDao;

    @Test
    public void testGetClubSettingById() {
        // TODO vitfo, created on 23. 6. 2015 - need to implement other methods (insert, delete, ...)
    }
}
