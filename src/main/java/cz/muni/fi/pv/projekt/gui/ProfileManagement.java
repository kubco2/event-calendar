package cz.muni.fi.pv.projekt.gui;

import cz.muni.fi.pv.projekt.User;
import cz.muni.fi.pv.projekt.UserManager;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
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

    private ResourceBundle i18nLang;

    public ProfileManagement() {
        i18nLang = ResourceBundle.getBundle("i18n.profileManagement", Locale.getDefault());
        init();
    }

    private void init() {
        setLayout(new GridLayout(4, 2, 5, 5));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel nickLabel = new JLabel(i18nLang.getString("NICK"));
        JLabel nameLabel = new JLabel(i18nLang.getString("NAME"));
        JLabel passwordLabel = new JLabel(i18nLang.getString("PASSWORD"));
        nickField = new JTextField(user.getNick());
        nameField = new JTextField(user.getName());
        passwordField = new JPasswordField(user.getPassword());
        JButton update = new JButton(i18nLang.getString("UPDATE"));

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
                    JOptionPane.showMessageDialog(null,i18nLang.getString("EXC_UPDATE") +
                            ex.getMessage(), i18nLang.getString("EXC_UPDATE_TITLE"), JOptionPane.ERROR_MESSAGE);
                    LoggerFactory.getLogger(ProfileManagement.class).error(i18nLang.getString("EXC_UPDATE"), e);
                } catch (Error er) {
                    JOptionPane.showMessageDialog(null,i18nLang.getString("EXC_UPDATE") +
                            er.getCause().getMessage(), i18nLang.getString("EXC_UPDATE_TITLE"), JOptionPane.ERROR_MESSAGE);
                    LoggerFactory.getLogger(ProfileManagement.class).error(i18nLang.getString("EXC_UPDATE"), er);
                }
            }
        };
        update.addActionListener(updateListener);

        JButton delete = new JButton(i18nLang.getString("DELETE"));

        ActionListener deleteListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int answer = JOptionPane.showConfirmDialog(null, i18nLang.getString("WARN_DELETE"),
                        i18nLang.getString("WARN_DELETE_TITLE"), JOptionPane.YES_NO_OPTION,
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
                    JOptionPane.showMessageDialog(null,i18nLang.getString("EXC_DELETE") +
                            ex.getMessage(), i18nLang.getString("EXC_DELETE_TITLE"), JOptionPane.ERROR_MESSAGE);
                    LoggerFactory.getLogger(ProfileManagement.class).error(i18nLang.getString("EXC_DELETE"), e);
                } catch (Error er) {
                    JOptionPane.showMessageDialog(null,i18nLang.getString("EXC_DELETE") +
                            er.getCause().getMessage(), i18nLang.getString("EXC_DELETE_TITLE"), JOptionPane.ERROR_MESSAGE);
                    LoggerFactory.getLogger(ProfileManagement.class).error(i18nLang.getString("EXC_DELETE"), er);
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
