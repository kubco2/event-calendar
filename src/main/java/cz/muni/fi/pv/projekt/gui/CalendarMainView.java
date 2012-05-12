package cz.muni.fi.pv.projekt.gui;

import cz.muni.fi.pv.projekt.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Zuzana Krejcova
 */
public class CalendarMainView extends JFrame {

    static User currentUser = null;
    static List<Event> privateEvents = null;
    static List<Event> sharedEvents = null;

    // JPanels to be used in the main tabbed pane
    // all should be filled out in the respective classes
    private ProfileManagement profileManagement;
    private SharedCalendar sharedCal;
    private PersonalCalendar personalCal;

    // main tabbed pane containing calendar and profile panels
    private JTabbedPane mainPanel;
    
    private String userName = "Logged in as : %s";
    private JLabel userNameLabel;

    public static CalendarMainView instance;

    /* Apart from the above, we could do a menu with buttons/items/options
     * that would do the same as clicking the tabs on the tabbed pane mainPanel.
     * In the menu, there could also be some New event and Quit options,
     * perhaps even shortcuts to deleting user or other some such.
     */

    private CalendarMainView(User user) {
        currentUser = user;
        ApplicationContext springCtx = new ClassPathXmlApplicationContext("spring-context.xml");
        CalendarManager calendarManager = (CalendarManager) springCtx.getBean("calendarManager");
        EventManager eventManager = (EventManager) springCtx.getBean("eventManager");
        privateEvents = calendarManager.getEventsForUser(currentUser);
        sharedEvents = eventManager.selectSharedEvents();
        init();
    }

    public static CalendarMainView getCalendarMainView(User user) {
        instance = new CalendarMainView(user);
        return instance;
    }

    private void init() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Calendar");

        // root panel
        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));

        // menu stuff
        JMenuBar menuBar = new JMenuBar();
        JMenu mainMenu = new JMenu("Calendar");
        JMenuItem newEventItem = new JMenuItem("New Event");
        JMenuItem quitItem = new JMenuItem("Quit");
        JMenuItem logoutItem = new JMenuItem("Log Out");
        newEventItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
        logoutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));

        ActionListener newEventListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // try to create a new event, show a message in case of failure
                try {
                    new EventView2(currentUser, null).setVisible(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,"Could not open the event "
                        + "creation dialogue, exception caught: \n" + ex.getMessage(),
                        "Can't create event!", JOptionPane.ERROR_MESSAGE);
                    LoggerFactory.getLogger(CalendarMainView.class).error("Could not open the event "
                        + "creation dialogue, exception caught: \n", ex);
                }
            };
        };
        newEventItem.addActionListener(newEventListener);

        ActionListener quitListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // try to quit, show a message in case of failure
                try {
                    System.exit(0);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,"Could not quit the application, "
                        + "exception caught: \n" + ex.getMessage(),
                        "Can't quit!", JOptionPane.ERROR_MESSAGE);
                    LoggerFactory.getLogger(CalendarMainView.class).error("Could not quit the application, "
                        + "exception caught: \n", ex);
                }
            };
        };
        quitItem.addActionListener(quitListener);

        ActionListener logoutListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // try to log out, show a message in case of failure
                logOut();
            };
        };
        logoutItem.addActionListener(logoutListener);

        mainMenu.add(newEventItem);
        mainMenu.add(quitItem);
        mainMenu.add(logoutItem);
        menuBar.add(mainMenu);

        // panels stuff
        mainPanel = new JTabbedPane();
        mainPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        profileManagement = new ProfileManagement();
        sharedCal = new SharedCalendar();
        personalCal = new PersonalCalendar();

        mainPanel.addTab("Personal Calendar", personalCal);
        mainPanel.addTab("Public Calendar", sharedCal);
        mainPanel.addTab("Profile Management", profileManagement);

        // other various stuff. like username label.
        userNameLabel = new JLabel();
        String name = (currentUser == null)? "GUEST" : currentUser.getName();
        updateUser(name);
        userNameLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        userNameLabel.setBorder(new EmptyBorder(5, 5, 5, 5));

        // add all immediate children components to this frame
        setJMenuBar(menuBar);
        rootPanel.add(userNameLabel);
        rootPanel.add(mainPanel);
        add(rootPanel);

        pack();
    }

    public void logOut() {
        try {
            setVisible(false);
            currentUser = null;
            Login.getInstance().setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,"Could not quit the application, "
                + "exception caught: \n" + ex.getMessage(),
                "Can't quit!", JOptionPane.ERROR_MESSAGE);
            LoggerFactory.getLogger(CalendarMainView.class).error("Could not quit the application, "
                + "exception caught: \n", ex);
        }
    }
    
    public void updateUser(String user) {
        userNameLabel.setText(String.format(userName, user));
    }
}
