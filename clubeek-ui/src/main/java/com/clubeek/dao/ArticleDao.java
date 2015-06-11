package com.clubeek.dao;

import java.util.List;

import com.clubeek.dao.impl.ownframework.rep.RepArticle;
import com.clubeek.model.Article;
import com.clubeek.model.Article.Location;

public interface ArticleDao {

    public Article getArticle(int id);
    
    public List<Article> getAllArticles();
    
    public RepArticle getRepArticleInstance();
    
    public List<Article> selectArticles(int clubTeamId, int categoryId, Location location);
}
