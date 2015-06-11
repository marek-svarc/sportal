package com.clubeek.dao;

import java.util.List;

import com.clubeek.dao.impl.ownframework.rep.RepArticle;
import com.clubeek.db.Repository;
import com.clubeek.model.Article;
import com.clubeek.model.Article.Location;
import com.clubeek.model.Unique;

public interface ArticleDao extends Dao<Article> {

    public Article getArticleById(int id);
    
    public List<Article> getAllArticles();
    
    public List<Article> selectArticles(int clubTeamId, int categoryId, Location location);
    
//    public RepArticle getInstance();
}
