package com.clubeek.dao.impl.springjdbctemplate;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite.
 *
 * @author vitfo
 */
@RunWith(Suite.class)
@SuiteClasses({ 
    ArticleDaoImplTest.class, 
    CategoryDaoImplTest.class, 
    ClubTeamDaoImplTest.class,
    ClubMemberDaoImplTest.class
})
public class AllTests {

}
