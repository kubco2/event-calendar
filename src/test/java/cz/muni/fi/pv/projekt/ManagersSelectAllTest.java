package cz.muni.fi.pv.projekt;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author zkrejcov
 */
public class ManagersSelectAllTest extends TestWrapper {
    
    @BeforeClass
    public static void createData() {
        logger.info("CREATING USERS AND EVENTS");
        User usr1 = getNewUser();
        userManager.createUser(usr1);
        User usr2 = getNewUser();
        userManager.createUser(usr2);
        User usr3 = getNewUser();
        userManager.createUser(usr3);
        Event evt1 = getNewEvent(usr1);
        eventManager.createEvent(evt1);
        Event evt2 = getNewEvent(usr3);
        eventManager.createEvent(evt2);
        Event evt3 = getNewEvent(usr1, false);
        eventManager.createEvent(evt3);
        logger.info("CREATED USERS AND EVENTS");
    }
    
    
    @Test
    public void testSelectAllEvents() {
        List<Event> evts = eventManager.selectAllEvents();
        assertEquals(3, evts.size());
    }
    
    @Test
    public void testSelectSharedEvents() {
        List<Event> evts = eventManager.selectSharedEvents();
        assertEquals(2, evts.size());
    }
    
    @Test
    public void testSelectAllUsers() {
        List<User> users = userManager.selectAllUsers();
        assertEquals(3, users.size());
    }
}
