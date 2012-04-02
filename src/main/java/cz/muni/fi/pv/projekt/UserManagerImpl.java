package cz.muni.fi.pv.projekt;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Zuzana Krejcova
 */

@Repository("userManager")
public class UserManagerImpl implements UserManager {

    private final static Logger log = LoggerFactory.getLogger(UserManagerImpl.class);
    private JdbcTemplate jdbc;
    private SimpleJdbcInsert insertUser;

    @Resource
    public void setDataSource(DataSource dataSource) {
        jdbc = new JdbcTemplate(dataSource);
        insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingColumns("name", "nick", "password")
                .usingGeneratedKeyColumns("id");
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

    @Override
    public void createUser(User usr) {
        log.debug("createUser({})",usr);
        userValidation(usr);

        Map<String,Object> userMap = new HashMap<String,Object>();
        userMap.put("name",usr.getName());
        userMap.put("nick",usr.getNick());
        userMap.put("password", usr.getPassword());
        usr.setId(insertUser.executeAndReturnKey(userMap).longValue());
    }

    @Override
    public void deleteUser(User usr) {
        log.debug("deleteUser({})",usr);
        userValidation(usr);
        if (usr.getId()==null) {
            throw new IllegalArgumentException("The user does not have an ID yet.");
        }

        jdbc.update("DELETE FROM users WHERE id=?",
                    usr.getId());
    }

    @Override
    public void updateUser(User usr) {
        log.debug("updateUser({})",usr);
        userValidation(usr);
        if (usr.getId()==null) {
            throw new IllegalArgumentException("The user does not have an ID yet.");
        }

        jdbc.update("UPDATE users SET name=?,password=? WHERE id=?",
                    usr.getName(),usr.getPassword(),usr.getId());
    }

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

    @Override
    public List<User> selectAllUsers() {
        log.debug("selectAllUsers()");
        return jdbc.query("SELECT * FROM users",
                           USER_MAPPER);
    }
    
    private boolean nullOrEmpty(String str) {
        return str == null || "".equals(str);
    }
    
    private void userValidation(User user) {
        if(user == null) {
            throw new NullPointerException();
        }
        if( nullOrEmpty(user.getNick()) || nullOrEmpty(user.getName()) || nullOrEmpty(user.getPassword()) ) {
            throw new IllegalArgumentException("User cannot have null/empty attributes");
        }
    }
}
