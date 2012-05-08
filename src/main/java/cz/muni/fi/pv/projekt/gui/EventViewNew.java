package cz.muni.fi.pv.projekt.gui;

import cz.muni.fi.pv.projekt.Event;
import cz.muni.fi.pv.projekt.EventManager;
import cz.muni.fi.pv.projekt.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: shifty
 * Date: 08/05/12
 * Time: 15:24
 */
public class EventViewNew extends EventView {

    public EventViewNew(User user) {
        super(new Event(),user);
        getEvent().setOwner(user);
        this.setTitle("Create new event");
        setEditable(true);
        initButtons();
    }

    public void initButtons() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.LINE_AXIS));
        JButton buttonCreate = new JButton("Create event");
        buttonCreate.addActionListener(actionCreate);
        JButton buttonShared = new JButton("Private");
        buttonShared.addActionListener(actionShared);
        panel.add(buttonCreate);
        panel.add(buttonShared);
        setBoxButtons(panel);
    }

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
        }
    };

    ActionListener actionCreate = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            setEventData();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    ApplicationContext springCtx = new ClassPathXmlApplicationContext("spring-context.xml");
                    EventManager eventManager = (EventManager) springCtx.getBean("eventManager");
                    eventManager.createEvent(getEvent());
                }
            });
        }
    };

}
