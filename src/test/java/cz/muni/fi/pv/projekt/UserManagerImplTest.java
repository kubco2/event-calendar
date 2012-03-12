package cz.muni.fi.pv.projekt;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: Shifty
 * Date: 12.3.2012
 * Time: 15:05
 */
public class UserManagerImplTest {

    private UserManager userManager;

    @Before
    public void setUp() throws Exception {
        userManager = new UserManagerImpl();
    }

    @Test
    public void createNullUser() throws Exception {
        User usr = null;
        try {
            userManager.createUser(usr);
            fail("Null user was created");
        } catch (NullPointerException ex) {
            // OK
        }
    }

    @Test
    public void createCorrectUser() throws Exception {
        User usr1 = userTemplate(null, "Jakub", "kubco", "mypass");
        userManager.createUser(usr1);
        User usr2 = userManager.selectUserByNick(usr1.getNick());
        assertTrue("User was not created",usr2 != null);
        assertTrue("Created user got null id",usr2.getId() == null);
        assertEquals("Created user got bad name",usr1.getName(),usr2.getName());
        assertEquals("Created user got bad nick",usr1.getNick(),usr2.getNick());
        assertEquals("Created user got bad password",usr1.getPassword(),usr2.getPassword());
    }

    @Test
    public void updateUser() throws Exception {
        Long id = (long)1;
        User usr1 = userTemplate(id, "Jakub", "kubco", "mypass");
        userManager.createUser(usr1);
        User usr2 = userTemplate(id, "Jakub1", "kubco1", "mypass1");
        userManager.updateUser(usr2);
        User usr3 = userManager.selectUserById(id);
        Object[] usr2Props = {usr2.getName(),usr2.getNick(),usr2.getPassword()};
        Object[] usr3Props = {usr3.getName(),usr3.getNick(),usr3.getPassword()};
        assertArrayEquals("Created user was not updated",usr2Props,usr3Props);
    }

    @Test
    public void selectUserById() throws Exception {
        Long id1 = (long)1;
        Long id2 = (long)2;
        Long id3 = (long)3;
        Long id4 = (long)4;
        User usr1 = userTemplate(id1,"User name 1","nick1","pass1");
        User usr2 = userTemplate(id2,"User name 2","nick2","pass2");
        User usr3 = userTemplate(id3,"User name 3","nick3","pass3");
        User usr4 = userTemplate(id4,"User name 4","nick4","pass4");
        userManager.createUser(usr1);
        userManager.createUser(usr2);
        userManager.createUser(usr3);
        userManager.createUser(usr4);
        User[] users = {usr3,usr1,usr4,usr2};
        User[] usersGot = {userManager.selectUserById(id3),userManager.selectUserById(id1),
                userManager.selectUserById(id4),userManager.selectUserById(id2)};
        assertArrayEquals("SelectById method does not work correct",users,usersGot);
    }

    private User userTemplate(Long id, String name, String nick, String pass) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setNick(nick);
        user.setPassword(pass);
        return user;
    }
}
