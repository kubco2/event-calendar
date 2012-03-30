package cz.muni.fi.pv.projekt;

import java.util.Date;
import java.util.List;
import java.util.Random;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author zkrejcov
 */
public class EventManagerImplTest extends TestWrapper {
    
    @Test
    public void testSaveNullEvent() {
        Event evt = null;
        try {
            eventManager.createEvent(evt);
            fail("Created a null event.");
        } catch (NullPointerException e) {}
    }
    
    @Test
    public void testSaveNullAttribute() {
        Event evt = new Event();
        evt.setName(generateString());
        evt.setShared(true);
        try {
            eventManager.createEvent(evt);
            fail("Created an event with null attributes.");
        } catch (IllegalArgumentException e) {}
    }
    
    @Test
    public void testSaveCorrect() {
        User usr = getNewUser();
        userManager.createUser(usr);
        Event evt = getNewEvent(usr);
        eventManager.createEvent(evt);
        Event evt2 = eventManager.selectEventById(evt.getId());
        assertTrue(evt.equals(evt2));
    }
    
    @Test
    public void testDeleteNull() {
        Event evt = null;
        try {
            eventManager.deleteEvent(evt);
            fail("Deleted a null event.");
        } catch (NullPointerException e) {}
    }
    
    @Test
    public void testDeleteNullId() {
        Event evt = new Event();
        evt.setName(generateString());
        evt.setShared(true);
        try {
            eventManager.deleteEvent(evt);
            fail("Deleted an event with null ID.");
        } catch (IllegalArgumentException e) {}
    }
    
    @Test
    public void testDeleteCorrect() {
        User usr = getNewUser();
        userManager.createUser(usr);
        Event evt = getNewEvent(usr);
        eventManager.createEvent(evt);
        
        eventManager.deleteEvent(evt);
        try {
            eventManager.selectEventById(evt.getId());
            fail("The event was not deleted.");
        } catch (Exception e) {}
    }
    
    @Test
    public void testUpdateNull() {
        Event evt = null;
        try {
            eventManager.updateEvent(evt);
            fail("Updated a null event.");
        } catch (NullPointerException e) {}
    }
    
    @Test
    public void testUpdateNullId() {
        Event evt = new Event();
        evt.setName(generateString());
        evt.setShared(true);
        try {
            eventManager.updateEvent(evt);
            fail("Updated an event with null ID.");
        } catch (IllegalArgumentException e) {}
    }
    
    @Test
    public void testUpdateCorrect() {
        User usr = getNewUser();
        userManager.createUser(usr);
        Event evt = getNewEvent(usr);
        eventManager.createEvent(evt);
        
        String name1 = evt.getName();
        String name2 = "updated event name";
        evt.setName(name2);
        
        eventManager.updateEvent(evt);
        eventManager.selectEventById(evt.getId());
        assertFalse(name1.equals(eventManager.selectEventById(evt.getId()).getName()));
    }
    
    @Test
    public void testSelectNull() {
        try {
            eventManager.selectEventById(null);
            fail("Selected a null ID event.");
        } catch (NullPointerException e) {}
    }
    
    @Test
    public void testSelectAll() {
        setUp();
        User usr = getNewUser();
        userManager.createUser(usr);
        Event evt1 = getNewEvent(usr);
        eventManager.createEvent(evt1);
        Event evt2 = getNewEvent(usr);
        eventManager.createEvent(evt2);
        Event evt3 = getNewEvent(usr, false);
        eventManager.createEvent(evt3);
        
        List<Event> evts = eventManager.selectAllEvents();
        assertEquals(3, evts.size());
    }
    
    @Test
    public void testSelectShared() {
        setUp();
        User usr = getNewUser();
        userManager.createUser(usr);
        Event evt1 = getNewEvent(usr);
        eventManager.createEvent(evt1);
        Event evt2 = getNewEvent(usr);
        eventManager.createEvent(evt2);
        Event evt3 = getNewEvent(usr, false);
        eventManager.createEvent(evt3);
        
        List<Event> evts = eventManager.selectSharedEvents();
        assertEquals(2, evts.size());
    }
    
    
    private User getNewUser() {
        User usr = new User();
        usr.setName(generateString());
        usr.setNick(generateString());
        usr.setPassword(generateString());
        return usr;
    }
    
    private Event getNewEvent(User owner) {
        return getNewEvent(owner, true);
    }
    
    private Event getNewEvent(User owner, boolean shared) {
        Event evt = new Event();
        evt.setName(generateString()); 
        evt.setDescription(generateString());
        evt.setFrom(new Date());
        evt.setTo(new Date());
        evt.setPlace(generateString());
        evt.setShared(shared);
        evt.setOwner(owner);
        return evt;
    }
    
    private String generateString() {
        Random rng = new Random();
        String characters="randomtextofsomeint";
        int length = 10;
        char[] text = new char[length];
        for (int i = 0; i < length; i++)
        {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }
}
