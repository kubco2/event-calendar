package cz.muni.fi.pv.projekt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    private final static Logger log = LoggerFactory.getLogger(EventManagerImpl.class);
    private JdbcTemplate jdbc;
    private SimpleJdbcInsert insertEvent;
    private static UserManager userManager;
    private static CalendarManager calendarManager;

    @Autowired
    public EventManagerImpl(ApplicationContext springCtx) {
        if(userManager == null || calendarManager == null) {
            initManagers(springCtx);
        }
    }

    @Resource
    public void setDataSource(DataSource dataSource) {
        jdbc = new JdbcTemplate(dataSource);
        insertEvent = new SimpleJdbcInsert(dataSource)
                .withTableName("events")
                .usingColumns("name", "owner", "place", "description", "timeFrom", "timeTo", "shared")
                .usingGeneratedKeyColumns("id");
    }

    private static final ParameterizedRowMapper<Event> EVENT_MAPPER = new ParameterizedRowMapper<Event>() {
        @Override
        public Event mapRow(ResultSet rs, int i) throws SQLException {
            Event event = new Event();
            event.setId(rs.getLong("id"));
            event.setName(rs.getString("name"));
            event.setDescription(rs.getString("description"));
            event.setFrom(rs.getTimestamp("timeFrom"));
            event.setTo(rs.getTimestamp("timeTo"));
            User owner = userManager.selectUserById(rs.getLong("owner"));
            event.setOwner(owner);
            event.setPlace(rs.getString("place"));
            event.setShared(rs.getBoolean("shared"));
            return event;
        }
    };

    @Override
    @Transactional
    public void createEvent(Event evt) {
        log.debug("createEvent({})", evt);
        eventValidation(evt);

        Map<String,Object> eventMap = new HashMap<String,Object>();
        eventMap.put("name",evt.getName());
        eventMap.put("description",evt.getDescription());
        eventMap.put("timeFrom",evt.getFrom());
        eventMap.put("timeTo",evt.getTo());
        eventMap.put("owner",evt.getOwner().getId());
        eventMap.put("place",evt.getPlace());
        eventMap.put("shared",evt.isShared());
        evt.setId(insertEvent.executeAndReturnKey(eventMap).longValue());

        calendarManager.saveUserEvent(evt.getOwner(), evt);
    }

    @Override
    public void deleteEvent(Event evt) {
        log.debug("deleteEvent({})", evt);
        eventValidation(evt);
        if(evt.getId() == null) {
            throw new IllegalArgumentException("The event hasn't ID");
        }
        jdbc.update("DELETE FROM events WHERE id=?", evt.getId());
    }

    @Override
    public void updateEvent(Event evt) {
        log.debug("updateEvent({})", evt);
        eventValidation(evt);
        if(evt.getId() == null) {
            throw new IllegalArgumentException("The event hasn't ID");
        }

        jdbc.update("UPDATE events SET name=?,place=?,description=?,timeFrom=?,timeTo=?,owner=?,shared=? WHERE id=?",
                     evt.getName(), evt.getPlace(), evt.getDescription(), evt.getFrom(),
                     evt.getTo(), evt.getOwner().getId(), evt.isShared(), evt.getId());
    }

    @Override
    public Event selectEventById(Long id) {
        log.debug("selectEventById({})", id);
        if (id == null) {
            throw new NullPointerException();
        }

        try {
            return jdbc.queryForObject("SELECT * FROM events WHERE id=?",
                                        EVENT_MAPPER, id);
        } catch(IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Event> selectAllEvents() {
        log.debug("selectAllEvents()");
        return jdbc.query("SELECT * FROM events",
                           EVENT_MAPPER);
    }

    @Override
    public List<Event> selectSharedEvents() {
        log.debug("selectSharedEvents()");
        return jdbc.query("SELECT * FROM events WHERE shared=1",
                           EVENT_MAPPER);
    }

    private synchronized void initManagers(ApplicationContext springCtx) {
        if(userManager == null) {
            userManager = (UserManager) springCtx.getBean("userManager");
        }
        if(calendarManager == null) {
            calendarManager = (CalendarManager) springCtx.getBean("calendarManager");
        }
    }

    private void eventValidation(Event event) {
        if(event == null) {
            throw new NullPointerException();
        }
        if(event.getName() == null || "".equals(event.getName() )) {
            throw new IllegalArgumentException("Name of the event cannot be null or empty");
        }
        if(event.getOwner() == null || event.getOwner().getId() == null) {
            throw new IllegalArgumentException("Owner of the event is null or has no ID");
        }
        if(event.getTo() == null || event.getFrom() == null || event.getFrom().after(event.getTo()) ) {
            throw new IllegalArgumentException("Dates for the event are not correct");
        }
        if(event.getPlace() == null) {
            event.setPlace("");
        }
        if(event.getDescription() == null) {
            event.setDescription("");
        }
    }
    
}
