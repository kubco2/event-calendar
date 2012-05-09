package cz.muni.fi.pv.projekt.gui;

import cz.muni.fi.pv.projekt.CalendarManager;
import cz.muni.fi.pv.projekt.Event;
import cz.muni.fi.pv.projekt.User;
import cz.muni.fi.pv.projekt.UserManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 *
 * @author Zuzana Krejcova
 */
public class CalendarMainView extends JFrame {

    static User currentUser = null;
    static List<Event> events = null;
    static {
        ///feed user with test user
        ApplicationContext springCtx = new ClassPathXmlApplicationContext("spring-context.xml");
        UserManager userManager = (UserManager) springCtx.getBean("userManager");
        currentUser = userManager.selectUserById(1L);
        //feed event list
        CalendarManager calendarManager = (CalendarManager) springCtx.getBean("calendarManager");
        events = calendarManager.getEventsForUser(CalendarMainView.currentUser);
    }

    // JPanels to be used in the main tabbed pane
    // all should be filled out in the respective classes
    private ProfileManagement profileManagement;
    private SharedCalendar sharedCal;
    private PersonalCalendar personalCal;

    // main tabbed pane containing calendar and profile panels
    private JTabbedPane mainPanel;

    /* Apart from the above, we could do a menu with buttons/items/options
* that would do the same as clicking the tabs on the tabbed pane mainPanel.
* In the menu, there could also be some New event and Quit options,
* perhaps even shortcuts to deleting user or other some such.
*/

    public CalendarMainView() {
        init();
    }

    public CalendarMainView(User user) {
        currentUser = user;
        init();
    }

    private void init() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
            // TODO
            @Override
            public void actionPerformed(ActionEvent e) {
                // nothing yet, to be implemented
                // try to create a new event, show a message in case of failure
                try {
                    throw new Exception("TEST - NOT IMPLEMENTED");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,"Could not open the event "
                            + "creation dialogue, exception caught: \n" + ex.getMessage(),
                            "Can't create event!", JOptionPane.ERROR_MESSAGE);
                }
                return;
            };
        };
        newEventItem.addActionListener(newEventListener);

        ActionListener quitListener = new ActionListener() {
            // TODO
            @Override
            public void actionPerformed(ActionEvent e) {
                // nothing yet, to be implemented
                // try to quit, show a message in case of failure
                try {
                    System.exit(0);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,"Could not quit the application, "
                            + "exception caught: \n" + ex.getMessage(),
                            "Can't quit!", JOptionPane.ERROR_MESSAGE);
                }
                return;
            };
        };
        quitItem.addActionListener(quitListener);

        ActionListener logoutListener = new ActionListener() {
            // TODO
            @Override
            public void actionPerformed(ActionEvent e) {
                // nothing yet, to be implemented
                // try to log out, show a message in case of failure
                try {
                    throw new Exception("TEST - NOT IMPLEMENTED");
                    // but we already know we will need to show the login screen
// setVisible(false);
// new Login().setVisible(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,"Could not quit the application, "
                            + "exception caught: \n" + ex.getMessage(),
                            "Can't quit!", JOptionPane.ERROR_MESSAGE);
                }
                return;
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
        mainPanel.addTab("Shared Calendar", sharedCal);
        mainPanel.addTab("Profile Management", profileManagement);

        // other various stuff. like username label.
        JLabel userNameLabel = new JLabel();
        String userName = "Logged in as : ";
        userName += (currentUser == null)? "GUEST" : currentUser.getName();
        userNameLabel.setText(userName);
        userNameLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        userNameLabel.setBorder(new EmptyBorder(5, 5, 5, 5));

        // add all immediate children components to this frame
        setJMenuBar(menuBar);
        rootPanel.add(userNameLabel);
        rootPanel.add(mainPanel);
        add(rootPanel);

        pack();
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CalendarMainView().setVisible(true);
            }
        });
    }
}
