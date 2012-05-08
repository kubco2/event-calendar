package cz.muni.fi.pv.projekt.gui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 *
 * @author Zuzana Krejcova
 */
public class CalendarMainView extends JFrame {

    // JPanels to be used in the main tabbed pane
    // all should be filled out in the respective classes
    private ProfileManagement profileManagement;
    private SharedCalendar sharedCal;
    private PersonalCalendar personalCal;

    // main tabbed pane containing calendar and profile panels
    private JTabbedPane mainPanel;

    /* Apart from the above, we could do a menu with buttons/items/options
     * that would do the same as clicking the tabs on the tabbed pane mainPanel.
     * In the menu, there could also be some New event and Quit options,
     * perhaps even shortcuts to deleting user or other some such.
     */

    public CalendarMainView() {
        init();
    }

    private void init() {

    }

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CalendarMainView().setVisible(true);
            }
        });
    }
}
