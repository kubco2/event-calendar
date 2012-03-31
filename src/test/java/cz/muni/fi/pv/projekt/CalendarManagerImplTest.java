package cz.muni.fi.pv.projekt;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Zuzka
 */
public class CalendarManagerImplTest  extends TestWrapper {

    /**
     * Test of saveUserEvent method.
     * Creates a user and event, saves them, adds the user to the event,
     * then adds another user and adds him to the event too. The then it verifies
     * that the user was added.
     */
    @Test
    public void testSaveUserEventCorrect() {
        User usr1 = getNewUser();
        userManager.createUser(usr1);
        usr1 = userManager.selectUserByNick(usr1.getNick());

        User usr2 = getNewUser();
        userManager.createUser(usr2);
        usr2 = userManager.selectUserByNick(usr2.getNick());

        Event evt = getNewEvent(usr1);
        eventManager.createEvent(evt);
        calendarManager.saveUserEvent(usr2, evt);

        assertTrue("The user wasn't added to the event!",
                calendarManager.getUsersForEvent(evt).contains(usr2));
    }
    
    @Test
    public void testSaveUserEventPrivate() {
        User usr1 = getNewUser();
        userManager.createUser(usr1);
        usr1 = userManager.selectUserByNick(usr1.getNick());

        User usr2 = getNewUser();
        userManager.createUser(usr2);
        usr2 = userManager.selectUserByNick(usr2.getNick());

        Event evt = getNewEvent(usr1, false);
        eventManager.createEvent(evt);
        
        try {
            calendarManager.saveUserEvent(usr2, evt);
            fail("Allowed to add another user to a private event.");
        } catch (Exception e) {}
    }

    /**
     * Test of saveUserEvent method.
     * Testing that null user can't be added.
     */
    @Test
    public void testSaveNullUser() {
        User usr1 = getNewUser();
        userManager.createUser(usr1);
        usr1 = userManager.selectUserByNick(usr1.getNick());
        
        User usr2 = null;

        Event evt = getNewEvent(usr1);
        eventManager.createEvent(evt);

        try {
            calendarManager.saveUserEvent(usr2, evt);
            fail("Null user was added to the event, no Exception was thrown.");
        } catch (NullPointerException npe) {}
    }
    
    @Test
    public void testSaveNullEvent() {
        User usr1 = getNewUser();
        userManager.createUser(usr1);
        usr1 = userManager.selectUserByNick(usr1.getNick());

        Event evt = null;

        try {
            calendarManager.saveUserEvent(usr1, evt);
            fail("User was added to null event, no Exception was thrown.");
        } catch (NullPointerException npe) {}
    }
    
    @Test
    public void testSaveNullUserID() {
        User usr1 = getNewUser();
        userManager.createUser(usr1);
        usr1 = userManager.selectUserByNick(usr1.getNick());
        
        User usr2 = getNewUser();

        Event evt = getNewEvent(usr1);
        eventManager.createEvent(evt);

        try {
            calendarManager.saveUserEvent(usr2, evt);
            fail("User with null ID was added to the event, no Exception was thrown.");
        } catch (IllegalArgumentException iae) {}
    }
    
    @Test
    public void testSaveNullEventID() {
        User usr1 = getNewUser();
        userManager.createUser(usr1);
        usr1 = userManager.selectUserByNick(usr1.getNick());
        
        User usr2 = getNewUser();
        userManager.createUser(usr2);
        usr2 = userManager.selectUserByNick(usr2.getNick());

        Event evt = getNewEvent(usr1);

        try {
            calendarManager.saveUserEvent(usr2, evt);
            fail("User was added to event with null ID, no Exception was thrown.");
        } catch (IllegalArgumentException iae) {}
    }

    /**
     * Test of deleteUserFromEvent method.
     * Creates a user and event, saves them, adds the user to the event,
     * then adds another user and adds him to the event too. After deleting one of the users from that event,
     * verify that the user is not "in" that event.
     */
    @Test
    public void testDeleteUserEventCorrect() {
        User usr1 = getNewUser();
        userManager.createUser(usr1);
        usr1 = userManager.selectUserByNick(usr1.getNick());

        User usr2 = getNewUser();
        userManager.createUser(usr2);
        usr2 = userManager.selectUserByNick(usr2.getNick());

        Event evt = getNewEvent(usr1);
        eventManager.createEvent(evt);
        calendarManager.saveUserEvent(usr2, evt);

        calendarManager.deleteUserFromEvent(usr1, evt);

        assertFalse("The user wasn't deleted from the event!",
                calendarManager.getUsersForEvent(evt).contains(usr1));
    }
    
    @Test
    public void testDeleteNullUserEvent() {
        User usr1 = getNewUser();
        userManager.createUser(usr1);
        usr1 = userManager.selectUserByNick(usr1.getNick());

        Event evt = getNewEvent(usr1);
        eventManager.createEvent(evt);

        try {
            calendarManager.deleteUserFromEvent(null, evt);
            fail("Allowed to remove a null user from event.");
        } catch (NullPointerException npe) {}
    }
    
    @Test
    public void testDeleteUserNullEvent() {
        User usr1 = getNewUser();
        userManager.createUser(usr1);
        usr1 = userManager.selectUserByNick(usr1.getNick());

        try {
            calendarManager.deleteUserFromEvent(usr1, null);
            fail("Allowed to remove a null event from users calendar.");
        } catch (NullPointerException npe) {}
    }
    
    @Test
    public void testDeleteNullUserIDEvent() {
        User usr1 = getNewUser();
        userManager.createUser(usr1);
        usr1 = userManager.selectUserByNick(usr1.getNick());

        Event evt = getNewEvent(usr1);
        eventManager.createEvent(evt);
        
        usr1.setId(null);

        try {
            calendarManager.deleteUserFromEvent(usr1, evt);
            fail("Allowed to remove a user with null id from event.");
        } catch (IllegalArgumentException iae) {}
    }
    
    @Test
    public void testDeleteUserNullEventID() {
        User usr1 = getNewUser();
        userManager.createUser(usr1);
        usr1 = userManager.selectUserByNick(usr1.getNick());

        Event evt = getNewEvent(usr1);
        eventManager.createEvent(evt);
        evt.setId(null);

        try {
            calendarManager.deleteUserFromEvent(usr1, evt);
            fail("Allowed to remove an event with null id from users calendar.");
        } catch (IllegalArgumentException iae) {}
    }
    
    
    @Test
    public void testGetEventsUserCorrect() {
        User usr = getNewUser();
        userManager.createUser(usr);
        usr = userManager.selectUserByNick(usr.getNick());

        Event evt1 = getNewEvent(usr);
        eventManager.createEvent(evt1);
        Event evt2 = getNewEvent(usr);
        eventManager.createEvent(evt2);
        Event evt3 = getNewEvent(usr);
        eventManager.createEvent(evt3);
        
        User usr2 = getNewUser();
        userManager.createUser(usr2);
        usr2 = userManager.selectUserByNick(usr2.getNick());

        Event evt4 = getNewEvent(usr2);
        eventManager.createEvent(evt4);
        calendarManager.saveUserEvent(usr, evt4);

        List<Event> evts = calendarManager.getEventsForUser(usr);

        assertEquals("Wrong number of events.",
                4, evts.size());
        for (Event evt : evts)
            if (!(evt.getId().equals(evt1.getId())
                || evt.getId().equals(evt2.getId())
                || evt.getId().equals(evt3.getId())
                || evt.getId().equals(evt4.getId())))
                fail("Got wrong event for this user.");
    }
    
    @Test
    public void testGetEventsNullUser() {
        try {
            calendarManager.getEventsForUser(null);
            fail("The user was null, should not have returned events.");
        } catch (NullPointerException npe) {}
    }
    
    @Test
    public void testGetEventsNullUserID() {
        User usr = getNewUser();
        try {
            calendarManager.getEventsForUser(usr);
            fail("The user has null ID, should not have returned events.");
        } catch (IllegalArgumentException iae) {}
    }
    
    @Test
    public void testGetUsersEventCorrect() {
        User usr1 = getNewUser();
        userManager.createUser(usr1);
        User usr2 = getNewUser();
        userManager.createUser(usr2);
        User usr3 = getNewUser();
        userManager.createUser(usr3);
        User usr4 = getNewUser();
        userManager.createUser(usr4);
        
        Event evt = getNewEvent(usr1);
        eventManager.createEvent(evt);
        
        calendarManager.saveUserEvent(usr2, evt);
        calendarManager.saveUserEvent(usr3, evt);
        calendarManager.saveUserEvent(usr4, evt);

        List<User> usrs = calendarManager.getUsersForEvent(evt);

        assertEquals("Wrong number of users.",
                4, usrs.size());
        for (User usr : usrs)
            if (!(usr.getId().equals(usr1.getId())
                || usr.getId().equals(usr2.getId())
                || usr.getId().equals(usr3.getId())
                || usr.getId().equals(usr4.getId())))
                fail("Got wrong user for this event.");
    }
    
    @Test
    public void testGetUsersNullEvent() {
        try {
            calendarManager.getUsersForEvent(null);
            fail("The event was null, should not have returned users.");
        } catch (NullPointerException npe) {}
    }
    
    @Test
    public void testGetUsersNullEventID() {
        User usr = getNewUser();
        userManager.createUser(usr);
        Event evt = getNewEvent(usr);
        try {
            calendarManager.getUsersForEvent(evt);
            fail("The event has null ID, should not have returned users.");
        } catch (IllegalArgumentException iae) {}
    }
}