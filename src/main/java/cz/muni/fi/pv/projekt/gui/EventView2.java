package cz.muni.fi.pv.projekt.gui;

import static java.util.Calendar.DAY_OF_MONTH;

import cz.muni.fi.pv.projekt.CalendarManager;
import cz.muni.fi.pv.projekt.Event;
import cz.muni.fi.pv.projekt.EventManager;
import cz.muni.fi.pv.projekt.User;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
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
    private String name;
    private String place;
    private String desc;
    private Date from;
    private Date to;
    private Boolean shared;
    private String joinLeaveSave;

    JTextField ownerField;
    JTextField nameField;
    JTextField placeField;
    JSpinner fromSpinner;
    JSpinner toSpinner;
    JTextArea descArea;
    JCheckBox shareBox;

    private ResourceBundle i18nLang;

    private boolean editable = true;

    public EventView2(User user, Event event) {
        super();
        i18nLang = ResourceBundle.getBundle("i18n.eventView2", Locale.getDefault());
        name = i18nLang.getString("<NAME>");
        place = i18nLang.getString("<PLACE>");
        desc = i18nLang.getString("<DESCRIBE>");
        from = new Date();
        to = new Date();
        shared = false;
        joinLeaveSave = i18nLang.getString("SAVE");
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
                    joinLeaveSave = i18nLang.getString("LEAVE");
                } else {
                    joinLeaveSave = i18nLang.getString("JOIN");
                }
            }
        } else this.event = new Event();
        init();
    }

    private void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(i18nLang.getString("EVENT"));

        // the root panel(s)
        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
        rootPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JPanel topPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        JPanel descPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        JPanel bottomPanel = new JPanel(new GridLayout(2, 2, 5, 5));

        // the content
        // owner
        JLabel ownerLabel = new JLabel(i18nLang.getString("OWNER"));
        ownerLabel.setFont(ownerLabel.getFont().deriveFont(Font.ITALIC));
        ownerField = new JTextField(owner);
        ownerField.setFont(ownerField.getFont().deriveFont(Font.ITALIC));
        ownerField.setEnabled(false);

        // name
        JLabel nameLabel = new JLabel(i18nLang.getString("NAME"));
        nameField = new JTextField(name);
        nameField.setEnabled(editable);

        // place
        JLabel placeLabel = new JLabel(i18nLang.getString("LOCATION"));
        placeField = new JTextField(place);
        placeField.setEnabled(editable);

        // from
        JLabel fromLabel = new JLabel(i18nLang.getString("STARTS"));
        fromSpinner = new JSpinner(new SpinnerDateModel(from, null, null, DAY_OF_MONTH));
        fromSpinner.setEnabled(editable);

        // to
        JLabel toLabel = new JLabel(i18nLang.getString("ENDS"));
        toSpinner = new JSpinner(new SpinnerDateModel(to, null, null, DAY_OF_MONTH));
        toSpinner.setEnabled(editable);

        // description
        JLabel descLabel = new JLabel(i18nLang.getString("DESCRIPTION"));
        descArea = new JTextArea(desc);
        descArea.setRows(5);
        descArea.setEnabled(editable);
        JScrollPane descScroll = new JScrollPane(descArea);

        // shared
        JLabel shareLabel = new JLabel(i18nLang.getString("SHARE"));
        shareBox = new JCheckBox(i18nLang.getString("IS_SHARED"), shared);
        shareBox.setEnabled(editable);

        // buttons
        JButton deleteButton = new JButton(i18nLang.getString("DELETE"));
        deleteButton.setEnabled(editable);

        ActionListener deleteListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // try to delete the event, show a message in case of failure
                try {
                    deleteEvent();
                    closeFrame();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,i18nLang.getString("EXC_DELETE") +
                        ex.getMessage(), i18nLang.getString("EXC_DELETE_TITLE"), JOptionPane.ERROR_MESSAGE);
                    LoggerFactory.getLogger(EventView2.class).error(
                        i18nLang.getString("EXC_DELETE"), ex);
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
                    if (e.getActionCommand().equals(i18nLang.getString("SAVE"))) {
                        saveEvent();
                    } else if(e.getActionCommand().equals(i18nLang.getString("JOIN"))) {
                        joinEvent();
                        ((JButton)e.getSource()).setText(i18nLang.getString("LEAVE"));
                    } else if (e.getActionCommand().equals(i18nLang.getString("LEAVE"))) {
                        leaveEvent();
                        ((JButton)e.getSource()).setText(i18nLang.getString("JOIN"));
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, String.format(i18nLang.getString("EXC_SAVE"),
                        e.getActionCommand().toLowerCase()) + ex.getMessage(),
                        i18nLang.getString("EXC_SAVE_TITLE"), JOptionPane.ERROR_MESSAGE);
                    LoggerFactory.getLogger(EventView2.class).error(
                        String.format(i18nLang.getString("EXC_SAVE"),
                        e.getActionCommand().toLowerCase()), ex);
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
            JOptionPane.showMessageDialog(null, i18nLang.getString("EXC_ASSERT_JOIN")
                + e.getMessage(), i18nLang.getString("EXC_SAVE_TITLE"), JOptionPane.ERROR_MESSAGE);
            LoggerFactory.getLogger(EventView2.class).error(i18nLang.getString("EXC_ASSERT_JOIN"), e);
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
