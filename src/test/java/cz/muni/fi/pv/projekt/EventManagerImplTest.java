package cz.muni.fi.pv.projekt;

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
    public void testSelectNullEventId() {
        try {
            eventManager.selectEventById(null);
            fail("Selected a null ID event.");
        } catch (NullPointerException e) {}
    }
}
