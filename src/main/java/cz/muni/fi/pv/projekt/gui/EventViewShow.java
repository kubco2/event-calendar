package cz.muni.fi.pv.projekt.gui;

import cz.muni.fi.pv.projekt.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: shifty
 * Date: 08/05/12
 * Time: 15:26
 */
public class EventViewShow extends EventView {

    private boolean isJoined;

    public EventViewShow(Event event,User user) {
        super(event,user);
        this.setTitle(event.getName()+" - "+event.getOwner().getName());
        isJoined = isJoined();
        fillForm();
        initButtons();
    }

    public boolean isJoined() {
        ApplicationContext springCtx = new ClassPathXmlApplicationContext("spring-context.xml");
        CalendarManager calendarManager = (CalendarManagerImpl) springCtx.getBean("calendarManager");
        List<User> listOFusers = calendarManager.getUsersForEvent(getEvent());
        return listOFusers.contains(getUser());
    }

    public void initButtons() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.LINE_AXIS));
        if(isOwner()) {
            JButton buttonEdit = new JButton("Edit");
            buttonEdit.addActionListener(actionEdit);
            JButton buttonDelete = new JButton("Delete");
            buttonDelete.addActionListener(actionDelete);
            panel.add(buttonEdit);
            panel.add(buttonDelete);
        } else {
            JButton buttonJoin = new JButton((isJoined)?"Leave":"Join");
            buttonJoin.addActionListener(actionJoin);
            panel.add(buttonJoin);
        }
        setBoxButtons(panel);
    }

    public void editButtons() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.LINE_AXIS));
        JButton buttonSave = new JButton("Save");
        buttonSave.addActionListener(actionSave);
        JButton buttonShared = new JButton((getEvent().isShared())?"Public":"Private");
        buttonShared.addActionListener(actionShared);
        JButton buttonUndo = new JButton("Back");
        buttonUndo.addActionListener(actionBack);
        panel.add(buttonSave);
        panel.add(buttonShared);
        panel.add(buttonUndo);
        setBoxButtons(panel);
    }

    public void saveEvent() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ApplicationContext springCtx = new ClassPathXmlApplicationContext("spring-context.xml");
                EventManager eventManager = (EventManager) springCtx.getBean("eventManager");
                eventManager.updateEvent(getEvent());
            }
        });
    }

    ActionListener actionEdit = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            editButtons();
            setEditable(true);
        }
    };

    ActionListener actionDelete = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            throw new UnsupportedOperationException();
        }
    };

    ActionListener actionJoin = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(isJoined) {
                isJoined=false;
                ((JButton)e.getSource()).setText("Join");
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        ApplicationContext springCtx = new ClassPathXmlApplicationContext("spring-context.xml");
                        CalendarManager calendarManager = (CalendarManagerImpl) springCtx.getBean("calendarManager");
                        calendarManager.deleteUserFromEvent(getUser(),getEvent());
                    }
                });
            } else {
                isJoined=true;
                ((JButton)e.getSource()).setText("Leave");
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        ApplicationContext springCtx = new ClassPathXmlApplicationContext("spring-context.xml");
                        CalendarManager calendarManager = (CalendarManagerImpl) springCtx.getBean("calendarManager");
                        calendarManager.saveUserEvent(getUser(),getEvent());
                    }
                });
            }
        }
    };

    ActionListener actionSave = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    setEventData();
                    saveEvent();
                }
            });
        }
    };

    ActionListener actionShared = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(getEvent().isShared()) {
                ((JButton)e.getSource()).setText("Private");
                getEvent().setShared(false);
            } else {
                ((JButton)e.getSource()).setText("Public");
                getEvent().setShared(true);
            }
            saveEvent();
        }
    };

    ActionListener actionBack = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            initButtons();
            setEditable(false);
            fillForm();
        }
    };

}
