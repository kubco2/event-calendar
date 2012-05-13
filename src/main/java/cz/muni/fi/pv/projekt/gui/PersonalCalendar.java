package cz.muni.fi.pv.projekt.gui;

import java.awt.BorderLayout;
import javax.swing.JPanel;

/**
 *
 * @author Zuzana Krejcova
 */
public class PersonalCalendar extends JPanel {

    public PersonalCalendar() {
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        Calendar calendar = Calendar.getCalendar(false).setEvents(CalendarMainView.privateEvents);
        add(calendar);
    }
}
