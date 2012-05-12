package cz.muni.fi.pv.projekt.gui;

import cz.muni.fi.pv.projekt.Event;

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
public class Calendar extends JPanel {

    JPanel content;
    JLabel monthName;

    private java.util.Date now;
    private java.util.Calendar show;

    private Locale locale;
    private SimpleDateFormat fullDateFormat;
    private SimpleDateFormat monthYearDateFormat;

    private List<Event> events;

    public static Calendar privateCalendar;
    public static Calendar publicCalendar;

    private Calendar() {
        this(null);
    }

    private Calendar(Locale locale) {
        this.locale=(locale==null)?Locale.getDefault():locale;

        show = java.util.Calendar.getInstance(this.locale);
        show.set(java.util.Calendar.HOUR,0);
        show.set(java.util.Calendar.MINUTE,0);
        show.set(java.util.Calendar.SECOND,0);
        show.set(java.util.Calendar.MILLISECOND,0);
        now  = show.getTime();
        fullDateFormat = new SimpleDateFormat("EEEE, dd. MMMM yyyy",this.locale);
        monthYearDateFormat = new SimpleDateFormat("MMMM, yyyy",this.locale);
        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));

        initTop();
        initContent();
    }

    public static Calendar getCalendar(Locale locale, boolean shared) {
        if (shared) {
            if (publicCalendar==null) publicCalendar = new Calendar(locale);
            return publicCalendar;
        } else {
            if (privateCalendar==null) privateCalendar = new Calendar(locale);
            return privateCalendar;
        }
    }

    public Calendar setEvents(List<Event> events) {
        this.events = events;
        paintCalendar();
        return this;
    }

    private void initTop() {
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton previous = new JButton(new ImageIcon("src/main/Resources/icons/left_16.png"));
        previous.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prev();
            }
        });
        JButton next = new JButton(new ImageIcon("src/main/Resources/icons/right_16.png"));
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                next();
            }
        });
        monthName = new JLabel();
        top.add(previous);
        top.add(next);
        top.add(monthName);

        Box box = new Box(BoxLayout.LINE_AXIS);
        box.add(top);
        box.setMaximumSize(new Dimension(Integer.MAX_VALUE,30));
        add(box);
    }

    private void initContent() {
        content = new JPanel();
        content.setLayout(new GridLayout(0, 7));
        initHead();
        paintCalendar();
        Box box = new Box(BoxLayout.LINE_AXIS);
        box.add(content);
        add(box);
    }

    private void initHead() {
        JPanel days = new JPanel();
        days.setLayout(new GridLayout(0, 7));
        DateFormatSymbols dfs = new DateFormatSymbols(locale);
        String weekdays[] = dfs.getWeekdays();
        Field mo = Field.getHeadField(weekdays[2]);
        Field tu = Field.getHeadField(weekdays[3]);
        Field we = Field.getHeadField(weekdays[4]);
        Field th = Field.getHeadField(weekdays[5]);
        Field fr = Field.getHeadField(weekdays[6]);
        Field sa = Field.getHeadField(weekdays[7]);
        Field su = Field.getHeadField(weekdays[1]);

        days.add(mo);
        days.add(tu);
        days.add(we);
        days.add(th);
        days.add(fr);
        days.add(sa);
        days.add(su);

        Box box = new Box(BoxLayout.LINE_AXIS);
        box.add(days);
        box.setMaximumSize(new Dimension(Integer.MAX_VALUE,3000));
        add(box);
    }

    public void recreate() {
        content.removeAll();
        content.revalidate();
        content.repaint();
    }

    private String getMonthYear() {
        return monthYearDateFormat.format(show.getTime());
    }

    private void paintCalendar() {
        recreate();

        String mnString = getMonthYear();
        monthName.setText(mnString.substring(0, 1).toUpperCase()+mnString.substring(1));

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
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                paintCalendar();
            }
        });
    }
    public void prev() {
        show.add(java.util.Calendar.MONTH, -1);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                paintCalendar();
            }
        });
    }


    /**
     * Day data structure for Field cell of Month in calendar view
     */
    class Day {
        private Date date;
        private List<Event> dayEvents = new ArrayList<Event>();
        public Day(Date date) {
            if(date == null) {
                throw new IllegalArgumentException("bad generated day");
            }
            this.date = date;
            findEvents();
        }

        public boolean isToday() {
            return date.equals(now);
        }

        public boolean hasEvents() {
            return dayEvents.size()>0;
        }

        public List<Event> getEvents() {
            return dayEvents;
        }

        private void findEvents() {
            if(events == null) {
                return;
            }
            long startDay = date.getTime();
            long endDay = date.getTime()+3600*24*1000-1;

            for(Event event : events) {
                long startEvent = event.getFrom().getTime();
                long endEvent = event.getTo().getTime();

                if(
                        (startEvent <= startDay && endEvent >= startDay)
                        ||
                        (startEvent <= endDay && endEvent >= endDay)
                        ||
                        (startEvent >= startDay && endEvent <= endDay)
                        ) {
                    dayEvents.add(event);
                }

            }
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

    private Field(String text) {
        super(text);
        setOpaque(true);
    }

    private void setHeadLook() {
        setHorizontalAlignment(SwingConstants.CENTER);
        setBackground(Color.LIGHT_GRAY);
    }

    private void setDayLook() {
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

    private void setDay(Calendar.Day day) {
        this.day=day;
    }

    private MouseListener actionMouse = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            new EventListView(day).setVisible(true);
        }
    };
}
