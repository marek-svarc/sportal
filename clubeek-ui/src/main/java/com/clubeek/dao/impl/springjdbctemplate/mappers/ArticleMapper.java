package com.clubeek.dao.impl.springjdbctemplate.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.clubeek.model.Article;
import com.clubeek.model.Article.Owner;

/**
 * Mapper for {@link Article} model object.
 * @author vitfo
 *
 */
public class ArticleMapper implements RowMapper<Article> {

    @Override
    public Article mapRow(ResultSet rs, int rowNum) throws SQLException {
        Article a = new Article();
        
        a.setId(rs.getInt("id"));
        a.setLocation(Article.Location.values()[rs.getInt("location")]);
        a.setPriority(rs.getBoolean("priority"));
        a.setCaption(rs.getString("caption"));
        a.setSummary(rs.getString("summary"));
        a.setContent(rs.getString("content"));
        a.setCreationDate(rs.getTimestamp("creation_date"));
        a.setExpirationDate(rs.getDate("expiration_date"));
        a.setOwner(Owner.values()[rs.getInt("owner_type")]);
        a.setClubTeamId(rs.getInt("club_team_id"));
        a.setCategoryId(rs.getInt("category_id"));
        
        return a;
    }

}
