package com.clubeek.dao.impl.springjdbctemplate.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.clubeek.enums.ContactType;
import com.clubeek.enums.NotificationType;
import com.clubeek.model.Contact;

public class ContactMapper implements RowMapper<Contact> {

    @Override
    public Contact mapRow(ResultSet rs, int rowNum) throws SQLException {
        Contact c = new Contact();
        
        c.setId(rs.getInt("id"));
        c.setContact(rs.getString("contact"));
        c.setDescription(rs.getString("description"));
        c.setContactType(ContactType.values()[rs.getInt("contact_type")]);
        c.setNotificationType(NotificationType.values()[rs.getInt("notification_type")]);
        c.setClubMemberId(rs.getInt("club_member_id"));
        
        return c;
    }
}
