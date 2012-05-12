package cz.muni.fi.pv.projekt.gui;

import java.awt.BorderLayout;
import javax.swing.JPanel;

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
        Calendar calendar = Calendar.getCalendar(null, true).setEvents(CalendarMainView.sharedEvents);
        add(calendar);
    }
}
