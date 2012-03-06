package cz.muni.fi.pv.projekt;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: xjanco
 * Date: 28.2.2012
 * Time: 8:12:41
 * To change this template use File | Settings | File Templates.
 */
public interface UserManager {

    public void createUser(User usr);
    public void deleteUser(User usr);
    public void updateUser(User usr);
    public User selectUserById(Long id);
    public User selectUserByNick(String nick);
    public List<User> selectAllUsers();

}
