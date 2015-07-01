package com.clubeek.dao.impl.springjdbctemplate;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
    ActionDaoImplTest.class, 
    ArticleDaoImplTest.class, 
    CategoryDaoImplTest.class, 
    ClubMemberDaoImplTest.class, 
    ClubRivalDaoImplTest.class,
    ClubSettingDaoImplTest.class, 
    ClubTeamDaoImplTest.class, 
    ContactDaoImplTest.class, 
    TeamMatchDaoImplTest.class, 
    TeamMemberDaoImplTest.class,
    TeamTrainingDaoImplTest.class, 
    UserDaoImplTest.class })
public class AllTests {

}
