package cz.muni.fi.pv.projekt;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Zuzana Krejcova
 */
@Repository("eventManager")
public class EventManagerImpl implements EventManager {

    final static Logger log = LoggerFactory.getLogger(EventManagerImpl.class);

    private JdbcTemplate jdbc;
    private SimpleJdbcInsert insertEvent;

    private static UserManager userManager = new UserManagerImpl();

    @Resource
    public void setDataSource(DataSource dataSource) {
        jdbc = new JdbcTemplate(dataSource);
        insertEvent = new SimpleJdbcInsert(dataSource)
                .withTableName("events")
                .usingColumns("name", "owner", "place", "description", "from", "to", "shared")
                .usingGeneratedKeyColumns("id");
    }

    private static final ParameterizedRowMapper<Event> EVENT_MAPPER = new ParameterizedRowMapper<Event>() {
        @Override
        public Event mapRow(ResultSet rs, int i) throws SQLException {
            Event event = new Event();
            event.setId(rs.getLong("id"));
            event.setName(rs.getString("name"));
            event.setDescription(rs.getString("description"));
            event.setFrom(rs.getDate("from"));
            event.setTo(rs.getDate("to"));
            User owner = userManager.selectUserById(rs.getLong("owner"));
            event.setOwner(owner);
            event.setPlace(rs.getString("place"));
            event.setShared(rs.getBoolean("shared"));
            return event;
        }
    };

    @Override
    public void createEvent(Event evt) {
        log.debug("createEvent({})", evt);
        if (evt == null) throw new NullPointerException();
        if (nullOrEmpty(evt.getName()) || nullOrEmpty(evt.getPlace()) || nullOrEmpty(evt.getDescription())
                || null == evt.getFrom() || null == evt.getTo() || null == evt.getOwner()) {
            throw new IllegalArgumentException("Some attribute of this event is NULL.");
        }
        evt.setId(insertEvent.executeAndReturnKey(new BeanPropertySqlParameterSource(evt)).longValue());
    }

    @Override
    public void deleteEvent(Event evt) {
        log.debug("deleteEvent({})", evt);
        if (evt == null) throw new NullPointerException();
        if (evt.getId() == null) throw new IllegalArgumentException("The event does not have an ID yet.");
        jdbc.update("DELETE FROM events WHERE id=?", evt.getId());
    }

    @Override
    public void updateEvent(Event evt) {
        log.debug("updateEvent({})", evt);
        if (evt == null) throw new NullPointerException();
        if (nullOrEmpty(evt.getName()) || nullOrEmpty(evt.getPlace()) || nullOrEmpty(evt.getDescription())
                || null == evt.getFrom() || null == evt.getTo() || null == evt.getOwner()) {
            throw new IllegalArgumentException("Some attribute of this event is NULL.");
        }
        if (evt.getId() == null) throw new IllegalArgumentException("The user does not have an ID yet.");
        jdbc.update("UPDATE events SET name=?,place=?,description=?,from=?,to=?,owner=?,shared=? WHERE id=?",
                evt.getName(), evt.getPlace(), evt.getDescription(), evt.getFrom(),
                evt.getTo(), evt.getOwner(), evt.isShared(), evt.getId());
    }

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

    private boolean nullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
