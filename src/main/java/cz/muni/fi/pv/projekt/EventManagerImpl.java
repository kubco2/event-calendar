package cz.muni.fi.pv.projekt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Zuzana Krejcova
 */
@Repository("eventManager")
public class EventManagerImpl implements EventManager {

    final static Logger log = LoggerFactory.getLogger(EventManagerImpl.class);

    private JdbcTemplate jdbc;
    private SimpleJdbcInsert insertEvent;

    private static UserManager userManager;
    
    @Autowired
    public EventManagerImpl(ApplicationContext springCtx) {
        userManager = (UserManagerImpl) springCtx.getBean("userManager");
    }

    @Resource
    public void setDataSource(DataSource dataSource) {
        jdbc = new JdbcTemplate(dataSource);
        insertEvent = new SimpleJdbcInsert(dataSource)
                .withTableName("events")
                .usingColumns("name", "owner", "place", "description", "timeFrom", "timeTo", "shared")
                .usingGeneratedKeyColumns("id");
    }

    private final ParameterizedRowMapper<Event> EVENT_MAPPER = new ParameterizedRowMapper<Event>() {
        @Override
        public Event mapRow(ResultSet rs, int i) throws SQLException {
            Event event = new Event();
            event.setId(rs.getLong("id"));
            event.setName(rs.getString("name"));
            event.setDescription(rs.getString("description"));
            event.setFrom(rs.getDate("timeFrom"));
            event.setTo(rs.getDate("timeTo"));
            User owner = userManager.selectUserById(rs.getLong("owner"));
            event.setOwner(owner);
            event.setPlace(rs.getString("place"));
            event.setShared(rs.getBoolean("shared"));
            return event;
        }
    };

    /**
     * create event and assign ID
     * @param evt event to create
     * @throws NullPointerException if event is null
     * @throws IllegalArgumentException if some of properties in event are null
     * @throws org.springframework.dao.DataAccessException runtime exception
     */
    @Override
    public void createEvent(Event evt) {
        log.debug("createEvent({})", evt);
        if (evt == null) throw new NullPointerException();
        if (nullOrEmpty(evt.getName()) || nullOrEmpty(evt.getPlace()) || nullOrEmpty(evt.getDescription())
                || null == evt.getFrom() || null == evt.getTo() || null == evt.getOwner() || null == evt.getOwner().getId()) {
            throw new IllegalArgumentException("Some attribute of this event is NULL.");
        }
        Map<String,Object> eventMap = new HashMap<String,Object>();
        eventMap.put("name",evt.getName());
        eventMap.put("description",evt.getDescription());
        eventMap.put("timeFrom",evt.getFrom());
        eventMap.put("timeTo",evt.getTo());
        eventMap.put("owner",evt.getOwner().getId());
        eventMap.put("place",evt.getPlace());
        eventMap.put("shared",evt.isShared());
        evt.setId(insertEvent.executeAndReturnKey(eventMap).longValue());

        ApplicationContext springCtx = new ClassPathXmlApplicationContext("spring-context.xml");
        CalendarManager calendarManager = (CalendarManager) springCtx.getBean("calendarManager");
        calendarManager.saveUserEvent(evt.getOwner(),evt);
    }

    /**
     * delete event
     * @param evt event to delete
     * @throws NullPointerException if event is null
     * @throws IllegalArgumentException if ID of event is null
     * @throws org.springframework.dao.DataAccessException runtime exception
     */
    @Override
    public void deleteEvent(Event evt) {
        log.debug("deleteEvent({})", evt);
        if (evt == null) throw new NullPointerException();
        if (evt.getId() == null) throw new IllegalArgumentException("The event does not have an ID yet.");
        jdbc.update("DELETE FROM events WHERE id=?", evt.getId());
    }

    /**
     * update content of event
     * @param evt event to update
     * @throws NullPointerException if event is null
     * @throws IllegalArgumentException if some of properties are null or empty
     * @throws org.springframework.dao.DataAccessException runtime exception
     */
    @Override
    public void updateEvent(Event evt) {
        log.debug("updateEvent({})", evt);
        if (evt == null) throw new NullPointerException();
        if (nullOrEmpty(evt.getName()) || nullOrEmpty(evt.getPlace()) || nullOrEmpty(evt.getDescription())
                || null == evt.getFrom() || null == evt.getTo() || null == evt.getOwner()|| null == evt.getOwner().getId()) {
            throw new IllegalArgumentException("Some attribute of this event is NULL.");
        }
        if (evt.getId() == null) throw new IllegalArgumentException("The user does not have an ID yet.");
        jdbc.update("UPDATE events SET name=?,place=?,description=?,timeFrom=?,timeTo=?,owner=?,shared=? WHERE id=?",
                evt.getName(), evt.getPlace(), evt.getDescription(), evt.getFrom(),
                evt.getTo(), evt.getOwner().getId(), evt.isShared(), evt.getId());
    }

    /**
     * select event by OD
     * @param id assigned to event
     * @return event with this ID
     * @throws NullPointerException if ID is null;
     * @throws org.springframework.dao.DataAccessException runtime exception
     */
    @Override
    public Event selectEventById(Long id) {
        log.debug("selectEventById({})", id);
        if (id == null) throw new NullPointerException();
        try {
            return jdbc.queryForObject("SELECT * FROM events WHERE id=?",
                                        EVENT_MAPPER, id);
        } catch(IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    /**
     * select all events in database
     * @return list of events
     * @throws org.springframework.dao.DataAccessException runtime exception
     */
    @Override
    public List<Event> selectAllEvents() {
        log.debug("selectAllEvents()");
        return jdbc.query("SELECT * FROM events",
                           EVENT_MAPPER);
    }

    /**
     * select all shared events from database
     * @return list of shared events
     * @throws org.springframework.dao.DataAccessException runtime exception
     */
    @Override
    public List<Event> selectSharedEvents() {
        log.debug("selectSharedEvents()");
        return jdbc.query("SELECT * FROM events WHERE shared=1",
                           EVENT_MAPPER);
    }

    private boolean nullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
