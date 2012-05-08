package cz.muni.fi.pv.projekt.gui;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Zuzana Krejcova
 */
public class Login
{
    public static void main(String[] args) {

        // Inicializaci GUI provedeme ve vlákně message dispatcheru,
        // ne v hlavním vlákně (bude vysvětleno později)!
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setTitle("Calendar - Login / Registration");

                JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false);
                splitPane.setResizeWeight(0.5);
                splitPane.setDividerSize(1);

                JPanel leftPanel = new JPanel();
                leftPanel.setLayout(new GridLayout(4, 2, 5, 5));
                leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
                JPanel rightPanel = new JPanel();
                rightPanel.setLayout(new GridLayout(4, 2, 5, 5));
                rightPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

                JLabel loginNickLabel = new JLabel("Nick");
                JLabel loginPasswordLabel = new JLabel("Password");
                JTextField loginNickField = new JTextField("nick");
                JPasswordField loginPasswordField = new JPasswordField("password");
                JButton login = new JButton("Login");

                ActionListener loginListener = new ActionListener() {
                    // TODO
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // nothing yet, to be implemented
                        // try to log in, show a message in case of failure
                        try {
                            throw new Exception("TEST - NOT IMPLEMENTED");
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null,"Login unsuccessful, exception caught: \n" +
                            ex.getMessage(), "Login unsuccessful!", JOptionPane.ERROR_MESSAGE);
                        }
                        return;
                    };
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


                JLabel regNickLabel = new JLabel("Nick");
                JLabel regNameLabel = new JLabel("Name");
                JLabel regPasswordLabel = new JLabel("Password");
                JTextField regNickField = new JTextField("nick");
                JTextField regNameField = new JTextField("name");
                JPasswordField regPasswordField = new JPasswordField("password");
                JButton register = new JButton("Register");

                ActionListener regListener = new ActionListener() {
                    // TODO
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // nothing yet, to be implemented
                        // try to register, show a message in case of failure
                        try {
                            throw new Exception("TEST - NOT IMPLEMENTED");
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null,"Registration unsuccessful, exception caught: \n" +
                            ex.getMessage(), "Registration unsuccessful!", JOptionPane.ERROR_MESSAGE);
                        }
                        return;
                    };
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

                frame.add(splitPane);

                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
