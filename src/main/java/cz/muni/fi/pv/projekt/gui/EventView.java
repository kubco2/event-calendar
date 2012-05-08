package cz.muni.fi.pv.projekt.GUI;

import cz.muni.fi.pv.projekt.Event;
import cz.muni.fi.pv.projekt.User;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: shifty
 * Date: 08/05/12
 * Time: 14:18
 */
public class EventView extends JFrame{

    private Event event;
    private User user;

    public EventView(Event event,User user) {
        if(event == null || user == null) {
            throw new IllegalArgumentException("event and user must be initialized");
        }
        this.event=event;
        this.user=user;
        this.setSize(420,320);
        init();
        setEditable(false);
    }

    public void init() {

        JLabel labelName  = new JLabel("name") ;
        labelName.setMaximumSize(new Dimension(200, 20));
        JLabel labelPlace = new JLabel("place");
        labelPlace.setMaximumSize(new Dimension(200, 20));
        JLabel labelFrom  = new JLabel("Date started");
        labelFrom.setMaximumSize(new Dimension(200, 20));
        JLabel labelTo    = new JLabel("Date end");
        labelTo.setMaximumSize(new Dimension(200, 20));
        JLabel labelDescr = new JLabel("Description");
        labelDescr.setMaximumSize(new Dimension(200, 20));

        editName   = new JTextField();
        editName.setMaximumSize(new Dimension(200, 20));
        editPlace  = new JTextField();
        editPlace.setMaximumSize(new Dimension(200, 20));
        editFrom   = new JTextField();
        editFrom.setMaximumSize(new Dimension(200, 20));
        editTo     = new JTextField();
        editTo.setMaximumSize(new Dimension(200, 20));
        editDescr  = new JTextArea();
        editDescr.setLineWrap(true);
        editDescr.setWrapStyleWord(true);
        editDescr.setMaximumSize(new Dimension(200, 200));
        editDescr.setMinimumSize(new Dimension(200, 200));
        editDescr.setPreferredSize(new Dimension(200, 200));
        listOfEditable = new ArrayList<JTextComponent>();
        listOfEditable.add(editName);
        listOfEditable.add(editPlace);
        listOfEditable.add(editFrom);
        listOfEditable.add(editTo);
        listOfEditable.add(editDescr);

        Box boxName  = new Box(BoxLayout.LINE_AXIS);
        boxName.add(labelName);
        boxName.add(editName);

        Box boxPlace  = new Box(BoxLayout.LINE_AXIS);
        boxPlace.add(labelPlace);
        boxPlace.add(editPlace);

        Box boxFrom  = new Box(BoxLayout.LINE_AXIS);
        boxFrom.add(labelFrom);
        boxFrom.add(editFrom);

        Box boxTo  = new Box(BoxLayout.LINE_AXIS);
        boxTo.add(labelTo);
        boxTo.add(editTo);

        Box boxDescr  = new Box(BoxLayout.LINE_AXIS);
        boxDescr.add(labelDescr);
        boxDescr.add(editDescr);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.PAGE_AXIS));
        panel.add(boxButtons);
        panel.add(boxName);
        panel.add(boxPlace);
        panel.add(boxFrom);
        panel.add(boxTo);
        panel.add(boxDescr);

        this.add(panel,BorderLayout.NORTH);
    }

    public void setEditable(boolean bool) {
        for(JTextComponent field: listOfEditable) {
            field.setEditable(bool);
        }
    }

    public void setEventData() {
        event.setName(editName.getText());
        event.setPlace(editPlace.getText());
        event.setFrom(new Date());
        event.setTo(new Date());
        event.setDescription(editDescr.getText());
    }

    public void fillForm() {
        editName.setText(event.getName());
        editPlace.setText(event.getPlace());
        editFrom.setText(event.getFrom().toString());
        editTo.setText(event.getTo().toString());
        editDescr.setText(event.getDescription());
    }

    public void setBoxButtons(Component buttons) {
        this.boxButtons.removeAll();
        this.boxButtons.add(buttons);
    }

    public Event getEvent() {
        return event;
    }
    public User getUser() {
        return user;
    }
    public boolean isOwner() {
        return user.equals(event.getOwner());
    }

    private Box boxButtons = new Box(BoxLayout.LINE_AXIS);
    private JTextField editName;
    private JTextField editPlace;
    private JTextField editFrom;
    private JTextField editTo;
    private JTextArea editDescr;
    private List<JTextComponent> listOfEditable;
}
