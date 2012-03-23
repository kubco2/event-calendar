package cz.muni.fi.pv.projekt;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Random;

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
        //userManager = new UserManagerImpl();
        ApplicationContext springCtx = new ClassPathXmlApplicationContext("spring-context.xml");
        userManager = (UserManagerImpl) springCtx.getBean("userManager");
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
        User usr1 = userTemplate("Jakub", "mypass");
        userManager.createUser(usr1);
        assertNotNull("createUser doesn't assign ID",usr1.getId());

        User usr2 = userManager.selectUserById(usr1.getId());
        assertTrue("User was not created",usr2 != null);
        assertEquals("Created user got bad name",usr1.getName(),usr2.getName());
        assertEquals("Created user got bad nick",usr1.getNick(),usr2.getNick());
        assertEquals("Created user got bad password",usr1.getPassword(),usr2.getPassword());
    }

    @Test
    public void updateNullUser() throws Exception {
        User usr = null;
        try {
            userManager.updateUser(usr);
            fail("Null user was updated");
        } catch (NullPointerException ex) {
            // OK
        }
    }

    @Test
    public void updateUser() throws Exception {
        User usr1 = userTemplate("Jakub", "mypass");
        userManager.createUser(usr1);
        usr1.setName(generateString());
        usr1.setPassword("mypass1");
        userManager.updateUser(usr1);
        User usr2 = userManager.selectUserById(usr1.getId());
        assertEquals("Updated user got bad name",usr1.getName(),usr2.getName());
        assertEquals("Updated user got bad password",usr1.getPassword(),usr2.getPassword());
    }

    @Test
    public void deleteNullUser() throws Exception {
        User usr = null;
        try {
            userManager.deleteUser(usr);
            fail("Null user was deleted");
        } catch (NullPointerException ex) {
            // OK
        }
    }

    @Test
    public void deleteUser() throws Exception {
        User usr = userTemplate("User name 1","pass1");
        userManager.createUser(usr);
        User usr1 = userManager.selectUserById(usr.getId());
        assertNotNull(usr1);
        userManager.deleteUser(usr);
        usr1 = userManager.selectUserById(usr.getId());
        assertNull("Manager didn't delete user", usr1);
    }

    @Test
    public void selectUserById() throws Exception {
        User usr1 = userTemplate("User name 1","pass1");
        User usr2 = userTemplate("User name 2","pass2");
        userManager.createUser(usr1);
        userManager.createUser(usr2);
        User[] users = {usr1,usr2};
        User[] usersGot = {userManager.selectUserById(usr1.getId()),
                userManager.selectUserById(usr2.getId())};
        assertArrayEquals("SelectUserById method does not work correct",users,usersGot);
    }

    private User userTemplate(String name, String pass) {
        User user = new User();
        user.setName(name);
        user.setNick(generateString());
        user.setPassword(pass);
        return user;
    }

    private String generateString() {
        Random rng = new Random();
        String characters="randomtextofsomeint";
        int length = 10;
        char[] text = new char[length];
        for (int i = 0; i < length; i++)
        {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }

}
