package cz.muni.fi.pv.projekt;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: xjanco
 * Date: 28.2.2012
 * Time: 8:18:42
 *
 */
public interface CalendarManager {

    /**
     * Adds the user to the event.
     * @param usr User
     * @param evt user is assigned to this event
     * @throws NullPointerException if any of the parameters is null.
     * @throws IllegalArgumentException if the user or the event don't have an id.
     */
    public void saveUserEvent(User usr, Event evt);

    /**
     * Revokes user's participation in the event.
     * @param usr User
     * @param evt Event
     * @throws NullPointerException if any of the parameters is null.
     * @throws IllegalArgumentException if the user or the event don't have an id.
     */
    public void deleteUserFromEvent(User usr, Event evt);

    /**
     * Lists all the events this user joined/created.
     * @param usr User
     * @return List of events this user joined/created.
     * @throws NullPointerException if the user is null.
     * @throws IllegalArgumentException if the user doesn't have an id.
     */
    public List<Event> getEventsForUser(User usr);

    /**
     * Lists all the users participating in this event.
     * @param evt Event
     * @return List of users for this event.
     * @throws NullPointerException if the event is null.
     * @throws IllegalArgumentException if the event doesn't have an id.
     */
    public List<User> getUsersForEvent(Event evt);
}
