package com.clubeek.dao.impl.springjdbctemplate;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clubeek.dao.ClubMemberDao;
import com.clubeek.dao.ContactDao;
import com.clubeek.enums.ContactType;
import com.clubeek.enums.NotificationType;
import com.clubeek.model.Contact;

/**
 * Class that tests ContactDaoImpl.
 *
 * @author vitfo
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-config.xml")
public class ContactDaoImplTest {
    
    @Autowired
    ContactDao contactDao;
    @Autowired
    ClubMemberDao clubMemberDao;
    
    @Before
    public void deleteAllContacts() {
        deleteAll(contactDao);
    }
    
    /**
     * Tests insert, update, delete, getByClubMemberId, getAll.
     */
    @Test
    public void test() {
        assertTrue(contactDao.getAllContacts().size() == 0);
        
        insertContact(contactDao, "777123456", "Phone contact", ContactType.PHONE, NotificationType.ALWAYS);
        insertContact(contactDao, "bla@centrum.cz", "Email contact", ContactType.EMAIL, NotificationType.ONLY_IMPORTANT);
        assertTrue(contactDao.getAllContacts().size() == 2);
        
        int clubMemberId = contactDao.getAllContacts().get(0).getClubMemberId();
        List<Contact> cList = contactDao.getContactsByClubMemberId(clubMemberId);
        assertTrue(cList.size() == 2);
        
        deleteAll(contactDao);
        assertTrue(contactDao.getAllContacts().size() == 0);
        
        insertContact(contactDao, "777123456", "Phone contact", ContactType.PHONE, NotificationType.ALWAYS);
        Contact c = contactDao.getAllContacts().get(0);
        assertTrue(c.getContact().equals("777123456"));
        assertTrue(c.getDescription().equals("Phone contact"));
        assertTrue(c.getContactType().equals(ContactType.PHONE));
        assertTrue(c.getNotificationType().equals(NotificationType.ALWAYS));
        
        c.setContact("mail@centrum.cz");
        c.setDescription("My email");
        c.setContactType(ContactType.EMAIL);
        c.setNotificationType(NotificationType.ONLY_IMPORTANT);
        contactDao.updateRow(c);
        assertTrue(contactDao.getAllContacts().size() == 1);
        
        Contact c1 = contactDao.getAllContacts().get(0);
        assertTrue(c1.getContact().equals("mail@centrum.cz"));
        assertTrue(c1.getDescription().equals("My email"));
        assertTrue(c1.getContactType().equals(ContactType.EMAIL));
        assertTrue(c1.getNotificationType().equals(NotificationType.ONLY_IMPORTANT));
    }
    
    @Test
    public void testGetContactsByClubMemberId() {
        insertContact(contactDao, "777123456", "Phone contact", ContactType.PHONE, NotificationType.ALWAYS);
        
        int clubMemberId = contactDao.getAllContacts().get(0).getClubMemberId();
        
        List<Contact> cList = contactDao.getContactsByClubMemberId(clubMemberId);
        Contact c = cList.get(0);
        assertTrue(c.getContact().equals("777123456"));
        assertTrue(c.getDescription().equals("Phone contact"));
        assertTrue(c.getContactType().equals(ContactType.PHONE));
        assertTrue(c.getNotificationType().equals(NotificationType.ALWAYS));
    }

    @Test
    public void testUpdateContacts() {
        insertContact(contactDao, "777123456", "Phone contact", ContactType.PHONE, NotificationType.ALWAYS);
        insertContact(contactDao, "mail@centrum.cz", "Email contact", ContactType.EMAIL, NotificationType.ONLY_IMPORTANT);
        insertContact(contactDao, "abcd@outlook.com", "My email contact", ContactType.EMAIL, NotificationType.NEVER);
        
        List<Contact> oldContacts = contactDao.getAllContacts();
        assertTrue(oldContacts.size() == 3);
        
        // create new contact
        Contact newContact = new Contact();
        newContact.setClubMemberId(oldContacts.get(0).getClubMemberId());
        newContact.setContact("608456789");
        newContact.setContactType(ContactType.PHONE);
        newContact.setDescription("abcd");
        newContact.setNotificationType(NotificationType.ONLY_IMPORTANT);
        
        // create list of new contact and updated contact.
        List<Contact> newContacts = new ArrayList<Contact>();
        newContacts.add(newContact);
        // update email contact
        for (Contact con : oldContacts) {
            if (con.getDescription().equals("My email contact")) {
                assertTrue(con.getContact().equals("abcd@outlook.com"));
                assertTrue(con.getNotificationType().equals(NotificationType.NEVER));
                
                con.setContact("koudelka@atlas.cz");
                con.setNotificationType(NotificationType.ALWAYS);
                newContacts.add(con);
            }
        }
        // before update
        assertTrue(contactDao.getAllContacts().size() == 3);
        contactDao.updateContacts(oldContacts, newContacts);
        // after update
        assertTrue(contactDao.getAllContacts().size() == 2);
        List<Contact> list = contactDao.getAllContacts();
        for (Contact nc : list) {
            if (nc.getContact().equals("koudelka@atlas.cz")) {
                assertTrue(nc.getNotificationType().equals(NotificationType.ALWAYS));
            } else {
                assertTrue(nc.getContact().equals("608456789"));
                assertTrue(nc.getDescription().equals("abcd"));
                assertTrue(nc.getContactType().equals(ContactType.PHONE));
                assertTrue(nc.getNotificationType().equals(NotificationType.ONLY_IMPORTANT));
            }
        }
    }
    
    public void insertContact(ContactDao contactDao, String contact, String description, ContactType contactType, NotificationType notificationType) {
        ClubMemberDaoImplTest clubMemberTest = new ClubMemberDaoImplTest();
        clubMemberTest.insertClubMembers(clubMemberDao, 1);
        int clubMemberId = clubMemberDao.getAllClubMembers().get(0).getId();
        
        Contact c = new Contact();
        c.setClubMemberId(clubMemberId);
        c.setContact(contact);
        c.setContactType(contactType);
        c.setDescription(description);
        c.setNotificationType(notificationType);
        contactDao.insertRow(c);
    }

    public void deleteAll(ContactDao contactDao) {
        for (Contact c : contactDao.getAllContacts()) {
            contactDao.deleteRow(c.getId());
        }
    }
}
