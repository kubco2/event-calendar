package cz.muni.fi.pv.projekt.gui;

import cz.muni.fi.pv.projekt.User;
import cz.muni.fi.pv.projekt.UserManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Zuzana Krejcova
 */
public class Login extends JFrame {

    private JTextField loginNickField;
    private JPasswordField loginPasswordField;
    private JButton login;
    private JTextField regNickField;
    private JTextField regNameField;
    private JPasswordField regPasswordField;
    private JButton register;
    private JComboBox localeSelect;
    private JProgressBar progressBar;
    private JSplitPane splitPane;
    
    private static Login instance;
    private ResourceBundle i18nLang;

    // this should not be used to create the login screen 
    // - use getInstance() instead - avoids unnecessary problems
    private Login() {
        i18nLang = ResourceBundle.getBundle("i18n.login", Locale.getDefault());
        init();
    }

    public static Login getInstance() {
        if (instance==null) instance = new Login();
        return instance;
    }

    private void init() {
        System.out.println("PERFORMING INITIALIZATION");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(i18nLang.getString("TITLE"));

        Locale[] locale = {new Locale("sk","SK"),new Locale("cs","CZ"),new Locale("en","GB")};
        localeSelect = new JComboBox(locale);
        localeSelect.setSelectedItem(Locale.getDefault());
        localeSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Locale.setDefault((Locale)localeSelect.getSelectedItem());
                i18nLang = ResourceBundle.getBundle("i18n.login", Locale.getDefault());
                
                setVisible(false);
                remove(splitPane);
                initSplitPane();
                add(splitPane);
                setVisible(true);
            }
        });
        
        initSplitPane();
        
        progressBar = new JProgressBar(0,0);

        add(localeSelect,BorderLayout.NORTH);
        add(splitPane);
        add(progressBar,BorderLayout.SOUTH);
        
        pack();
    }

    private void initSplitPane() {
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerSize(1);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(4, 2, 5, 5));
        leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(4, 2, 5, 5));
        rightPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel loginNickLabel = new JLabel(i18nLang.getString("NICK"));
        JLabel loginPasswordLabel = new JLabel(i18nLang.getString("PASSWORD"));
        loginNickField = new JTextField();
        loginPasswordField = new JPasswordField();
        login = new JButton(i18nLang.getString("LOGIN"));

        ActionListener loginListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((JButton)e.getSource()).setEnabled(false);
                LoginProcess lp = new LoginProcess();
                lp.addPropertyChangeListener(progressListener);
                lp.execute();
            }
        };
        login.addActionListener(loginListener);

        leftPanel.add(loginNickLabel);
        leftPanel.add(loginNickField);
        leftPanel.add(loginPasswordLabel);
        leftPanel.add(loginPasswordField);
        leftPanel.add(new Panel());
        leftPanel.add(login);
        leftPanel.add(new Panel());
        leftPanel.add(new Panel());


        JLabel regNickLabel = new JLabel(i18nLang.getString("NICK"));
        JLabel regNameLabel = new JLabel(i18nLang.getString("NAME"));
        JLabel regPasswordLabel = new JLabel(i18nLang.getString("PASSWORD"));
        regNickField = new JTextField();
        regNameField = new JTextField();
        regPasswordField = new JPasswordField();
        register = new JButton(i18nLang.getString("REGISTER"));

        ActionListener regListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((JButton)e.getSource()).setEnabled(false);
                RegisterProcess rp = new RegisterProcess();
                rp.addPropertyChangeListener(progressListener);
                rp.execute();
            }
        };
        register.addActionListener(regListener);

        rightPanel.add(regNickLabel);
        rightPanel.add(regNickField);
        rightPanel.add(regNameLabel);
        rightPanel.add(regNameField);
        rightPanel.add(regPasswordLabel);
        rightPanel.add(regPasswordField);
        rightPanel.add(new Panel());
        rightPanel.add(register);

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
    }

    private class LoginProcess extends SwingWorker<User,Integer> {
        @Override
        protected User doInBackground() throws Exception {
            setProgress(1);
            String nick = loginNickField.getText();
            char[] pass = loginPasswordField.getPassword();

            if(nick.length() ==0 || pass.length ==0) {
                throw new IllegalArgumentException(i18nLang.getString("EXC_FIELDS"));
            }

            ApplicationContext springCtx = new ClassPathXmlApplicationContext("spring-context.xml");
            UserManager userManager = (UserManager) springCtx.getBean("userManager");
            User user = userManager.selectUserByNick(nick);
            if(user==null || !Arrays.equals(user.getPassword().toCharArray(),pass)) {
                throw new IllegalAccessException(i18nLang.getString("EXC_BAD"));
            }
            return user;
        }

        @Override
        protected void done() {
            try {
                setProgress(2);
                User user = get();
                setVisible(false);
                CalendarMainView.getCalendarMainView(user).setVisible(true);
                loginNickField.setText(null);
                loginPasswordField.setText(null);
                regNameField.setText(null);
                regNickField.setText(null);
                regPasswordField.setText(null);
            } catch(Exception e) {
                JOptionPane.showMessageDialog(null,i18nLang.getString("EXC_LOGIN") +
                        e.getMessage(), i18nLang.getString("EXC_LOGIN_TITLE"), JOptionPane.ERROR_MESSAGE);
                LoggerFactory.getLogger(Login.class).error(i18nLang.getString("EXC_LOGIN"), e);
            } catch (Error er) {
                JOptionPane.showMessageDialog(null,i18nLang.getString("EXC_LOGIN") +
                        er.getCause().getMessage(), i18nLang.getString("EXC_LOGIN_TITLE"), JOptionPane.ERROR_MESSAGE);
                LoggerFactory.getLogger(Login.class).error(i18nLang.getString("EXC_LOGIN"), er);
            }
            login.setEnabled(true);
        }
    }

    private class RegisterProcess extends SwingWorker<User,Integer> {
        @Override
        protected User doInBackground() throws Exception {
            setProgress(1);
            User user = new User();
            user.setName(regNameField.getText());
            user.setNick(regNickField.getText());
            user.setPassword(String.valueOf(regPasswordField.getPassword()));

            if(user.getName().length() ==0 || user.getNick().length() ==0 || user.getPassword().length() ==0) {
                throw new IllegalArgumentException(i18nLang.getString("EXC_FIELDS"));
            }

            ApplicationContext springCtx = new ClassPathXmlApplicationContext("spring-context.xml");
            UserManager userManager = (UserManager) springCtx.getBean("userManager");
            if(userManager.selectUserByNick(user.getNick()) != null) {
                throw new IllegalArgumentException(i18nLang.getString("EXC_USED"));
            }
            userManager.createUser(user);
            if(user.getId()==null) {
                throw new IllegalArgumentException(i18nLang.getString("EXC_NOT_CREATED"));
            }
            return user;
        }

        @Override
        protected void done() {
            try {
                setProgress(2);
                User user = get();
                setVisible(false);
                CalendarMainView.getCalendarMainView(user).setVisible(true);
                loginNickField.setText(null);
                loginPasswordField.setText(null);
                regNameField.setText(null);
                regNickField.setText(null);
                regPasswordField.setText(null);
            } catch(Exception e) {
                JOptionPane.showMessageDialog(null,i18nLang.getString("EXC_REGISTRATION") +
                        e.getMessage(), i18nLang.getString("EXC_REGISTRATION_TITLE"), JOptionPane.ERROR_MESSAGE);
                LoggerFactory.getLogger(Login.class).error(i18nLang.getString("EXC_REGISTRATION"), e);
            } catch (Error er) {
                JOptionPane.showMessageDialog(null,i18nLang.getString("EXC_REGISTRATION") +
                        er.getCause().getMessage(), i18nLang.getString("EXC_REGISTRATION_TITLE"), JOptionPane.ERROR_MESSAGE);
                LoggerFactory.getLogger(Login.class).error(i18nLang.getString("EXC_REGISTRATION"), er);
            }
            register.setEnabled(true);
        }
    }

    private PropertyChangeListener progressListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(i18nLang.getString("PROGRESS"))) {
                if(evt.getNewValue().equals(1)) {
                    progressBar.setIndeterminate(true);
                } else {
                    progressBar.setIndeterminate(false);
                }
            }
        }
    };

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Login().setVisible(true);
            }
        });
    }
}
