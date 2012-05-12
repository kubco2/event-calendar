package cz.muni.fi.pv.projekt.gui;

import cz.muni.fi.pv.projekt.User;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Zuzana Krejcova
 */
public class ProfileManagement extends JPanel {

    private User user = CalendarMainView.currentUser;

    public ProfileManagement() {
        init();
    }

    private void init() {
        setLayout(new GridLayout(4, 2, 5, 5));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel nickLabel = new JLabel("New nick :");
        JLabel nameLabel = new JLabel("New name :");
        JLabel passwordLabel = new JLabel("New password :");
        JTextField nickField = new JTextField(user.getNick());
        JTextField nameField = new JTextField(user.getName());
        JPasswordField passwordField = new JPasswordField(user.getPassword());
        JButton update = new JButton("Update");

        ActionListener updateListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO update the profile
            }
        };
        update.addActionListener(updateListener);

        JButton delete = new JButton("Delete Profile");

        ActionListener deleteListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO delete the profile
            }
        };
        delete.addActionListener(deleteListener);

        add(nickLabel);
        add(nickField);
        add(nameLabel);
        add(nameField);
        add(passwordLabel);
        add(passwordField);
        add(delete);
        add(update);
    }
}
