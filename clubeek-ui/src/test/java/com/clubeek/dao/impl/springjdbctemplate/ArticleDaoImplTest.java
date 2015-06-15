package com.clubeek.dao.impl.springjdbctemplate;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import com.clubeek.dao.ArticleDao;
import com.clubeek.dao.impl.ownframework.rep.RepArticle;
import com.clubeek.model.Article;

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
    
    @Test
    public void getAllArticlesTest() {
        // Comparing results of new and old implementations.
        int rowsNew = articleDao.getAllArticles().size();
        int rowsOld = RepArticle.selectAll(null).size();
        
        assertTrue("The row number shoud equal", rowsNew == rowsOld);
    }
    
    @Test
    public void selectArticlesTest() {
        // Comparing results of new and old implementations.
        int rowsNew = articleDao.selectArticles(0, 0, Article.Location.NEWS).size();
        int rowsOld = RepArticle.select(0, 0, Article.Location.NEWS, null).size();
        
        assertTrue("The row number shoud equal", rowsNew == rowsOld);
        
        int rowsNew02 = articleDao.selectArticles(0, 0, Article.Location.BULLETIN_BOARD).size();
        int rowsOld02 = RepArticle.select(0, 0, Article.Location.BULLETIN_BOARD, null).size();
        
        assertTrue("The row number shoud equal", rowsNew02 == rowsOld02);
    }
}
