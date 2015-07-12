package com.clubeek.dao.impl.springjdbctemplate;

import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clubeek.dao.ArticleDao;
import com.clubeek.dao.CategoryDao;
import com.clubeek.dao.ClubDao;
import com.clubeek.dao.ClubTeamDao;
import com.clubeek.enums.LocationType;
import com.clubeek.enums.OwnerType;
import com.clubeek.enums.SportType;
import com.clubeek.model.Article;
import com.clubeek.model.DiscussionPost;

/**
 * Class that tests ArticleDaoImpl.
 * 
 * @author vitfo
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-config.xml")
public class ArticleDaoImplTest {
    @Autowired
    ArticleDao articleDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    ClubTeamDao clubTeamDao;
    @Autowired
    ClubDao clubDao;
    
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
        a.setOwnerType(OwnerType.CLUB);
        articleDao.updateRow(a);
        
        Article idArt = articleDao.getArticleById(id);
        assertTrue("Content shoud be 'My content'", idArt.getContent().equals("My content"));
        assertTrue("Summary shoud be 'My summary'", idArt.getSummary().equals("My summary"));
        assertTrue("Caption shoud be 'My caption'", idArt.getCaption().equals("My caption"));
        assertTrue("Owner type shoud be CLUB", idArt.getOwnerType().equals(OwnerType.CLUB));
        
        // delete
        articleDao.deleteRow(id);
        assertTrue("The article should be deleted", articleDao.getAllArticles().size() == 0);
    }
    
    @Test
    public void deleteRowsTest() {
        assertTrue(articleDao.getAllArticles().size() == 0);
        
        insertArticles(articleDao, 10);
        assertTrue(articleDao.getAllArticles().size() == 10);
        
        List<Article> articles = articleDao.getAllArticles();
        List<Article> articlesToDelete = articles.subList(3, 8);
        articleDao.deleteRows(articlesToDelete);
        assertTrue(articleDao.getAllArticles().size() == 5);
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
        assertTrue(articleDao.getAllArticles().size() == 0);
        
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
        clubTeamTest.insertClubTeam(clubTeamDao, categoryDao, clubDao, "My club team", SportType.BASEBALL, true, 5);
        clubTeamTest.insertClubTeam(clubTeamDao, categoryDao, clubDao, "My other club team", SportType.BASEBALL, true, 5);
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
        assertTrue(articleDao.selectArticles(clubTeamId, categoryId, LocationType.BULLETIN_BOARD).size() == 5);
        assertTrue(articleDao.selectArticles(clubTeamId, categoryId, LocationType.NEWS).size() == 7);
        assertTrue(articleDao.selectArticles(0, 0, LocationType.BULLETIN_BOARD).size() == 6);
        assertTrue(articleDao.selectArticles(0, 0, LocationType.NEWS).size() == 8);
    }
    
    /**
     * Tests insert, delete, update, getAll for discussion post.
     */
    @Test
    public void testDiscussionPosts() {
        assertTrue(articleDao.getAllArticles().size() == 0);
        
        // insert
        insertArticles(articleDao, 1);
        int articleId = articleDao.getAllArticles().get(0).getId();
        assertTrue(articleDao.getAllDiscussionPosts(articleId).size() == 0);
        Calendar creation = new GregorianCalendar();
        creation.setTime(new Date(0L));
        creation.set(2014,  5, 24, 17, 38, 51);
        insertDiscussionPost(articleDao, creation.getTime(), "My comment", articleId);
        
        assertTrue(articleDao.getAllDiscussionPosts(articleId).size() == 1);
        DiscussionPost dp = articleDao.getAllDiscussionPosts(articleId).get(0);
        assertTrue(dp.getCreationTime().getTime() == creation.getTimeInMillis());
        assertTrue(dp.getComment().equals("My comment"));
        
        // update
        Calendar creation2 = new GregorianCalendar();
        creation2.setTime(new Date(0L));
        creation2.set(2015,  7, 19, 22, 13, 03);
        dp.setCreationTime(creation2.getTime());
        dp.setComment("Comment");
        articleDao.updateDiscussionPost(dp);
        
        assertTrue(articleDao.getAllDiscussionPosts(articleId).size() == 1);
        DiscussionPost dp2 = articleDao.getAllDiscussionPosts(articleId).get(0);
        assertTrue(dp2.getCreationTime().getTime() == creation2.getTimeInMillis());
        assertTrue(dp2.getComment().equals("Comment"));
        
        // delete
        articleDao.deleteDiscussionPost(dp2.getId());
        assertTrue(articleDao.getAllDiscussionPosts(articleId).size() == 0);
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
        
        // categoryId and clubTeamId might be null
//        CategoryDaoImplTest categoryTest = new CategoryDaoImplTest();
//        categoryTest.insertCategory(categoryDao, "My test category", true);
//        int categoryId = categoryDao.getAllCategories().get(0).getId();
//        a.setCategoryId(categoryId);
//        
//        ClubTeamDaoImplTest clubTeamTest = new ClubTeamDaoImplTest();
//        clubTeamTest.insertClubTeam(clubTeamDao, categoryDao, "Test club team", true, 5);
//        int clubTeamId = clubTeamDao.getAllClubTeams().get(0).getId();
//        a.setClubTeamId(clubTeamId);
        
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
    
    public void insertArticles(ArticleDao articleDao, int numOfArticles) {
        Random gen = new Random();
        for (int i = 0; i < numOfArticles; i++) {
            boolean priority = (gen.nextInt(2) == 0) ? true : false;
            LocationType locationType = (gen.nextInt(2) == 0) ? LocationType.BULLETIN_BOARD : LocationType.NEWS;
            insertArticle(articleDao, priority, locationType);
        }
    }
    
    public void deleteAllArticles(ArticleDao articleDao) {
        for (Article a : articleDao.getAllArticles()) {
            articleDao.deleteRow(a.getId());
        }
    }
    
    public void insertDiscussionPost(ArticleDao articleDao, Date creationTime, String comment, int articleId) {
        DiscussionPost dp = new DiscussionPost();
        dp.setCreationTime(creationTime);
        dp.setComment(comment);
        dp.setReferencedObjectId(articleId);
        articleDao.insertDiscussionPost(dp);
    }
}
