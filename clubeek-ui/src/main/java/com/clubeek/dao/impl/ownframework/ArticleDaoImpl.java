package com.clubeek.dao.impl.ownframework;

import java.sql.ResultSet;
import java.util.List;

import com.clubeek.dao.ArticleDao;
import com.clubeek.dao.impl.ownframework.rep.RepArticle;
import com.clubeek.model.Article;
import com.clubeek.model.Article.Location;
import com.clubeek.model.Unique;

public class ArticleDaoImpl implements ArticleDao {

    @Override
    public Article getArticleById(int id) {
        // return RepArticle.selectById(id, new RepArticle.TableColumn[]{RepArticle.TableColumn.CAPTION,
        // RepArticle.TableColumn.CONTENT, RepArticle.TableColumn.CREATION_DATE});
        // TODO vitfo, created on 11. 6. 2015 - zkontorlovat zda funguje i pro ne null
        return RepArticle.selectById(id, null);
    }

    @Override
    public List<Article> getAllArticles() {
        return RepArticle.selectAll(null);
    }

    
//     @Override
//     public RepArticle getInstance() {
//     return RepArticle.getInstance();
//     }

    @Override
    public List<Article> selectArticles(int clubTeamId, int categoryId, Location location) {
        return RepArticle.select(clubTeamId, categoryId, location, null);
    }

    @Override
    public void updateRow(Article object) {
        RepArticle.update(object);        
    }

    @Override
    public void insertRow(Article object) {
        RepArticle.insert(object);
    }

    @Override
    public void deleteRow(int id) {
        RepArticle.delete(id);        
    }

    @Override
    public void exchangeRows(int idA, int idB) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not supported.");
    }

}
