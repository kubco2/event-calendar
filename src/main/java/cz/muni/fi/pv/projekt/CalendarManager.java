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

    public void saveUserEvent(User usr, Event evt);
    public void deleteUserFromEvent(User usr, Event evt);
    public List<Event> getEventsForUser(User usr);
    public List<User> getUsersForEvent(Event evt);
}
