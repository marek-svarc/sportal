package com.clubeek.dao.impl.springjdbctemplate;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clubeek.dao.ArticleDao;
import com.clubeek.dao.CategoryDao;
import com.clubeek.dao.ClubTeamDao;
import com.clubeek.enums.LocationType;
import com.clubeek.enums.OwnerType;
import com.clubeek.model.Article;
import com.clubeek.model.ClubTeam;

/**
 * Class that tests {@link ArticleDaoImpl}.
 * 
 * @author vitfo
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
// This annotation points to the context used to setup the Spring container, which I then inject my repository from. Since I have not specified a name for my context Spring searches for a configuration file within the same directory as my test class that has the name PostRepositoryTest-context.xml.
@ContextConfiguration("test-config.xml")
public class ArticleDaoImplTest {
    @Autowired
    ArticleDao articleDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    ClubTeamDao clubTeamDao;
    
    /**
     * Deletes all articles before each test.
     */
    @Before
    public void beforeEach() {
        deleteAllArticles(articleDao);
    }
    
    /**
     * Delete, insert, update.
     */
    @Test
    public void test() {
        // insert
        insertArticle(articleDao, false, LocationType.BULLETIN_BOARD);
        
        // get all articles
        int numOfArticles = articleDao.getAllArticles().size();
        assertTrue(numOfArticles > 0);
        
        // update one article
        Article a = articleDao.getAllArticles().get(0);
        int id = a.getId();
        a.setCaption("My caption");
        a.setSummary("My summary");
        a.setContent("My content");
        a.setOwnerType(OwnerType.TEAM);
        articleDao.updateRow(a);
        
        Article idArt = articleDao.getArticleById(id);
        assertTrue("Content shoud be 'My content'", idArt.getContent().equals("My content"));
        assertTrue("Summary shoud be 'My summary'", idArt.getSummary().equals("My summary"));
        assertTrue("Caption shoud be 'My caption'", idArt.getCaption().equals("My caption"));
        assertTrue("Owner type shoud be TEAM", idArt.getOwnerType().equals(OwnerType.TEAM));
        
        // delete
        articleDao.deleteRow(id);
        assertTrue("The article should be deleted", articleDao.getAllArticles().size() == 0);
    }
    

    @Test
    public void testGetAllArticles() {
        // insert several articles
        insertArticle(articleDao, false, LocationType.NEWS);
        insertArticle(articleDao, true, LocationType.BULLETIN_BOARD);
        insertArticle(articleDao, false, LocationType.BULLETIN_BOARD);
        
        assertTrue(articleDao.getAllArticles().size() == 3);
    }

    @Test
    public void testSelectArticles() {
        insertArticle(articleDao, false, LocationType.NEWS);
        insertArticle(articleDao, true, LocationType.BULLETIN_BOARD);
        insertArticle(articleDao, false, LocationType.BULLETIN_BOARD);
        insertArticle(articleDao, true, LocationType.NEWS);
        insertArticle(articleDao, false, LocationType.BULLETIN_BOARD);
        
        CategoryDaoImplTest categoryTest = new CategoryDaoImplTest();
        categoryTest.deleteAll(categoryDao);
        categoryTest.insertCategory(categoryDao, "My category", true);
        categoryTest.insertCategory(categoryDao, "My other category", true);
        int categoryId = categoryDao.getAllCategories().get(0).getId();
        int categoryIdOther = categoryDao.getAllCategories().get(1).getId();
        
        ClubTeamDaoImplTest clubTeamTest = new ClubTeamDaoImplTest();
        clubTeamTest.deleteAll(clubTeamDao);
        clubTeamTest.insertClubTeam(clubTeamDao, categoryDao, "My club team", true, 5);
        clubTeamTest.insertClubTeam(clubTeamDao, categoryDao, "My other club team", true, 5);
        int clubTeamId = clubTeamDao.getActiveClubTeams().get(0).getId();
        int clubTeamIdOther = clubTeamDao.getActiveClubTeams().get(1).getId();
        
        insertArticle(articleDao, "My caption 1", false, LocationType.NEWS, OwnerType.CLUB, categoryId, clubTeamId);
        insertArticle(articleDao, "My caption 2", false, LocationType.NEWS, OwnerType.CLUB, categoryId, clubTeamId);
        insertArticle(articleDao, "My caption 3", false, LocationType.NEWS, OwnerType.CLUB, categoryId, clubTeamId);
        insertArticle(articleDao, "My caption 4", false, LocationType.NEWS, OwnerType.CLUB, categoryId, clubTeamId);
        insertArticle(articleDao, "My caption 5", false, LocationType.NEWS, OwnerType.CLUB, categoryIdOther, clubTeamId);
        insertArticle(articleDao, "My caption 6", false, LocationType.NEWS, OwnerType.CLUB, categoryIdOther, clubTeamIdOther);
        insertArticle(articleDao, "My caption 7", false, LocationType.BULLETIN_BOARD, OwnerType.CLUB, categoryId, clubTeamId);
        insertArticle(articleDao, "My caption 8", false, LocationType.BULLETIN_BOARD, OwnerType.CLUB, categoryId, clubTeamId);
        insertArticle(articleDao, "My caption 9", false, LocationType.BULLETIN_BOARD, OwnerType.CLUB, categoryIdOther, clubTeamIdOther);
        
        // sqlOwnerCondition (in select) adds some parameters
        assertTrue(articleDao.selectArticles(clubTeamId, categoryId, LocationType.BULLETIN_BOARD).size() == 2);
        assertTrue(articleDao.selectArticles(clubTeamId, categoryId, LocationType.NEWS).size() == 5);
        assertTrue(articleDao.selectArticles(0, 0, LocationType.BULLETIN_BOARD).size() == 3);
        assertTrue(articleDao.selectArticles(0, 0, LocationType.NEWS).size() == 6);
    }
    
    public void insertArticle(ArticleDao articleDao, boolean priority, LocationType locationType) {
        Article a = new Article();
        a.setCaption("Caption");
        a.setContent("Content");
        a.setSummary("Summary");
        a.setLocationType(locationType);
        a.setCreationDate(new Date());
        if (priority) {
            a.setPriority(priority);
        }
        CategoryDaoImplTest categoryTest = new CategoryDaoImplTest();
        categoryTest.insertCategory(categoryDao, "My test category", true);
        int categoryId = categoryDao.getAllCategories().get(0).getId();
        a.setCategoryId(categoryId);
        
        ClubTeamDaoImplTest clubTeamTest = new ClubTeamDaoImplTest();
        clubTeamTest.insertClubTeam(clubTeamDao, categoryDao, "Test club team", true, 5);
        int clubTeamId = clubTeamDao.getAllClubTeams().get(0).getId();
        a.setClubTeamId(clubTeamId);
        
        articleDao.insertRow(a);
    }
    
    public void insertArticle(ArticleDao articleDao, String caption, boolean priority, LocationType locationType, OwnerType ownerType, int categoryId, int clubTeamId) {
        Article a = new Article();
        
        a.setCaption(caption);
        a.setPriority(priority);
        a.setLocationType(locationType);
        a.setOwnerType(ownerType);
        a.setCategoryId(categoryId);
        a.setClubTeamId(clubTeamId);
        a.setCreationDate(new Date());
        if (priority) {
            a.setPriority(priority);
        }
        
        articleDao.insertRow(a);
    }
    
    public void deleteAllArticles(ArticleDao articleDao) {
        for (Article a : articleDao.getAllArticles()) {
            articleDao.deleteRow(a.getId());
        }
    }
}
