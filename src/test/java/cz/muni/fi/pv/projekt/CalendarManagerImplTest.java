package cz.muni.fi.pv.projekt;

import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Zuzka
 */
public class CalendarManagerImplTest {

    private CalendarManagerImpl calendarManager;
    private EventManagerImpl eventManager;
    private UserManagerImpl userManager;

    @Before
    public void setUp() {
        calendarManager = new CalendarManagerImpl();
        eventManager = new EventManagerImpl();
        userManager = new UserManagerImpl();
    }

    /**
     * Test of saveUserEvent method.
     * Creates a user and event, saves them, adds the user to the event,
     * then adds another user and adds him to the event too. The then it verifies
     * that the user was added.
     */
    @Test
    public void testSaveUserEvent() {
        User usr1 = createUser("John Doe", "G.I.Joe", "thisIsAVeryCleverPassword!");
        userManager.createUser(usr1);
        usr1 = userManager.selectUserByNick("G.I.Joe");

        User usr2 = createUser("Jane Doe", "G.I.Jane", "thisIsEvenBetter!");
        userManager.createUser(usr2);
        usr2 = userManager.selectUserByNick("G.I.Jane");

        Event evt = createEvent(usr1.getId(), "THE best event ever", "everywhere",
                "the title says it all", new Date(), new Date(), true);
        eventManager.createEvent(evt);
        calendarManager.saveUserEvent(usr2, evt);

        assertTrue("The user wasn't added to the event!",
                calendarManager.getUsersForEvent(evt).contains(usr2));
    }

    /**
     * Test of saveUserEvent method.
     * Testing that null user can't be added.
     */
    @Test
    public void testSaveNullUser() {
        User usr = null;

        Event evt = createEvent(usr.getId(), "THE best event ever", "everywhere",
                "the title says it all", new Date(), new Date(), true);
        eventManager.createEvent(evt);

        try {
            calendarManager.saveUserEvent(usr, evt);
            fail("Null user was added to the event, no Exception was thrown.");
        } catch (NullPointerException npe) {
            // the exception is expected and correct behavior
        }
    }

    /**
     * Test of deleteUserFromEvent method.
     * Creates a user and event, saves them, adds the user to the event,
     * then adds another user and adds him to the event too. After deleting one of the users from that event,
     * verify that the user is not "in" that event.
     */
    @Test
    public void testDeleteUserFromEvent() {
        User usr1 = createUser("John Doe", "G.I.Joe", "thisIsAVeryCleverPassword!");
        userManager.createUser(usr1);
        usr1 = userManager.selectUserByNick("G.I.Joe");

        User usr2 = createUser("Jane Doe", "G.I.Jane", "thisIsEvenBetter!");
        userManager.createUser(usr2);
        usr2 = userManager.selectUserByNick("G.I.Jane");

        Event evt = createEvent(usr1.getId(), "THE best event ever", "everywhere",
                "the title says it all", new Date(), new Date(), true);
        eventManager.createEvent(evt);
        calendarManager.saveUserEvent(usr2, evt);

        calendarManager.deleteUserFromEvent(usr1, evt);

        assertFalse("The user wasn't deleted from the event!",
                calendarManager.getUsersForEvent(evt).contains(usr1));
    }


    // private method SHORTCUTS from here on

    private User createUser(String name, String nick, String password) {
        User user = new User();
        user.setName(name);
        user.setNick(nick);
        user.setPassword(password);
        return user;
    }

    private Event createEvent(Long owner, String name, String place, String description, Date from, Date to, boolean shared) {
        Event event = new Event();
        event.setOwner(owner);
        event.setName(name);
        event.setPlace(place);
        event.setDescription(description);
        event.setFrom(from);
        event.setTo(to);
        event.setShared(shared);
        return event;
    }
}