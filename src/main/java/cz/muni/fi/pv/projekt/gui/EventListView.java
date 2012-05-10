package cz.muni.fi.pv.projekt.gui;

import cz.muni.fi.pv.projekt.Event;
import cz.muni.fi.pv.projekt.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;

/**
 * Created by IntelliJ IDEA.
 * User: shifty
 * Date: 09/05/12
 * Time: 17:51
 */
public class EventListView extends JFrame {

    private Calendar.Day day;
    private SimpleDateFormat dateFormat;

    public EventListView(Calendar.Day day) {
        if(day == null) {
            throw new IllegalArgumentException("day cant be null");
        }
        this.day=day;

        initTop();
        initContent();
        setSize(300,300);
        setVisible(true);
    }

    private void initTop() {
        JPanel top = new JPanel(new FlowLayout());
        JButton create = new JButton("create event");
        create.addActionListener(actionCreate);
        top.add(create);
        top.add(new JLabel(day.toString()));
        add(top,BorderLayout.NORTH);
    }

    private void initContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content,BoxLayout.PAGE_AXIS));
        if(!day.hasEvents()) {
            JLabel noEvents = new JLabel("No events");
            Box box = new Box(BoxLayout.LINE_AXIS);
            box.add(noEvents);
            content.add(box);
        } else {
            for(Event event : day.getEvents()) {
                //Box box = new Box(BoxLayout.LINE_AXIS);
                //box.add(new EventField(event));
                content.add(new EventField(event));
            }
        }

        add(content, BorderLayout.CENTER);
    }

    private ActionListener actionCreate = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventView newEvent = new EventViewNew(new User());
            newEvent.setVisible(true);
        }
    };
    private ActionListener actionShowEvent = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            EventView newEvent = new EventViewNew(CalendarMainView.currentUser);
            newEvent.setVisible(true);
        }
    };
}

class EventField extends JPanel{

    private Event event;

    public EventField(Event evt) {
        event = evt;

        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
        init();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EventView ev = new EventViewShow(event, CalendarMainView.currentUser);
                ev.setVisible(true);
            }
        });
    }

    private void init() {
        JLabel from1 = new JLabel("From:    ");
        JLabel from2 = new JLabel(event.getFrom().toString());
        Box from = new Box(BoxLayout.LINE_AXIS);
        from.add(from1);
        from.add(from2);
        JLabel to1 = new JLabel("To:    ");
        JLabel to2 = new JLabel(event.getTo().toString());
        Box to = new Box(BoxLayout.LINE_AXIS);
        to.add(to1);
        to.add(to2);
        JLabel name = new JLabel(event.getName());
        add(from);
        add(to);
        add(name);
    }


}
