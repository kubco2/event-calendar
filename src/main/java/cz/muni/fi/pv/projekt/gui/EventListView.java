package cz.muni.fi.pv.projekt.gui;

import cz.muni.fi.pv.projekt.User;
import cz.muni.fi.pv.projekt.Event;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * User: shifty
 * Date: 09/05/12
 * Time: 17:51
 */
public class EventListView extends JFrame {

    private Calendar.Day day;

    public EventListView(Calendar.Day day) {
        if(day == null) {
            throw new IllegalArgumentException("day cant be null");
        }
        this.day=day;
        initTop();
        initContent();
        setSize(200,300);
        setVisible(true);
    }

    private void initTop() {
        JPanel top = new JPanel(new FlowLayout());
        JButton create = new JButton("create event");
        create.addActionListener(actionCreate);
        top.add(create);

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
                Box box = new Box(BoxLayout.LINE_AXIS);
                box.add(new EventField(event));
                content.add(box);
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
            EventView newEvent = new EventViewNew(new User());
            newEvent.setVisible(true);
        }
    };
}

class EventField extends JLabel{

    public EventField(Event evt) {
        final Event event = evt;
        setText(event.getOwner().getName()+" "+event.getName()+" "+event.getPlace());
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EventView ev = new EventViewShow(event,CalendarMainView.currentUser);
                ev.setVisible(true);
            }
        });
    }


}
