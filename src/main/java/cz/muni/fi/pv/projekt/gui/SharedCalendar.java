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
}
