package cz.muni.fi.pv.projekt.gui;

import javax.swing.*;
import java.awt.*;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 *
 * @author Zuzana Krejcova
 */
public class PersonalCalendar extends JPanel {

    public PersonalCalendar() {
        setLayout(new BorderLayout());
        init();
    }

    private void init() {
        GregorianCalendar gc = new GregorianCalendar();
        //Calendar calendar = new Calendar(gc.get(java.util.Calendar.MONTH),gc.get(java.util.Calendar.YEAR));
        Calendar calendar = new Calendar(new Locale("sk","SK"));
        add(calendar);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PersonalCalendar().setVisible(true);
            }
        });
    }
}
