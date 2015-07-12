package com.clubeek.dao.impl.springjdbctemplate;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
    EventDaoImplTest.class, 
    ArticleDaoImplTest.class, 
    CategoryDaoImplTest.class, 
    ClubMemberDaoImplTest.class, 
    ClubRivalDaoImplTest.class,
    ClubDaoImplTest.class, 
    ClubTeamDaoImplTest.class, 
    ContactDaoImplTest.class, 
    TeamMatchDaoImplTest.class, 
    TeamMemberDaoImplTest.class,
    TeamTrainingDaoImplTest.class, 
    UserDaoImplTest.class,
    ClubUrlDaoImplTest.class})
public class AllTests {

}
