package cz.muni.fi.pv.projekt;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: Shifty
 * Date: 12.3.2012
 * Time: 15:05
 */
public class UserManagerImplTest  extends TestWrapper {
    
    @Test
    public void testCreateNullUser() {
        User usr = null;
        try {
            userManager.createUser(usr);
            fail("Null user was created");
        } catch (NullPointerException ex) {
            // OK
        }
    }
    
    @Test
    public void testCreateUserNullAttr() {
        User usr = new User();
        try {
            userManager.createUser(usr);
            fail("User with null attributes was created.");
        } catch (IllegalArgumentException ex) {}
    }

    @Test
    public void testCreateCorrectUser() {
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
    public void testUpdateNullUser() {
        User usr = null;
        try {
            userManager.updateUser(usr);
            fail("Null user was updated");
        } catch (NullPointerException ex) {
            // OK
        }
    }

    @Test
    public void testUpdateNullId() {
        User usr = getNewUser();
        try {
            userManager.updateUser(usr);
            fail("User with null ID was updated.");
        } catch (IllegalArgumentException ex) {}
    }
    
    @Test
    public void testUpdateUser() {
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
    public void testDeleteNullUser() {
        User usr = null;
        try {
            userManager.deleteUser(usr);
            fail("Null user was deleted");
        } catch (NullPointerException ex) {
            // OK
        }
    }
    
    @Test
    public void testDeleteNullId() {
        User usr = getNewUser();
        try {
            userManager.updateUser(usr);
            fail("User with null ID was updated.");
        } catch (IllegalArgumentException ex) {}
    }

    @Test
    public void testDeleteCorrectUser() {
        User usr = userTemplate("User name 1","pass1");
        userManager.createUser(usr);
        User usr1 = userManager.selectUserById(usr.getId());
        assertNotNull(usr1);
        userManager.deleteUser(usr);
        usr1 = userManager.selectUserById(usr.getId());
        assertNull("Manager didn't delete user", usr1);
    }

    @Test
    public void testSelectUserById() {
        User usr1 = userTemplate("User name 1","pass1");
        User usr2 = userTemplate("User name 2","pass2");
        userManager.createUser(usr1);
        userManager.createUser(usr2);
        User[] users = {usr1,usr2};
        User[] usersGot = {userManager.selectUserById(usr1.getId()),
                userManager.selectUserById(usr2.getId())};
        assertArrayEquals("SelectUserById method does not work correct",users,usersGot);
    }
    
    @Test
    public void testSelectUserNullId() {
        try {
            userManager.selectUserById(null);
            fail("Selected a user with null ID.");
        } catch (NullPointerException e) {}
    }
    
    @Test
    public void testSelectUserByNick() {
        User usr = getNewUser();
        userManager.createUser(usr);
        User usr2 = userManager.selectUserByNick(usr.getNick());
        assertEqualsLong(usr.getId(), usr2.getId());
        assertEqualsString(usr.getNick(), usr2.getNick());
    }
    
    @Test
    public void testSelectUserNullNick() {
        try {
            userManager.selectUserByNick(null);
            fail("Selected a user with null nick.");
        } catch (NullPointerException e) {}
    }

    private User userTemplate(String name, String pass) {
        User user = new User();
        user.setName(name);
        user.setNick(generateString());
        user.setPassword(pass);
        return user;
    }
}
