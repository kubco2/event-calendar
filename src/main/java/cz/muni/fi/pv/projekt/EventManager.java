package cz.muni.fi.pv.projekt;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: xjanco
 * Date: 28.2.2012
 * Time: 8:09:53
 * To change this template use File | Settings | File Templates.
 */
public interface EventManager {

    public void createEvent(Event evt);
    public void deleteEvent(Event evt);
    public void updateEvent(Event evt);
    public Event selectEventById(Long id);
    public List<Event> selectAllEvents();
    public List<Event> selectSharedEvents();
}
