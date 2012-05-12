package cz.muni.fi.pv.projekt.gui;

import cz.muni.fi.pv.projekt.User;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import org.slf4j.LoggerFactory;

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
                try {
                    throw new Exception("NOT YET IMPLEMENTED");
                } catch(Exception ex) {
                    JOptionPane.showMessageDialog(null,"Update unsuccessful, exception caught: \n" +
                            ex.getMessage(), "Update unsuccessful!", JOptionPane.ERROR_MESSAGE);
                    LoggerFactory.getLogger(ProfileManagement.class).error("Update unsuccessful, exception caught: \n", e);
                } catch (Error er) {
                    JOptionPane.showMessageDialog(null,"Update unsuccessful, exception caught: \n" +
                            er.getCause().getMessage(), "Update unsuccessful!", JOptionPane.ERROR_MESSAGE);
                    LoggerFactory.getLogger(ProfileManagement.class).error("Update unsuccessful, exception caught: \n", er);
                }
            }
        };
        update.addActionListener(updateListener);

        JButton delete = new JButton("Delete Profile");

        ActionListener deleteListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO delete the profile
                try {
                    throw new Exception("NOT YET IMPLEMENTED");
                } catch(Exception ex) {
                    JOptionPane.showMessageDialog(null,"Deletion unsuccessful, exception caught: \n" +
                            ex.getMessage(), "Deletion unsuccessful!", JOptionPane.ERROR_MESSAGE);
                    LoggerFactory.getLogger(ProfileManagement.class).error("Update unsuccessful, exception caught: \n", e);
                } catch (Error er) {
                    JOptionPane.showMessageDialog(null,"Deletion unsuccessful, exception caught: \n" +
                            er.getCause().getMessage(), "Deletion unsuccessful!", JOptionPane.ERROR_MESSAGE);
                    LoggerFactory.getLogger(ProfileManagement.class).error("Deletion unsuccessful, exception caught: \n", er);
                }
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
