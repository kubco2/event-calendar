package cz.muni.fi.pv.projekt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 *
 * @author zkrejcov
 */
public class TestWrapper {
    
    protected static ApplicationContext springCtx;
    protected static CalendarManagerImpl calendarManager;
    protected static EventManagerImpl eventManager;
    protected static UserManagerImpl userManager;
    
    private JdbcTemplate jdbc;
    private String createCal = 
            "CREATE TABLE calendar ("
            + "    eventId INT UNSIGNED NOT NULL ,"
            + "    userId INT UNSIGNED NOT NULL ,"
            + "    CONSTRAINT id PRIMARY KEY (eventId,userId)"
            + ") TYPE=innoDB";
    private String createUsr = 
            "CREATE TABLE users ("
            + "    id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY ,"
            + "    name VARCHAR(100) NOT NULL ,"
            + "    nick VARCHAR(50) NOT NULL UNIQUE ,"
            + "    password VARCHAR(60) NOT NULL"
            + ") TYPE=innoDB";
    private String createEvt = 
            "CREATE TABLE events ("
            + "    id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY ,"
            + "    name VARCHAR(100) NOT NULL ,"
            + "    owner INT UNSIGNED NOT NULL ,"
            + "    place VARCHAR(100) NOT NULL ,"
            + "    description TEXT NOT NULL ,"
            + "    timeFrom DATETIME NOT NULL ,"
            + "    timeTo DATETIME NOT NULL ,"
            + "    shared TINYINT(1) NOT NULL) TYPE=innoDB";
    private String drop = "DROP TABLE ";
    private String dropCal = drop+"calendar";
    private String dropUsr = drop+"users";
    private String dropEvt = drop+"events";
    
    
    
    @BeforeClass
    public static void setUpSuite() {
        springCtx = new ClassPathXmlApplicationContext("spring-context.xml");
        userManager = (UserManagerImpl) springCtx.getBean("userManager");
        calendarManager = (CalendarManagerImpl) springCtx.getBean("calendarManager");
        eventManager = (EventManagerImpl) springCtx.getBean("eventManager");
    }
    
    @BeforeClass
    public void setUp() {
        cleanDB();
        createTables();
    }
    
    @AfterClass
    public static void tearDown() {
    }
    
    

    private void cleanDB() {
        jdbc.execute(dropCal);
        jdbc.execute(dropEvt);
        jdbc.execute(dropUsr);
    }
    
    private void createTables() {
        jdbc.execute(createCal);
        jdbc.execute(createEvt);
        jdbc.execute(createUsr);
    }

    
    
    @Resource
    public void setDataSource(DataSource dataSource) {
        jdbc = new JdbcTemplate(dataSource);
    }
}
