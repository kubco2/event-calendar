package cz.muni.fi.pv.projekt.gui;

import cz.muni.fi.pv.projekt.Event;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.EmptyBorder;

/**
 * Created by IntelliJ IDEA.
 * User: shifty
 * Date: 09/05/12
 * Time: 17:51
 */
public class EventListView extends JFrame {

    private Calendar.Day day;
    private JPanel root;

    public EventListView(Calendar.Day day) {
        if(day == null) {
            throw new IllegalArgumentException("day cant be null");
        }
        this.day=day;

        init();
    }

    private void init() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(day.toString());
        root = new JPanel(new BorderLayout(0, 5));
        root.setBorder(new EmptyBorder(10, 10, 10, 10));
        initTop();
        initContent();
        initBottom();
        add(root);
        pack();
    }

    private void initTop() {
        root.add(new JLabel(day.toString()),BorderLayout.NORTH);
    }

    private void initContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content,BoxLayout.PAGE_AXIS));
        if(!day.hasEvents()) {
            JLabel noEvents = new JLabel("There are no events.");
            Box box = new Box(BoxLayout.LINE_AXIS);
            box.add(noEvents);
            content.add(box);
        } else {
            boolean odd = true;
            for(Event event : day.getEvents()) {
                content.add(new EventField(event, odd));
                odd = !odd;
            }
        }

        root.add(content, BorderLayout.CENTER);
    }

    private void initBottom() {
        JButton create = new JButton("Create Event");
        ActionListener actionCreate = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // this view is very probably invalid after working on an event
                setVisible(false);
                new EventView2(CalendarMainView.currentUser, null).setVisible(true);
            }
        };
        create.addActionListener(actionCreate);
        root.add(create,BorderLayout.SOUTH);
    }
}

class EventField extends JPanel {

    private Event event;

    public EventField(Event evt, boolean odd) {
        event = evt;
        Color colour = odd? Color.WHITE:getBackground();
        init(colour);
    }

    private void init(Color colour) {
        setLayout(new GridLayout(3, 2, 5, 5));

        setBackground(colour);

        JLabel name = new JLabel(event.getName());
        name.setFont(name.getFont().deriveFont(Font.ITALIC));

        JPanel empty = new JPanel();
        empty.setBackground(colour);

        JLabel fromLabel = new JLabel("From :");
        JLabel fromValue = new JLabel(event.getFrom().toString());

        JLabel toLabel = new JLabel("To :");
        JLabel toValue = new JLabel(event.getTo().toString());

        add(name);
        add(empty);
        add(fromLabel);
        add(fromValue);
        add(toLabel);
        add(toValue);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // this view is very probably invalid after working on an event
                setVisible(false);
                new EventView2(CalendarMainView.currentUser, event).setVisible(true);
            }
        });
    }
}
