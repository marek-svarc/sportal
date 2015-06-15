package com.clubeek.dao;

import java.util.List;

import com.clubeek.model.Contact;

public interface ContactDao extends Dao<Contact> {

    public void updateContacts(List<Contact> oldContacts, List<Contact> newContacts);
    
    public List<Contact> getContactsByClubMemberId(int clubMemberId);
}
