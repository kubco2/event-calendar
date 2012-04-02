package cz.muni.fi.pv.projekt;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.Random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author zkrejcov
 */
public class TestWrapper {
    
    protected final static Logger logger = LoggerFactory.getLogger(TestWrapper.class);
    
    private static String createCal = 
            "CREATE TABLE calendar ("
            + "    eventId INT UNSIGNED NOT NULL ,"
            + "    userId INT UNSIGNED NOT NULL ,"
            + "    CONSTRAINT id PRIMARY KEY (eventId,userId),"
            + "    FOREIGN KEY (eventId) REFERENCES events(id) ON DELETE CASCADE ON UPDATE NO ACTION,"
            + "    FOREIGN KEY (userId) REFERENCES users(id) ON DELETE CASCADE ON UPDATE NO ACTION"
            + ") TYPE=innoDB";
    private static String createUsr = 
            "CREATE TABLE users ("
            + "    id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY ,"
            + "    name VARCHAR(100) NOT NULL ,"
            + "    nick VARCHAR(50) NOT NULL UNIQUE ,"
            + "    password VARCHAR(60) NOT NULL"
            + ") TYPE=innoDB";
    private static String createEvt = 
            "CREATE TABLE events ("
            + "    id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY ,"
            + "    name VARCHAR(100) NOT NULL ,"
            + "    owner INT UNSIGNED NOT NULL ,"
            + "    place VARCHAR(100) NOT NULL ,"
            + "    description TEXT NOT NULL ,"
            + "    timeFrom DATETIME NOT NULL ,"
            + "    timeTo DATETIME NOT NULL ,"
            + "    shared TINYINT(1) NOT NULL,"
            + "    INDEX owner_id (owner),"
            + "    FOREIGN KEY (owner) REFERENCES users(id) ON DELETE CASCADE ON UPDATE NO ACTION"
            + ") TYPE=innoDB";
    private static final String drop = "DROP TABLE ";
    private static String dropCal = drop+"calendar";
    private static String dropUsr = drop+"users";
    private static String dropEvt = drop+"events";
    
    protected static ApplicationContext springCtx;
    protected static CalendarManagerImpl calendarManager;
    protected static EventManager eventManager;
    protected static UserManagerImpl userManager;
    private static DBHelper dbh;
    protected static JdbcTemplate jdbc;
    
    @BeforeClass
    public static void setUp() {
        logger.info("----- Setting up Test Class -----");
        springCtx = new ClassPathXmlApplicationContext("spring-context.xml");
        dbh = (DBHelper)springCtx.getBean("dbh");
        userManager = (UserManagerImpl) springCtx.getBean("userManager");
        calendarManager = (CalendarManagerImpl) springCtx.getBean("calendarManager");
        eventManager = (EventManager) springCtx.getBean("eventManager");
        jdbc = (dbh).getJdbc();
        userManager.setDataSource(dbh.getDS());
        //eventManager.setDataSource(dbh.getDS());
        calendarManager.setDataSource(dbh.getDS());
        recreateTables();
    }
    
    @AfterClass
    public static void tearDown() {
    }
    
    protected static void recreateTables() {
        cleanDB();
        createTables();
    }

    private static void cleanDB() {
        logger.info("... Cleaning the DataBase ...");
        jdbc.execute(dropCal);
        jdbc.execute(dropEvt);
        jdbc.execute(dropUsr);
    }
    
    private static  void createTables() {
        logger.info("... Recreating the tables ...");
        jdbc.execute(createUsr);
        jdbc.execute(createEvt);
        jdbc.execute(createCal);
    }
    
    protected static User getNewUser() {
        User usr = new User();
        usr.setName(generateString());
        usr.setNick(generateString());
        usr.setPassword(generateString());
        return usr;
    }
    
    protected static Event getNewEvent(User owner) {
        return getNewEvent(owner, true);
    }
    
    protected static Event getNewEvent(User owner, boolean shared) {
        Event evt = new Event();
        evt.setName(generateString()); 
        evt.setDescription(generateString());
        evt.setFrom(new Date());
        evt.setTo(new Date());
        evt.setPlace(generateString());
        evt.setShared(shared);
        evt.setOwner(owner);
        return evt;
    }
    
    protected static String generateString() {
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
    
    protected void assertEqualsString(String expected, String actual) {
        assertArrayEquals(expected.toCharArray(), actual.toCharArray());
    }
    
    protected void assertEqualsLong(Long expected, Long actual) {
        assertEquals(expected.longValue(), actual.longValue());
    }
}
