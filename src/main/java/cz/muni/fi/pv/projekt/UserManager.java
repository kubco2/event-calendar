package cz.muni.fi.pv.projekt;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: xjanco
 * Date: 28.2.2012
 * Time: 8:12:41
 */
public interface UserManager {

    /**
     * Insert user to database. When user is inserted, he get ID
     * @param usr User to create
     * @throws NullPointerException if user is NULL
     * @throws IllegalArgumentException if some of user properties are NULL or empty, except ID
     */
    public void createUser(User usr);

    /**
     * Delete user from database
     * @param usr User for delete
     * @throws NullPointerException if user is NULL
     * @throws IllegalArgumentException if some of user properties are NULL or empty
     */
    public void deleteUser(User usr);

    /**
     * Update name or password of user
     * @param usr User for update
     * @throws NullPointerException if user is NULL
     * @throws IllegalArgumentException if some of user properties are NULL or empty
     */
    public void updateUser(User usr);

    /**
     * Select user by Id from database
     * @param id user's id
     * @return User if user with this ID is in database, otherwise NULL
     * @throws NullPointerException if parameter is null
     */
    public User selectUserById(Long id);

    /**
     * Select user by nickname from database
     * @param nick user's nickname
     * @return User if user with this nickname is in database, otherwise NULL
     * @throws NullPointerException if nick is null
     */
    public User selectUserByNick(String nick);

    /**
     * select all user in database
     * @return list of users
     */
    public List<User> selectAllUsers();

}
