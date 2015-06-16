package com.clubeek.dao.impl.springjdbctemplate;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.clubeek.dao.ArticleDao;
import com.clubeek.dao.impl.ownframework.rep.RepArticle.TableColumn;
import com.clubeek.dao.impl.springjdbctemplate.mappers.ArticleMapper;
import com.clubeek.model.Article;
import com.clubeek.model.Article.Location;
import com.clubeek.model.Article.Owner;

/**
 * Implementation of @link {@link ArticleDao} interface.
 *
 * @author vitfo
 */
@Repository
public class ArticleDaoImpl implements ArticleDao {

    private JdbcTemplate template;

    @Autowired
    public void init(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public void updateRow(Article object) {
//        SqlParameterSource params = new BeanPropertySqlParameterSource(object);
//        Cannot use params (and NamedParameterJdbcTemplate) because of enums in object and their ordinal() method that is called.
        
        template.update("update t_article set"
                + "location = ?, "
                + "priority = ?, "
                + "caption = ?, "
                + "summary = ?, "
                + "content = ?, "
                + "creation_date = ?, "
                + "expiration_date = ?, "
                + "owner_type = ?, "
                + "club_team_id = ?, "
                + "category_id = ?"
                , new Object[] {
                        object.getLocation().ordinal(),
                        object.getPriority(),
                        object.getCaption(),
                        object.getSummary(),
                        object.getContent(),
                        object.getCreationDate(),
                        object.getExpirationDate(),
                        object.getOwner().ordinal(),
                        object.getClubTeamId(),
                        object.getCategoryId()
                });
    }

    @Override
    public void insertRow(Article object) {
        template.update("insert into t_article ("
                + "location, "
                + "priority, "
                + "caption, "
                + "summary, "
                + "content, "
                + "creation_date, "
                + "expiration_date, "
                + "owner_type, "
                + "club_team_id, "
                + "category_id"
                + ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 
                new Object[] {
                        object.getLocation().ordinal(),
                        object.getPriority(),
                        object.getCaption(),
                        object.getSummary(),
                        object.getContent(),
                        object.getCreationDate(),
                        object.getExpirationDate(),
                        object.getOwner().ordinal(),
                        object.getClubTeamId(),
                        object.getCategoryId()
                });
    }

    @Override
    public void deleteRow(int id) {
        template.update("delete from t_article where id = ?", new Integer[]{id});
    }

    @Override
    public void exchangeRows(int idA, int idB) {
        throw new UnsupportedOperationException();
    }   

    @Override
    public Article getArticleById(int id) {
        return template.queryForObject("select * from t_article where id = ?", new Integer[]{id}, new ArticleMapper());
    }

    @Override
    public List<Article> getAllArticles() {
        return template.query("select * from t_article", new ArticleMapper());
    }

    @Override
    public List<Article> selectArticles(int clubTeamId, int categoryId, Location location) {
        String str = String.format("SELECT * FROM t_article WHERE %s AND %s AND %s ORDER BY %s DESC, %s DESC",
                sqlOwnerCondition(clubTeamId, categoryId),
                sqlLocationCondition(location), 
                sqlExpiredDateCondition(), "priority", "creation_date");
        return template.query(str, new ArticleMapper());
    }
    
    private String sqlOwnerCondition(int clubTeamId, int categoryId) {
        if ((clubTeamId > 0) || (categoryId > 0)) {
            return String.format("((%s = %d) OR (%s = %d) OR (%s = %d))", TableColumn.CLUB_TEAM_ID, clubTeamId,
                    "category_id", categoryId, "owner_type", Owner.CLUB_ALL.ordinal());
        } else {
            return String.format("((%s = %d) OR (%s = %d))", "owner_type", Owner.CLUB_ALL.ordinal(),
                    "owner_type", Owner.CLUB.ordinal());
        }
    }

    private String sqlExpiredDateCondition() {
        return String.format("((%s is null) OR (LOCALTIMESTAMP <= %s))", "expiration_date", "expiration_date");
    }
    
    private static String sqlLocationCondition(Location location) {
        return String.format("(%s = %d)", "location", location.ordinal());
    }
}
