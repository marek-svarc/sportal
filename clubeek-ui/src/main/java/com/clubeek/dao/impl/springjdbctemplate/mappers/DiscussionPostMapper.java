package com.clubeek.dao.impl.springjdbctemplate.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.clubeek.model.DiscussionPost;

public class DiscussionPostMapper implements RowMapper<DiscussionPost> {

    @Override
    public DiscussionPost mapRow(ResultSet rs, int rowNum) throws SQLException {
        DiscussionPost d = new DiscussionPost();
        
        d.setId(rs.getInt("id"));
        d.setCreationTime(rs.getTimestamp("creation_time"));
        d.setComment(rs.getString("comment"));
        d.setReferencedObjectId(rs.getInt("article_id"));
        
        return d;
    }

}
