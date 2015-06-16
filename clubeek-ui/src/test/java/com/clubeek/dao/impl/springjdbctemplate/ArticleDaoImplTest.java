package com.clubeek.dao.impl.springjdbctemplate;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clubeek.dao.ArticleDao;
import com.clubeek.dao.CategoryDao;
import com.clubeek.model.Article;
import com.clubeek.model.Category;
import com.clubeek.model.Article.Owner;

/**
 * Class that tests {@link ArticleDaoImpl}.
 * This implementation swapped {@link com.clubeek.dao.impl.ownframework.ArticleDaoImpl} so the test is comparing results from old implementation with the new one.
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
        insertArticle(articleDao, false);
        
        // get all articles
        int numOfArticles = articleDao.getAllArticles().size();
        assertTrue(numOfArticles > 0);
        
        // update one article
        Article a = articleDao.getAllArticles().get(0);
        int id = a.getId();
        a.setCaption("My caption");
        a.setSummary("My summary");
        a.setContent("My content");
        a.setOwner(Owner.TEAM);
        articleDao.updateRow(a);
        
        Article idArt = articleDao.getArticleById(id);
        assertTrue("Content shoud be 'My content'", idArt.getContent().equals("My content"));
        assertTrue("Summary shoud be 'My summary'", idArt.getSummary().equals("My summary"));
        assertTrue("Caption shoud be 'My caption'", idArt.getCaption().equals("My caption"));
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
//    public void testGetArticleById() {
//        fail("Not yet implemented");
//    }
//
//    @Test
//    public void testGetAllArticles() {
//        fail("Not yet implemented");
//    }
//
//    @Test
//    public void testSelectArticles() {
//        fail("Not yet implemented");
//    }
//    
//    @Test
//    public void getAllArticlesTest() {
//        // Comparing results of new and old implementations.
////        int rowsNew = articleDao.getAllArticles().size();
////        int rowsOld = RepArticle.selectAll(null).size();
////        
////        assertTrue("The row number shoud equal", rowsNew == rowsOld);
//    }
//    
//    @Test
//    public void selectArticlesTest() {
//        // Comparing results of new and old implementations.
////        int rowsNew = articleDao.selectArticles(0, 0, Article.Location.NEWS).size();
////        int rowsOld = RepArticle.select(0, 0, Article.Location.NEWS, null).size();
////        
////        assertTrue("The row number shoud equal", rowsNew == rowsOld);
////        
////        int rowsNew02 = articleDao.selectArticles(0, 0, Article.Location.BULLETIN_BOARD).size();
////        int rowsOld02 = RepArticle.select(0, 0, Article.Location.BULLETIN_BOARD, null).size();
////        
////        assertTrue("The row number shoud equal", rowsNew02 == rowsOld02);
//    }
    
    public static void insertArticle(ArticleDao articleDao, boolean priority) {
        Article a = new Article();
        a.setCaption("Caption");
        a.setContent("Content");
        a.setSummary("Summary");
        a.setCreationDate(new Date());
        if (priority) {
            a.setPriority(priority);
        }
//        insertCategory("My category");
//        int categoryId = categoryDao.getAllCategories().get(0).getId();
//        a.setCategoryId(categoryId);
        
        articleDao.insertRow(a);
    }
    
    public static void deleteAllArticles(ArticleDao articleDao) {
        for (Article a : articleDao.getAllArticles()) {
            articleDao.deleteRow(a.getId());
        }
    }
}
