package com.clubeek.dao.impl.springjdbctemplate.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.clubeek.model.Contact;

public class ContactMapper implements RowMapper<Contact> {

    @Override
    public Contact mapRow(ResultSet rs, int rowNum) throws SQLException {
        Contact c = new Contact();
        
        c.setId(rs.getInt("id"));
        c.setContact(rs.getString("contact"));
        c.setDescription(rs.getString("description"));
        // TODO vitfo, created on 16. 6. 2015 - enum
//        c.setType(rs.getInt("type"));
//        c.setNotification(notification);
        c.setClubMemberId(rs.getInt("club_member_id"));
        
        return c;
    }
}
