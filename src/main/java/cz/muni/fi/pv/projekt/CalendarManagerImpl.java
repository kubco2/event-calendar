package cz.muni.fi.pv.projekt;

import java.util.List;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 *
 * @author zkrejcov
 */
@Repository("calendarManager")
public class CalendarManagerImpl implements CalendarManager {

    final static Logger log = LoggerFactory.getLogger(CalendarManagerImpl.class);

    private JdbcTemplate jdbc;
    private SimpleJdbcInsert insertUserToEvent;

    private static UserManager userManager = new UserManagerImpl();

    @Resource
    public void setDataSource(DataSource dataSource) {
        jdbc = new JdbcTemplate(dataSource);
        insertUserToEvent = new SimpleJdbcInsert(dataSource)
                .withTableName("calendar")
                .usingColumns("eventId", "userId");
    }

    private static final ParameterizedRowMapper<User> USER_MAPPER = new ParameterizedRowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int i) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setNick(rs.getString("nick"));
            user.setPassword(rs.getString("password"));
            return user;
        }
    };

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

    /**
     * Adds the user to the event.
     * @param usr
     * @param evt
     * @throws NullPointerException if any of the parameters is null.
     * @throws IllegalArgumentException if the user or the event don't have an id.
     */
    @Override
    public void saveUserEvent(User usr, Event evt) {
        log.debug("saveUserEvent({}, {})", usr, evt);
        if (usr == null) throw new NullPointerException("The user is NULL.");
        if (evt == null) throw new NullPointerException("The event is NULL.");
        if (usr.getId() == null) throw new IllegalArgumentException("The user does not have an ID yet.");
        if (evt.getId() == null) throw new IllegalArgumentException("The event does not have an ID yet.");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("eventId", evt.getId());
        map.put("userId", usr.getId());
        insertUserToEvent.execute(map);
    }

    /**
     * Revokes user's participation in the event.
     * @param usr
     * @param evt
     * @throws NullPointerException if any of the parameters is null.
     * @throws IllegalArgumentException if the user or the event don't have an id.
     */
    @Override
    public void deleteUserFromEvent(User usr, Event evt) {
        log.debug("deleteUserFromEvent({}, {})", usr, evt);
        if (usr == null) throw new NullPointerException("The user is NULL.");
        if (evt == null) throw new NullPointerException("The event is NULL.");
        if (usr.getId() == null) throw new IllegalArgumentException("The user does not have an ID yet.");
        if (evt.getId() == null) throw new IllegalArgumentException("The event does not have an ID yet.");
        jdbc.update("DELETE FROM calendar WHERE eventId=? AND userId=?",
                evt.getId(), usr.getId());
    }

    /**
     * Lists all the events this user joined/created.
     * @param usr
     * @return List of events this user joined/created.
     * @throws NullPointerException if the user is null.
     * @throws IllegalArgumentException if the user doesn't have an id.
     */
    @Override
    public List<Event> getEventsForUser(User usr) {
        log.debug("getEventsForUser({})", usr);
        if (usr == null) throw new NullPointerException("The user is NULL.");
        if (usr.getId() == null) throw new IllegalArgumentException("The user does not have an ID yet.");
        try {
            return jdbc.query("SELECT * FROM calendar WHERE userId=?", EVENT_MAPPER, usr.getId());
        } catch(IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    /**
     * Lists all the users participating in this event.
     * @param usr
     * @return List of users for this event.
     * @throws NullPointerException if the event is null.
     * @throws IllegalArgumentException if the event doesn't have an id.
     */
    @Override
    public List<User> getUsersForEvent(Event evt) {
        log.debug("getUsersForEvent({})", evt);
        if (evt == null) throw new NullPointerException("The event is NULL.");
        if (evt.getId() == null) throw new IllegalArgumentException("The event does not have an ID yet.");
        try {
            return jdbc.query("SELECT * FROM calendar WHERE eventId=?", USER_MAPPER, evt.getId());
        } catch(IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }
}
