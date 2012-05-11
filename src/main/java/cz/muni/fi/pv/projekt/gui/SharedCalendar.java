package cz.muni.fi.pv.projekt.gui;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Zuzana Krejcova
 */
public class SharedCalendar extends JPanel {

    public SharedCalendar() {
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        Calendar calendar = new Calendar().setEvents(CalendarMainView.sharedEvents);
        add(calendar);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SharedCalendar().setVisible(true);
            }
        });
    }
}
