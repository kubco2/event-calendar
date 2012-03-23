package cz.muni.fi.pv.projekt;

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
import java.util.List;

/**
 *
 * @author Zuzana Krejcova
 */

@Repository("userManager")
public class UserManagerImpl implements UserManager {

    final static Logger log = LoggerFactory.getLogger(UserManagerImpl.class);

    private JdbcTemplate jdbc;
    private SimpleJdbcInsert insertUser;

    @Resource
    public void setDataSource(DataSource dataSource) {
        this.jdbc = new JdbcTemplate(dataSource);
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingColumns("name", "nick", "password")
                .usingGeneratedKeyColumns("id");
    }

    private static final ParameterizedRowMapper<User> USER_MAPPER = new ParameterizedRowMapper<User>() {
        public User mapRow(ResultSet rs, int i) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setNick(rs.getString("nick"));
            user.setPassword(rs.getString("password"));
            return user;
        }
    };

    /**
     * Insert user to database. When user is inserted, he get ID
     * @param usr User to create
     * @throws NullPointerException if user is NULL
     * @throws IllegalArgumentException if some of user properties are NULL or empty, except ID
     */
    @Override
    public void createUser(User usr) {
        log.debug("createUser({})",usr);
        if(usr==null) {
            throw new NullPointerException();
        }
        if(nullOrEmpty(usr.getName()) || nullOrEmpty(usr.getNick()) || nullOrEmpty(usr.getPassword())) {
            throw new IllegalArgumentException();
        }
        usr.setId(insertUser.executeAndReturnKey(new BeanPropertySqlParameterSource(usr)).longValue());
    }

    /**
     * Delete user from database
     * @param usr User for delete
     * @throws NullPointerException if user parameter is null
     */
    @Override
    public void deleteUser(User usr) {
        log.debug("deleteUser({})",usr);
        if(usr==null) {
            throw new NullPointerException();
        }
        jdbc.update("DELETE FROM users WHERE id=?",
                usr.getId());
    }

    /**
     * Update name or password of user
     * @param usr User for update
     * @throws NullPointerException if param is null
     * @throws IllegalArgumentException if properties name,password are null or empty
     */
    @Override
    public void updateUser(User usr) {
        log.debug("updateUser({})",usr);
        if(usr==null) {
            throw new NullPointerException();
        }
        if(nullOrEmpty(usr.getName()) || nullOrEmpty(usr.getPassword())) {
            throw new IllegalArgumentException();
        }
        jdbc.update("UPDATE users SET name=?,password=? WHERE id=?",
                    usr.getName(),usr.getPassword(),usr.getId());
    }

    /**
     * Select user by Id from database
     * @param id user's id
     * @return User if user with this id is in database, otherwise NULL 
     * @throws NullPointerException if parameter is null
     */
    @Override
    public User selectUserById(Long id) {
        log.debug("selectUserById({})",id);
        if(id==null) {
            throw new NullPointerException();
        }
        try {
            return jdbc.queryForObject("SELECT * FROM users WHERE id=?",
                                        USER_MAPPER,id);
        } catch(IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    /**
     * Select user by nickname from database
     * @param nick user's nickname
     * @return User if user with this nickname is in database, otherwise NULL
     * @throws NullPointerException if nick is null
     */
    @Override
    public User selectUserByNick(String nick) {
        log.debug("selectUserByNick({})",nick);
        if(nick==null) {
            throw new NullPointerException();
        }
        try {
            return jdbc.queryForObject("SELECT * FROM users WHERE nick=?",
                                        USER_MAPPER,nick);
        } catch(IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    /**
     * select all user in database
     * @return list of users
     */
    @Override
    public List<User> selectAllUsers() {
        log.debug("selectAllUsers()");
        return jdbc.query("SELECT * FROM users",
                           USER_MAPPER);
    }
    
    private boolean nullOrEmpty(String str) {
        return str == null || "".equals(str);
    }
}
