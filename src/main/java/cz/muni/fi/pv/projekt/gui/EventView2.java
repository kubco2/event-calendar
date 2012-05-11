package cz.muni.fi.pv.projekt.gui;

import cz.muni.fi.pv.projekt.Event;
import cz.muni.fi.pv.projekt.User;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicBorders.ButtonBorder;

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

    private boolean editable = true;

    public EventView2() {
        init();
    }

    public EventView2(User user, Event event) {
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
            }
            if (userJoined()) {
                joinLeaveSave = "Leave";
            } else {
                joinLeaveSave = "Join";
            }
        }
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
        JTextField ownerField = new JTextField(owner);
        ownerField.setFont(ownerField.getFont().deriveFont(Font.ITALIC));
        ownerField.setEnabled(false);

        // name
        JLabel nameLabel = new JLabel("Event name");
        JTextField nameField = new JTextField(name);
        nameField.setEnabled(editable);

        // place
        JLabel placeLabel = new JLabel("Location");
        JTextField placeField = new JTextField(place);
        placeField.setEnabled(editable);

        // from
        JLabel fromLabel = new JLabel("Event starts");
        JSpinner fromSpinner = new JSpinner(new SpinnerDateModel(from, null, null, Calendar.DAY_OF_MONTH));
        fromSpinner.setEnabled(editable);

        // to
        JLabel toLabel = new JLabel("Event ends");
        JSpinner toSpinner = new JSpinner(new SpinnerDateModel(to, null, null, Calendar.DAY_OF_MONTH));
        toSpinner.setEnabled(editable);

        // description
        JLabel descLabel = new JLabel("Description");
        JTextArea descArea = new JTextArea(desc);
        descArea.setRows(5);
        descArea.setEnabled(editable);
        JScrollPane descScroll = new JScrollPane(descArea);

        // shared
        JLabel shareLabel = new JLabel("Will others see this event?");
        JCheckBox shareBox = new JCheckBox("Is shared event.", shared);
        shareBox.setEnabled(editable);

        // buttons
        JButton deleteButton = new JButton("Delete");
        deleteButton.setEnabled(editable);

        ActionListener deleteListener = new ActionListener() {
            // TODO
            @Override
            public void actionPerformed(ActionEvent e) {
                // nothing yet, to be implemented
                // try to delete the event, show a message in case of failure
                try {
                    throw new Exception("TEST - NOT IMPLEMENTED");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,"Could not delete the event, exception caught: \n" +
                    ex.getMessage(), "Deletion unsuccessful!", JOptionPane.ERROR_MESSAGE);
                }
                return;
            };
        };
        deleteButton.addActionListener(deleteListener);

        JButton saveButton = new JButton(joinLeaveSave);

        ActionListener saveListener = new ActionListener() {
            // TODO
            @Override
            public void actionPerformed(ActionEvent e) {
                // nothing yet, to be implemented
                // try to save the event, show a message in case of failure
                try {
                    // TODO actions according to the button "state"
                    if (e.getActionCommand().equals("Save")) {}
                    else if(e.getActionCommand().equals("Join")) {
                    ((JButton)e.getSource()).setText("Leave");}
                    else if (e.getActionCommand().equals("Leave")) {
                    ((JButton)e.getSource()).setText("Join");}
                    throw new Exception("TEST - NOT IMPLEMENTED");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,"Could not "+e.getActionCommand().toLowerCase()
                            + " the event, exception caught: \n" + ex.getMessage(),
                            "Operation unsuccessful!", JOptionPane.ERROR_MESSAGE);
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

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EventView2().setVisible(true);
            }
        });
    }

    // TODO
    private boolean userJoined() {
        return false;
    }
}
