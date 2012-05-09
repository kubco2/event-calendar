package cz.muni.fi.pv.projekt.gui;

import cz.muni.fi.pv.projekt.Event;
import cz.muni.fi.pv.projekt.EventManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: shifty
 * Date: 08/05/12
 * Time: 19:06
 */
public class Calendar extends JPanel{

    JPanel content;
    JLabel monthName;

    private java.util.Calendar now;
    private java.util.Calendar show;

    private Locale locale;
    private SimpleDateFormat fullDateFormat;
    private SimpleDateFormat monthYearDateFormat;

    public Calendar(Locale locale){
        this.locale=(locale==null)?Locale.ENGLISH:locale;
        java.util.Calendar today = java.util.Calendar.getInstance(locale);
        today.set(java.util.Calendar.HOUR,0);
        today.set(java.util.Calendar.MINUTE,0);
        today.set(java.util.Calendar.SECOND,0);
        today.set(java.util.Calendar.MILLISECOND,0);
        show = today;
        now = (java.util.Calendar)show.clone();
        fullDateFormat = new SimpleDateFormat("EEEE, dd. MMMM yyyy",locale);
        monthYearDateFormat = new SimpleDateFormat("MMMM, yyyy",locale);
        setLayout(new BorderLayout());
        initTop();
        initContent();
    }

    private void initTop() {
        JPanel top = new JPanel();
        JButton next = new JButton("<--");
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prev();
            }
        });
        JButton previous = new JButton("-->");
        previous.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                next();
            }
        });
        monthName = new JLabel();
        top.add(next);
        top.add(monthName);
        top.add(previous);

        add(top,BorderLayout.NORTH);
    }

    private String getMonthYear() {
        return monthYearDateFormat.format(show.getTime());
    }

    private void initContent() {
        content = new JPanel();
        content.setLayout(new GridLayout(0, 7));

        paintCalendar();

        add(content,BorderLayout.CENTER);
    }

    public void recreate() {
        content.removeAll();
        content.revalidate();
        content.repaint();
    }

    private void paintCalendar() {
        recreate();

        monthName.setText(getMonthYear());
        DateFormatSymbols dfs = new DateFormatSymbols();
        String weekdays[] = dfs.getWeekdays();

        Field mo = Field.getHeadField(weekdays[2]);
        Field tu = Field.getHeadField(weekdays[3]);
        Field we = Field.getHeadField(weekdays[4]);
        Field th = Field.getHeadField(weekdays[5]);
        Field fr = Field.getHeadField(weekdays[6]);
        Field sa = Field.getHeadField(weekdays[7]);
        Field su = Field.getHeadField(weekdays[1]);

        content.add(mo);
        content.add(tu);
        content.add(we);
        content.add(th);
        content.add(fr);
        content.add(sa);
        content.add(su);

        Month month = new Month(show);
        paintFields(month);
    }

    private void paintFields(Month month) {
        for(int i=1;i<month.getStartDayInMonth();i++) {
            content.add(new JLabel());
        }
        List<Day> days = month.getDays();
        for(int day=0;day<days.size(); day++) {
            content.add(Field.getDayField(String.valueOf(day + 1),days.get(day)));
        }
    }

    public void next() {
        show.add(java.util.Calendar.MONTH, 1);
        paintCalendar();
    }
    public void prev() {
        show.add(java.util.Calendar.MONTH, -1);
        paintCalendar();
    }

    /**
     * Day data structure for Field cell of Month in calendar view
     */
    class Day {
        private Date date;
        private List<Event> events = new ArrayList<Event>();
        public Day(Date date) {
            if(date == null) {
                throw new IllegalArgumentException("bad generated day");
            }
            this.date = date;

            ApplicationContext springCtx = new ClassPathXmlApplicationContext("spring-context.xml");
            EventManager eventManager = (EventManager) springCtx.getBean("eventManager");
            Event e = eventManager.selectEventById(1L);
            events.add(e);

        }
        public boolean isToday() {
            return date.equals(now.getTime());
        }
        public boolean hasEvents() {
            return events.size()>0;
        }
        public List<Event> getEvents() {
            return events;
        }

        @Override
        public String toString() {
            return fullDateFormat.format(date);
        }
    }

    /**
     * Month data structure in calendar view
     */
    class Month {
        private int startDayInMonth;
        private int numOfDays;
        private java.util.Calendar calendar;

        private List<Day> days = new ArrayList<Day>();

        public Month(java.util.Calendar calendar) {
            this.calendar=(java.util.Calendar)calendar.clone();
            init();
            fillWithDays();
        }

        private void init() {
            calendar.set(java.util.Calendar.DAY_OF_MONTH,1);
            numOfDays = calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
            startDayInMonth = calendar.get(java.util.Calendar.DAY_OF_WEEK);
        }

        private void fillWithDays() {
            for(int day=0;day<numOfDays;day++) {
                days.add(new Day(calendar.getTime()));
                calendar.add(java.util.Calendar.DAY_OF_MONTH, 1);
            }
        }

        public int getStartDayInMonth() {
            return (startDayInMonth==1)?7:startDayInMonth-1;
        }

        public List<Day> getDays() {
            return days;
        }
    }
}

/**
 * One cell of month in calendar view
 */
class Field extends JLabel {

    private Calendar.Day day;

    public static Field getHeadField(String string) {
        Field field = new Field(string);
        field.setHeadLook();
        return field;
    }
    public static Field getDayField(String string, Calendar.Day day) {
        Field field = new Field(string);
        field.setDay(day);
        field.setDayLook();
        return field;
    }

    public Field(String text) {
        super(text);
        setOpaque(true);
    }

    public void setHeadLook() {
        setHorizontalAlignment(SwingConstants.CENTER);
        setBackground(Color.LIGHT_GRAY);
    }

    public void setDayLook() {
        setVerticalAlignment(SwingConstants.TOP);
        setBorder(BorderFactory.createBevelBorder(1));
        if(day.hasEvents()) {
            setBackground(new Color(0xD1CCFF));
        }
        if(day.isToday()) {
            setBackground(new Color(0xFFBABA+((day.hasEvents())?0xD1CCFF:0)));
        }
        addMouseListener(actionMouse);
    }

    public void setDay(Calendar.Day day) {
        this.day=day;
    }

    private MouseListener actionMouse = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            new EventListView(day);
        }
    };
}
