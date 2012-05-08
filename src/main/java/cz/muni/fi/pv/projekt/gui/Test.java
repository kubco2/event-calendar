package cz.muni.fi.pv.projekt.gui;

import cz.muni.fi.pv.projekt.Event;
import cz.muni.fi.pv.projekt.EventManager;
import cz.muni.fi.pv.projekt.User;
import cz.muni.fi.pv.projekt.UserManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: shifty
 * Date: 08/05/12
 * Time: 15:48
 */
public class Test {

    public static void main(String[] args) {

        ApplicationContext springCtx = new ClassPathXmlApplicationContext("spring-context.xml");
        UserManager userManager = (UserManager) springCtx.getBean("userManager");
        EventManager eventManager = (EventManager) springCtx.getBean("eventManager");

        User u1 = userManager.selectUserById(1L);
        Event e = eventManager.selectEventById(1L);

        EventView ev = new EventViewNew(u1);
        ev.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ev.setVisible(true);
    }

}
