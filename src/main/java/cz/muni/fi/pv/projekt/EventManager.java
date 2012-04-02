package cz.muni.fi.pv.projekt;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: xjanco
 * Date: 28.2.2012
 * Time: 8:09:53
 */
public interface EventManager {

    /**
     * create event and assign ID
     * @param evt event to create
     * @throws NullPointerException if event is null
     * @throws IllegalArgumentException if name of event is null or empty,
     *                                  if owner or owner's ID is null,
     *                                  if dates are null or date 'from' is after date 'to'
     */
    public void createEvent(Event evt);

    /**
     * delete event
     * @param evt event to delete
     * @throws NullPointerException if event is null
     * @throws IllegalArgumentException if name of event is null or empty,
     *                                  if owner or owner's ID is null,
     *                                  if dates are null or date 'from' is after date 'to'
     *                                  if event ID is null
     */
    public void deleteEvent(Event evt);

    /**
     * update content of event
     * @param evt event to update
     * @throws NullPointerException if event is null
     * @throws IllegalArgumentException if name of event is null or empty,
     *                                  if owner or owner's ID is null,
     *                                  if dates are null or date 'from' is after date 'to'
     *                                  if event ID is null
     */
    public void updateEvent(Event evt);

    /**
     * select event by ID
     * @param id assigned to event
     * @return event with this ID, or null if event with this ID doesn't exists
     * @throws NullPointerException if ID is null;
     */
    public Event selectEventById(Long id);

    /**
     * select all events in database
     * @return list of events
     */
    public List<Event> selectAllEvents();

    /**
     * select all shared events from database
     * @return list of shared events
     */
    public List<Event> selectSharedEvents();
}
