package cz.muni.fi.pv.projekt.gui;

import java.awt.EventQueue;
import javax.swing.JPanel;

/**
 *
 * @author Zuzana Krejcova
 */
public class ProfileManagement extends JPanel {

    public ProfileManagement() {
        init();
    }

    private void init() {
        
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ProfileManagement().setVisible(true);
            }
        });
    }
}
