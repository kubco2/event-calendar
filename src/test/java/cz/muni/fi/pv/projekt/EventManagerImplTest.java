package cz.muni.fi.pv.projekt;

import java.util.Date;
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
    
    
    
    
    private User getNewUser() {
        User usr = new User();
        usr.setName(generateString());
        usr.setNick(generateString());
        usr.setPassword(generateString());
        return usr;
    }
    
    private Event getNewEvent(User owner) {
        Event evt = new Event();
        evt.setName(generateString()); 
        evt.setDescription(generateString());
        evt.setFrom(new Date());
        evt.setTo(new Date());
        evt.setPlace(generateString());
        evt.setShared(true);
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
