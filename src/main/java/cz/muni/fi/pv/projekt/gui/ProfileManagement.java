package cz.muni.fi.pv.projekt.gui;

import cz.muni.fi.pv.projekt.User;
import cz.muni.fi.pv.projekt.UserManager;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Zuzana Krejcova
 */
public class ProfileManagement extends JPanel {

    private User user = CalendarMainView.currentUser;

    JTextField nickField;
    JTextField nameField;
    JPasswordField passwordField;

    public ProfileManagement() {
        init();
    }

    private void init() {
        setLayout(new GridLayout(4, 2, 5, 5));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel nickLabel = new JLabel("New nick :");
        JLabel nameLabel = new JLabel("New name :");
        JLabel passwordLabel = new JLabel("New password :");
        nickField = new JTextField(user.getNick());
        nameField = new JTextField(user.getName());
        passwordField = new JPasswordField(user.getPassword());
        JButton update = new JButton("Update");

        ActionListener updateListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // maybe ensure atomicity sometime in the future?
                    user.setName(nameField.getText());
                    user.setNick(nickField.getText());
                    user.setPassword(String.valueOf(passwordField.getPassword()));
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            getUserManager().updateUser(user);
                            CalendarMainView.instance.updateUser(user.getName());
                        }
                    });
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
                try {
                    int answer = JOptionPane.showConfirmDialog(null, "You are about to delete "
                        + "your user account.\nDo you really want to do that?",
                        "User account deletion", JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                    if (answer==JOptionPane.YES_OPTION) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                getUserManager().deleteUser(user);
                            }
                        });
                        CalendarMainView.instance.logOut();
                    }
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

    private UserManager getUserManager() {
        ApplicationContext springCtx = new ClassPathXmlApplicationContext("spring-context.xml");
        return (UserManager) springCtx.getBean("userManager");
    }
}
