package cz.muni.fi.pv.projekt.gui;

import static java.util.Calendar.DAY_OF_MONTH;

import cz.muni.fi.pv.projekt.CalendarManager;
import cz.muni.fi.pv.projekt.Event;
import cz.muni.fi.pv.projekt.EventManager;
import cz.muni.fi.pv.projekt.User;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Zuzana Krejcova
 */
public class EventView2 extends JFrame {

    private User currentUser = null;
    private Event event = null;

    private String owner;
    private String name = "<name the event>";
    private String place = "<place the event somewhere>";
    private String desc = "<describe the event>";
    private Date from = new Date();
    private Date to = new Date();
    private Boolean shared = false;
    private String joinLeaveSave = "Save";

    JTextField ownerField;
    JTextField nameField;
    JTextField placeField;
    JSpinner fromSpinner;
    JSpinner toSpinner;
    JTextArea descArea;
    JCheckBox shareBox;

    private boolean editable = true;

    public EventView2(User user, Event event) {
        super();
        currentUser = user;
        owner = user.getName();
        if (event != null) {
            this.event = event;
            owner = event.getOwner().getName();
            name = event.getName();
            place = event.getPlace();
            desc = event.getDescription();
            from = event.getFrom();
            to = event.getTo();
            shared = event.isShared();
            if (event.getOwner() != user) {
                editable = false;
                if (userJoined()) {
                    joinLeaveSave = "Leave";
                } else {
                    joinLeaveSave = "Join";
                }
            }
        } else this.event = new Event();
        init();
    }

    private void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Event");

        // the root panel(s)
        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
        rootPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JPanel topPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        JPanel descPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        JPanel bottomPanel = new JPanel(new GridLayout(2, 2, 5, 5));

        // the content
        // owner
        JLabel ownerLabel = new JLabel("Event owner");
        ownerLabel.setFont(ownerLabel.getFont().deriveFont(Font.ITALIC));
        ownerField = new JTextField(owner);
        ownerField.setFont(ownerField.getFont().deriveFont(Font.ITALIC));
        ownerField.setEnabled(false);

        // name
        JLabel nameLabel = new JLabel("Event name");
        nameField = new JTextField(name);
        nameField.setEnabled(editable);

        // place
        JLabel placeLabel = new JLabel("Location");
        placeField = new JTextField(place);
        placeField.setEnabled(editable);

        // from
        JLabel fromLabel = new JLabel("Event starts");
        fromSpinner = new JSpinner(new SpinnerDateModel(from, null, null, DAY_OF_MONTH));
        fromSpinner.setEnabled(editable);

        // to
        JLabel toLabel = new JLabel("Event ends");
        toSpinner = new JSpinner(new SpinnerDateModel(to, null, null, DAY_OF_MONTH));
        toSpinner.setEnabled(editable);

        // description
        JLabel descLabel = new JLabel("Description");
        descArea = new JTextArea(desc);
        descArea.setRows(5);
        descArea.setEnabled(editable);
        JScrollPane descScroll = new JScrollPane(descArea);

        // shared
        JLabel shareLabel = new JLabel("Will others see this event?");
        shareBox = new JCheckBox("Is shared event.", shared);
        shareBox.setEnabled(editable);

        // buttons
        JButton deleteButton = new JButton("Delete");
        deleteButton.setEnabled(editable);

        ActionListener deleteListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // try to delete the event, show a message in case of failure
                try {
                    deleteEvent();
                    closeFrame();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,"Could not delete the event, exception caught: \n" +
                        ex.getMessage(), "Deletion unsuccessful!", JOptionPane.ERROR_MESSAGE);
                    LoggerFactory.getLogger(EventView2.class).error(
                        "Could not delete the event, exception caught: \n", ex);
                }
                return;
            };
        };
        deleteButton.addActionListener(deleteListener);

        JButton saveButton = new JButton(joinLeaveSave);

        ActionListener saveListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // try to save the event, show a message in case of failure
                try {
                    if (e.getActionCommand().equals("Save")) {
                        saveEvent();
                    } else if(e.getActionCommand().equals("Join")) {
                        joinEvent();
                        ((JButton)e.getSource()).setText("Leave");
                    } else if (e.getActionCommand().equals("Leave")) {
                        leaveEvent();
                        ((JButton)e.getSource()).setText("Join");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,"Could not "+e.getActionCommand().toLowerCase()
                        + " the event, exception caught: \n" + ex.getMessage(),
                        "Operation unsuccessful!", JOptionPane.ERROR_MESSAGE);
                    LoggerFactory.getLogger(EventView2.class).error(
                        "Could not "+e.getActionCommand().toLowerCase()
                        + " the event, exception caught: \n", ex);
                }
                return;
            };
        };
        saveButton.addActionListener(saveListener);
        
        // add it all into its parent container
        topPanel.add(ownerLabel);
        topPanel.add(ownerField);
        topPanel.add(nameLabel);
        topPanel.add(nameField);
        topPanel.add(placeLabel);
        topPanel.add(placeField);
        topPanel.add(fromLabel);
        topPanel.add(fromSpinner);
        topPanel.add(toLabel);
        topPanel.add(toSpinner);
        descPanel.add(descLabel);
        descPanel.add(descScroll);
        bottomPanel.add(shareLabel);
        bottomPanel.add(shareBox);
        bottomPanel.add(deleteButton);
        bottomPanel.add(saveButton);

        rootPanel.add(topPanel);
        rootPanel.add(descPanel);
        rootPanel.add(bottomPanel);

        add(rootPanel);

        pack();
    }

    private boolean userJoined() {
        UserJoinedQuery ujquery = new UserJoinedQuery();
        ujquery.execute();
        boolean joined = false;
        try {
            joined = ujquery.get().booleanValue();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Failed asserting if user joined the event, "
                + "exception caught: \n" + e.getMessage(), "Operation failed!",
                JOptionPane.ERROR_MESSAGE);
            LoggerFactory.getLogger(EventView2.class).error("Failed asserting if "
                + "user joined the event, exception caught: \n", e);
        }
        return joined;
    }

    private void saveEvent() {
        event.setName(nameField.getText());
        event.setPlace(placeField.getText());
        event.setFrom((Date)fromSpinner.getValue());
        event.setTo((Date)toSpinner.getValue());
        event.setDescription(descArea.getText());
        event.setShared(shareBox.isSelected());
        event.setOwner(currentUser);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (event.getId()==null) getEventManager().createEvent(event);
                else getEventManager().updateEvent(event);
                updateCalendars();
                closeFrame();
            }
        });
    }

    private void deleteEvent() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                getEventManager().deleteEvent(event);
                updateCalendars();
            }
        });
    }

    private void joinEvent() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                getCalendarManager().saveUserEvent(currentUser, event);
                updateCalendars();
            }
        });
    }

    private void leaveEvent() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                getCalendarManager().deleteUserFromEvent(currentUser, event);
                updateCalendars();
            }
        });
    }

    private EventManager getEventManager() {
        ApplicationContext springCtx = new ClassPathXmlApplicationContext("spring-context.xml");
        return (EventManager) springCtx.getBean("eventManager");
    }

    private CalendarManager getCalendarManager() {
        ApplicationContext springCtx = new ClassPathXmlApplicationContext("spring-context.xml");
        return (CalendarManager) springCtx.getBean("calendarManager");
    }

    private class UserJoinedQuery extends SwingWorker<Boolean,Object> {

        @Override
        protected Boolean doInBackground() {
            return getCalendarManager().getUsersForEvent(event).contains(currentUser);
        }
    }

    private void closeFrame() {
        setVisible(false);
    }

    private void updateCalendars() {
        Calendar.privateCalendar.setEvents(getCalendarManager().getEventsForUser(currentUser));
        Calendar.publicCalendar.setEvents(getEventManager().selectSharedEvents());
    }
}
